package ru.hh.school.cache;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import java.io.Serializable;

public class FanoutStrategy implements CacheIntegrationStrategy {

  public void put(Iterable<BoundedCache> caches, String key, Serializable value) {
    for (Cache cache : caches)
      cache.put(key, value);
  }

  public Serializable get(Iterable<BoundedCache> caches, String key) {
    for (Cache cache : caches) {
      Serializable value = cache.get(key);
      if (value != null) {
        put(filter(caches, not(equalTo(cache))), key, value);
        return value;
      }
    }
    return null;
  }
}
