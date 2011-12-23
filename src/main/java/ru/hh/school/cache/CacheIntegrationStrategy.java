package ru.hh.school.cache;

import java.io.Serializable;

public interface CacheIntegrationStrategy {
  public void put(Iterable<BoundedCache> caches, String key, Serializable value);
  public Serializable get(Iterable<BoundedCache> caches, String key);
}
