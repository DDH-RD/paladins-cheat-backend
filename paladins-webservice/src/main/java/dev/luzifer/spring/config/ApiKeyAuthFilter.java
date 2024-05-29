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
    setAuthenticationFailureHandler(
        (request, response, exception) -> {
          log.debug("Failed to authenticate API key", exception);
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        });
    setAuthenticationSuccessHandler(
        (request, response, authentication) -> {
          log.debug("API key authenticated successfully");
          response.setStatus(HttpServletResponse.SC_OK);
        });
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String apiKey = request.getHeader(headerName);
    apiKey = apiKey == null ? null : apiKey.trim();
    if (apiKey == null || apiKey.isEmpty()) {
      throw new BadCredentialsException("API Key not found in request header");
    }
    return getAuthenticationManager().authenticate(new ApiKeyAuthenticationToken(apiKey));
  }
}
