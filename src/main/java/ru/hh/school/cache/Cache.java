package ru.hh.school.cache;

import java.io.Serializable;

public interface Cache {
  void put(String key, Serializable value);
  Serializable get(String key);
}
