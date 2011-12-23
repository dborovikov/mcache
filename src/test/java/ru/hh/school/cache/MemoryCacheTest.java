package ru.hh.school.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import ru.hh.school.cache.memory.MemoryCache;
import ru.hh.school.cache.strategy.LRU;

public class MemoryCacheTest {

  private final static int CACHE_SIZE = 3;

  private MemoryCache cache;

  @Before
  public void setUp() {
    cache = new MemoryCache(new LRU(), CACHE_SIZE);
  }

  @Test public void eviction() {
    for (int i = 0; i <= CACHE_SIZE; i++)
      cache.put("foo" + i, 1);
    assertNull(cache.get("foo0"));
    for (int i = 1; i < CACHE_SIZE; i++)
      assertEquals(1, cache.get("foo" + i));
    assertEquals(CACHE_SIZE, cache.size());
  }
}
