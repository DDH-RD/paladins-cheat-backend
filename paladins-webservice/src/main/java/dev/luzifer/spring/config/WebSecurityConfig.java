package dev.luzifer.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

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
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.addFilterBefore(
                        apiKeyAuthFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAfter(apiKeyAuthFilter(authenticationManager), BasicAuthenticationFilter.class);

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
