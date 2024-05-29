package dev.luzifer.spring.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

@Slf4j
public class ApiKeyAuthFilter extends AbstractAuthenticationProcessingFilter {

  private final String headerName;

  public ApiKeyAuthFilter(String headerName, AuthenticationManager authenticationManager) {
    super("/");

    this.headerName = headerName;
    setAuthenticationManager(authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String apiKey = request.getHeader(headerName);
    apiKey = apiKey == null ? null : apiKey.trim();
    log.info("API Key: {}", apiKey);
    if (apiKey == null || apiKey.isEmpty()) {
      log.info("API Key not found in request header");
      throw new BadCredentialsException("API Key not found in request header");
    }
    log.info("API Key found in request header, authenticating");
    return getAuthenticationManager().authenticate(new ApiKeyAuthenticationToken(apiKey));
  }
}
