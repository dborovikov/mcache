package ru.hh.school.cache.util;

public class Pair<K, V> {

  public final K fst;
  public final V snd;

  public Pair(K fst, V snd) {
    this.fst = fst;
    this.snd = snd;
  }

  public static <K,V> Pair<K, V> of(K k, V v) {
    return new Pair<K, V>(k, v);
  }
}