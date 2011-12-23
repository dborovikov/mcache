package ru.hh.school.cache.memory;

import static com.google.common.collect.Maps.newHashMap;
import java.io.Serializable;
import java.util.Map;
import ru.hh.school.cache.BoundedCache;
import ru.hh.school.cache.CacheStrategy;

public class MemoryCache extends BoundedCache {

  private final Map<String, Serializable> map = newHashMap();

  public MemoryCache(CacheStrategy strategy, int maxSize) {
    super(maxSize, strategy);
  }

  @Override
  protected Serializable removeInternal(String key) {
    return map.remove(key);
  }

  @Override
  protected void putInternal(String key, Serializable value) {
    map.put(key, value);
  }

  @Override
  protected Serializable getInternal(String key) {
    return map.get(key);
  }

  @Override
  public int size() {
    return map.size();
  }
}
