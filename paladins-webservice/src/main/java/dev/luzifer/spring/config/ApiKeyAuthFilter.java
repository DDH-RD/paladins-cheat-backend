package dev.luzifer.spring.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
public class ApiKeyAuthFilter extends AbstractAuthenticationProcessingFilter {

  private final String headerName;

  public ApiKeyAuthFilter(String headerName, AuthenticationManager authenticationManager) {
    super(new AntPathRequestMatcher("/api/**"));

    this.headerName = headerName;
    setAuthenticationManager(authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String apiKey = request.getHeader(headerName);
    apiKey = apiKey == null ? null : apiKey.trim();
    log.debug("Received API key: {}", apiKey);
    if (apiKey == null || apiKey.isEmpty()) {
      log.debug("API Key not found in request header");
      throw new BadCredentialsException("API Key not found in request header");
    }
    log.debug("Attempting to authenticate API key");
    return getAuthenticationManager().authenticate(new ApiKeyAuthenticationToken(apiKey));
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {
    SecurityContextHolder.getContext().setAuthentication(authResult);
    chain.doFilter(request, response);
  }

  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    super.unsuccessfulAuthentication(request, response, failed);
  }
}
