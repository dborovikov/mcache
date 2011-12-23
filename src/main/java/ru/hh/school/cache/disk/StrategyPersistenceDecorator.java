package ru.hh.school.cache.disk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hh.school.cache.CacheStrategy;
import static ru.hh.school.cache.disk.TextFileUtil.appendLineToFile;

public class StrategyPersistenceDecorator implements CacheStrategy {

  private final static Logger LOGGER = LoggerFactory.getLogger(StrategyPersistenceDecorator.class);

  private final CacheStrategy target;
  private final File log;

  public StrategyPersistenceDecorator(File log, CacheStrategy target) {
    this.target = target;
    this.log = log;
    replay();
  }

  private void replay() {
    if (!log.exists()) {
      try {
        log.createNewFile();
      } catch (IOException e) {
        LOGGER.error(e.getMessage(), e);
      }
    }
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(log));
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith("track:"))
          target.track(line.substring("track:".length()));
        if (line.startsWith("untrack:"))
          target.untrack(line.substring("untrack:".length()));
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e.getMessage(), e);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException ignored) {}
      }
    }
  }

  public void track(String key) {
    target.track(key);
    writeTrack(key);
  }

  public void untrack(String key) {
    target.untrack(key);
    writeUntrack(key);
  }

  public Iterable<String> worseKeys() {
    return target.worseKeys();
  }

  private void writeTrack(String key) {
    write("track:" + key);
  }

  private void writeUntrack(String key) {
    write("untrack:" + key);
  }

  private void write(String str) {
    try {
      appendLineToFile(log, str);
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }
}
