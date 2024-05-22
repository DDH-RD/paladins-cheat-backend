package dev.luzifer.data.service;

import dev.luzifer.data.converter.EntityConverter;

public abstract class BaseService {

  protected final EntityConverter entityConverter;

  public BaseService(EntityConverter entityConverter) {
    this.entityConverter = entityConverter;
  }
}
