package dev.luzifer.spring.config;

import dev.luzifer.PaladinsWebservice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

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
    if (apiKey == null) {
      apiKey = PaladinsWebservice.getApiKey();
    }
    apiKey = apiKey.trim();
    UsernamePasswordAuthenticationToken authRequest =
        new UsernamePasswordAuthenticationToken(apiKey, null);
    return getAuthenticationManager().authenticate(authRequest);
  }
}
