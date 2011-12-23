package ru.hh.school.cache.strategy;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import ru.hh.school.cache.CacheStrategy;

public class LRU implements CacheStrategy {

  // Fast removing, fast insertion, optimal memory usage, supports insertion order
  private final LinkedHashSet<String> keys = newLinkedHashSet();

  private final int batchSize;

  /**
   * Creates new LRU strategy with batchSize == 1
   */
  public LRU() {
    this(1);
  }

  /**
   * @param batchSize Maximal number of worse keys (must be positive)
   */
  public LRU(int batchSize) {
    checkArgument(batchSize >= 0);
    this.batchSize = batchSize;
  }

  public void track(String key) {
    checkNotNull(key);
    keys.remove(key);
    keys.add(key);
  }

  public void untrack(String key) {
    checkNotNull(key);
    keys.remove(key);
  }

  public Iterable<String> worseKeys() {
    checkState(!keys.isEmpty());
    List<String> worse = newArrayList();
    Iterator<String> it = keys.iterator();
    int i = 0;
    while (i < batchSize && it.hasNext()) {
      worse.add(it.next());
      i++;
    }
    checkArgument(!isEmpty(worse));
    return worse;
  }
}
