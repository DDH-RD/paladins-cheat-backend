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

    log.info(
        "Authenticating API key {} against actual API key {}",
        authenticationToken.getCredentials(),
        apiKey);
    if (authenticationToken.getCredentials() instanceof String credentials) {
      if (apiKey.equals(credentials)) {
        log.info("API key {} authenticated successfully", apiKey);
        return new ApiKeyAuthenticationToken(apiKey, apiKey);
      }
      log.info("API key {} was not the expected value", credentials);
    }

    throw new BadCredentialsException("The API key was not found or not the expected value.");
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
