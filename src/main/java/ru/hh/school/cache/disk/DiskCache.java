package ru.hh.school.cache.disk;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hh.school.cache.BoundedCache;
import ru.hh.school.cache.CacheStrategy;
import static ru.hh.school.cache.disk.SerializationUtil.readObjectFromFile;
import static ru.hh.school.cache.disk.SerializationUtil.writeObjectToFile;

public class DiskCache extends BoundedCache {

  private final static Logger LOGGER = LoggerFactory.getLogger(DiskCache.class);

  private final File baseCacheDir;
  private int counter;
 
  public DiskCache(File baseCacheDir, CacheStrategy strategy, int maxSize) {
    super(maxSize, new StrategyPersistenceDecorator(new File(baseCacheDir, "strategy.log"), strategy));
    this.baseCacheDir = baseCacheDir;
    actualize();
  }

  private void actualize() {
    for (File file : baseCacheDir.listFiles()) {
      if (!file.getName().startsWith(FILE_PREFIX))
        continue;
      counter++;
    }
    while (counter > maxSize)
      for (String key : strategy.worseKeys())
        remove(key);
  }

  @Override
  protected Serializable removeInternal(String key) {
    File file = key2file(key);
    if (!file.exists())
      return null;
    Serializable value;
    try {
      value = readObjectFromFile(file);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return null;
    }
    file.delete();
    counter--;
    return value;
  }

  @Override
  protected void putInternal(String key, Serializable value) {
    try {
      if (writeObjectToFile(value, key2file(key)))
        counter++;
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  @Override
  protected Serializable getInternal(String key) {
    try {
      return readObjectFromFile(key2file(key));
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public int size() {
    return counter;
  }

  private final static String FILE_PREFIX = "key-";

  private File key2file(String key) {
    try {
      return new File(baseCacheDir, FILE_PREFIX + URLEncoder.encode(key, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
