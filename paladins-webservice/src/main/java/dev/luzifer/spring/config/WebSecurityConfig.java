package dev.luzifer.spring.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {

  private final String apiKey;
  private final String apiKeyHeader;

  public WebSecurityConfig(
          @Value("${api.key}") String apiKey, @Value("${api.key.header}") String apiKeyHeader) {
    this.apiKey = apiKey;
    this.apiKeyHeader = apiKeyHeader;
  }

  @Bean
  public ApiKeyAuthFilter apiKeyAuthFilter(AuthenticationManager authenticationManager) {
    return new ApiKeyAuthFilter(apiKeyHeader, authenticationManager);
  }

  @Bean
  public FilterRegistrationBean<OncePerRequestFilter> apiKeyAuthFilterRegistrationBean(AuthenticationManager authenticationManager) {
    FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new OncePerRequestFilter() {
      @Override
      protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
          ApiKeyAuthFilter apiKeyAuthFilter = new ApiKeyAuthFilter(apiKeyHeader, authenticationManager);
          apiKeyAuthFilter.doFilter(request, response, filterChain);
        } else {
          filterChain.doFilter(request, response);
        }
      }
    });

    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);

    return registrationBean;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
          HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .anonymous(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(apiKeyAuthFilter(authenticationManager), BasicAuthenticationFilter.class);

    log.debug("API key: {}", apiKey);
    log.debug("API key header: {}", apiKeyHeader);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
          AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public ApiKeyAuthenticationProvider apiKeyAuthenticationProvider() {
    return new ApiKeyAuthenticationProvider(apiKey);
  }

  @Autowired
  public void configureGlobal(
          AuthenticationManagerBuilder auth,
          ApiKeyAuthenticationProvider apiKeyAuthenticationProvider) {
    auth.authenticationProvider(apiKeyAuthenticationProvider);
  }
}