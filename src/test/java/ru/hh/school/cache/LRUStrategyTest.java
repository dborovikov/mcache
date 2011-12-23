package ru.hh.school.cache;

import static com.google.common.collect.Iterables.getOnlyElement;
import static com.google.common.collect.Iterables.size;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import ru.hh.school.cache.strategy.LRU;

public class LRUStrategyTest {

  private LRU lru;

  @Before
  public void setUp() {
    lru = new LRU();
  }

  @Test public void getNextWorseKeysWithoutTrackedKeys() {
    try {
      lru.worseKeys();
      fail();
    } catch (IllegalStateException expected) {}
  }

  @Test public void trackOneValue() {
    lru.track("foo");
    Iterable<String> keys = lru.worseKeys();
    assertEquals(1, size(keys));
    assertEquals("foo", getOnlyElement(keys));
  }

  @Test public void trackOneValueAndGetWorseTwice() {
    lru.track("foo");
    Iterable<String> keys = lru.worseKeys();
    assertEquals(1, size(keys));
    assertEquals("foo", getOnlyElement(keys));
    keys = lru.worseKeys();
    assertEquals(1, size(keys));
    assertEquals("foo", getOnlyElement(keys));
  }

  @Test public void untrack() {
    lru.track("foo");
    lru.untrack("foo");
    try {
      lru.worseKeys();
      fail();
    } catch (IllegalStateException expected) {}
  }

  @Test public void getOneWorseKey() {
    lru.track("foo");
    lru.track("bar");
    Iterable<String> keys = lru.worseKeys();
    assertEquals(1, size(keys));
    assertEquals("foo", getOnlyElement(keys));
  }

  @Test public void retrack() {
    lru.track("foo");
    lru.track("bar");
    lru.track("foo");
    Iterable<String> keys = lru.worseKeys();
    assertEquals(1, size(keys));
    assertEquals("bar", getOnlyElement(keys));
  }

  @Test public void retackAndUntrackTogether() {
    lru.track("foo");
    lru.track("bar");
    lru.track("baz");

    lru.track("foo");
    lru.untrack("bar");

    Iterable<String> keys = lru.worseKeys();
    assertEquals(1, size(keys));
    assertEquals("baz", getOnlyElement(keys));
  }
}
