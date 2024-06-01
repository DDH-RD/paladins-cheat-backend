package dev.luzifer.spring.config.async;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

@Slf4j
public class AsyncUncaughtExceptionHandlerImpl implements AsyncUncaughtExceptionHandler {

  @Override
  public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
    log.error("Exception message - " + throwable.getMessage());
    log.error("Method name - " + method.getName());
    for (Object param : objects) {
      log.error("Parameter value - " + param);
    }
  }
}
