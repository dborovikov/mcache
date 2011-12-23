package ru.hh.school.cache;

import static com.google.common.collect.Lists.newArrayList;
import java.io.Serializable;
import java.util.List;
import ru.hh.school.cache.util.Pair;

public class OverflowStrategy implements CacheIntegrationStrategy {

  public void put(Iterable<BoundedCache> caches, String key, Serializable value) {
    List<Pair<String, Serializable>> toPut = newArrayList();
    toPut.add(Pair.of(key, value));
    for (BoundedCache cache : caches) {
      List<Pair<String, Serializable>> forcedOut = newArrayList();
      for (Pair<String, Serializable> pair : toPut)
        forcedOut.addAll(cache.putForcingOut(pair.fst, pair.snd));
      toPut = forcedOut;
    }
  }

  public Serializable get(Iterable<BoundedCache> caches, String key) {
    Serializable value;
    for (BoundedCache cache : caches)
      if ((value = cache.get(key)) != null)
        return value;
    return null;
  }
}
