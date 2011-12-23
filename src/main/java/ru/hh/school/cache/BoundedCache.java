package ru.hh.school.cache;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Lists.newArrayList;
import java.io.Serializable;
import java.util.List;
import ru.hh.school.cache.util.Pair;

public abstract class BoundedCache implements Cache {

  protected final int maxSize;
  protected final CacheStrategy strategy;

  public BoundedCache(int maxSize, CacheStrategy strategy) {
    checkArgument(maxSize > 0);
    checkNotNull(strategy);
    this.strategy = strategy;
    this.maxSize = maxSize;
  }

  public Serializable get(String key) {
    Serializable value = getInternal(key);
    if (value == null)
      return null;
    strategy.track(key);
    return value;
  }

  public void put(String key, Serializable value) {
    putForcingOut(key, value);
  }

  public List<Pair<String, Serializable>> putForcingOut(String key, Serializable value) {
    checkState(size() <= maxSize);
    List<Pair<String, Serializable>> removed = newArrayList();
    if (getInternal(key) == null && size() == maxSize)
      for (String candidate : strategy.worseKeys())
        removed.add(Pair.of(candidate, remove(candidate)));
    putInternal(key, value);
    strategy.track(key);
    return removed;
  }

  public Serializable remove(String key) {
    strategy.untrack(key);
    return removeInternal(key);
  }

  protected abstract Serializable getInternal(String key);

  protected abstract void putInternal(String key, Serializable value);

  protected abstract Serializable removeInternal(String key);

  public abstract int size();
}
