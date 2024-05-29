package dev.luzifer.spring.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

  private final String apiKey;

  public ApiKeyAuthenticationToken(String apiKey) {
    super(null);
    this.apiKey = apiKey;
    setAuthenticated(false);
  }

  @Override
  public Object getCredentials() {
    return apiKey;
  }

  @Override
  public Object getPrincipal() {
    return apiKey;
  }
}
