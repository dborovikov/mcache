package ru.hh.school.cache;

import java.io.File;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import ru.hh.school.cache.disk.DiskCache;
import ru.hh.school.cache.memory.MemoryCache;
import ru.hh.school.cache.strategy.LRU;
import static ru.hh.school.cache.util.FileUtil.rm;

public class MultiLevelCacheTest {

  private final static File CACHE_DIR = new File("test-cache");

  MemoryCache memoryCache;
  DiskCache diskCache;

  @Before
  public void setUp() {
    rm(CACHE_DIR);
    CACHE_DIR.mkdir();

    memoryCache = new MemoryCache(new LRU(), 3);
    diskCache = new DiskCache(CACHE_DIR, new LRU(), 4);
  }

  @Test
  public void fanoutSmokeTest() {
    MultiLevelCache cache = new MultiLevelCache(new FanoutStrategy())
      .addLevel(memoryCache)
      .addLevel(diskCache);
    
    cache.put("foo", 1);
    cache.put("bar", 1);
    cache.put("baz", 1);
    cache.put("ggg", 1);
    cache.put("fff", 1);

    assertNull(memoryCache.get("foo"));
    assertNull(memoryCache.get("bar"));
    assertEquals(1, memoryCache.get("baz"));
    assertEquals(1, memoryCache.get("ggg"));
    assertEquals(1, memoryCache.get("fff"));

    assertNull(diskCache.get("foo"));
    assertEquals(1, diskCache.get("bar"));
    assertEquals(1, diskCache.get("baz"));
    assertEquals(1, diskCache.get("ggg"));
    assertEquals(1, diskCache.get("fff"));

    assertEquals(1, cache.get("baz"));
    assertEquals(1, cache.get("ggg"));
    assertEquals(1, cache.get("fff"));
    assertEquals(1, cache.get("bar"));

    assertNull(cache.get("foo"));
    
    assertNotNull(memoryCache.get("bar"));
  }

  @Test
  public void overflowSmokeTest() {

    MultiLevelCache cache = new MultiLevelCache(new OverflowStrategy())
        .addLevel(memoryCache)
        .addLevel(diskCache);

    cache.put("foo", 1);
    cache.put("bar", 1);
    cache.put("baz", 1);
    cache.put("ggg", 1);
    cache.put("fff", 1);

    assertNull(memoryCache.get("foo"));
    assertNull(memoryCache.get("bar"));
    assertEquals(1, memoryCache.get("baz"));
    assertEquals(1, memoryCache.get("ggg"));
    assertEquals(1, memoryCache.get("fff"));

    assertEquals(1, diskCache.get("foo"));
    assertEquals(1, diskCache.get("bar"));
    assertNull(diskCache.get("baz"));
    assertNull(diskCache.get("ggg"));
    assertNull(diskCache.get("fff"));

    assertEquals(1, cache.get("foo"));
    assertEquals(1, cache.get("bar"));
    assertEquals(1, cache.get("baz"));
    assertEquals(1, cache.get("ggg"));
    assertEquals(1, cache.get("fff"));
  }
}
