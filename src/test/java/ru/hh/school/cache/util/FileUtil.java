package ru.hh.school.cache.util;

import java.io.File;

public class FileUtil {
  private FileUtil() {}

  public static void rm(File file) {
    if (!file.exists())
      return;
    if (file.isDirectory())
      for (File f : file.listFiles())
        rm(f);
    file.delete();
  }
}
