package ru.hh.school.cache;

import static com.google.common.collect.Lists.newArrayList;
import java.io.Serializable;
import java.util.List;

public class MultiLevelCache implements Cache {

  private final List<BoundedCache> caches = newArrayList();
  private final CacheIntegrationStrategy integrationStrategy;

  public MultiLevelCache(CacheIntegrationStrategy integrationStrategy) {
    this.integrationStrategy = integrationStrategy;
  }

  public MultiLevelCache addLevel(BoundedCache cache) {
    caches.add(cache);
    return this;
  }

  public void put(String key, Serializable value) {
    integrationStrategy.put(caches, key, value);
  }

  public Serializable get(String key) {
    return integrationStrategy.get(caches, key);
  }
}
