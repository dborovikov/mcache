package ru.hh.school.cache;

public interface CacheStrategy {
  
 void track(String key);

 void untrack(String key);
  
  /**
   * @return Non empty sequence of worse keys.
   * You must track at lest one key before call this method.
   */
 Iterable<String> worseKeys();
}
