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

    log.info("Authenticating API key: {}", authenticationToken.getCredentials());
    if (authenticationToken.getCredentials() instanceof String credentials) {
      if (apiKey.equals(credentials)) {
        log.info("API key authenticated successfully");
        return new ApiKeyAuthenticationToken(apiKey);
      }
    }

    log.info("API key was not found or not the expected value");
    throw new BadCredentialsException("The API key was not found or not the expected value.");
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
