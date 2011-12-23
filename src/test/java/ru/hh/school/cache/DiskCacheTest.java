package ru.hh.school.cache;

import java.io.File;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import ru.hh.school.cache.disk.DiskCache;
import ru.hh.school.cache.strategy.LRU;
import static ru.hh.school.cache.util.FileUtil.rm;

public class DiskCacheTest {

  private final static int CACHE_SIZE = 3;
  private final static File CACHE_DIR = new File("test-cache");

  private DiskCache cache;

  @Before
  public void setUp() {
    rm(CACHE_DIR);
    CACHE_DIR.mkdir();
    cache = new DiskCache(CACHE_DIR, new LRU(), CACHE_SIZE);
  }

  @Test public void eviction() {
    for (int i = 0; i <= CACHE_SIZE; i++)
      cache.put("foo" + i, 1);
    assertNull(cache.get("foo0"));
    for (int i = 1; i < CACHE_SIZE; i++)
      assertEquals(1, cache.get("foo" + i));
    assertEquals(CACHE_SIZE, cache.size());
  }

  @Test public void reload() {
    cache.put("foo", 1);
    cache.put("bar", 1);
    cache.put("baz", 1);

    cache = new DiskCache(CACHE_DIR, new LRU(), CACHE_SIZE - 1);
    assertNull(cache.get("foo"));
    assertEquals(1, cache.get("bar"));
    assertEquals(1, cache.get("baz"));
  }
}
