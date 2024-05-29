package dev.luzifer.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

  private final String apiKey;

  public ApiKeyAuthenticationProvider(String apiKey) {
    this.apiKey = apiKey;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    ApiKeyAuthenticationToken authenticationToken = (ApiKeyAuthenticationToken) authentication;

    log.debug("Received API key: {}", authenticationToken.getCredentials());
    if (authenticationToken.getCredentials() instanceof String credentials) {
      log.debug("Comparing API key with expected value");
      if (apiKey.equals(credentials)) {
        log.debug("API key is valid");
        ApiKeyAuthenticationToken authenticatedToken = new ApiKeyAuthenticationToken(apiKey);
        authenticatedToken.setAuthenticated(true);
        return authenticatedToken;
      }
    }

    log.debug("API key is invalid");
    throw new BadCredentialsException("The API key was not found or not the expected value.");
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
