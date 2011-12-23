package ru.hh.school.cache.disk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileUtil {

  private TextFileUtil(){}

  public static void appendLineToFile(File file, String text) throws IOException {
    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(file, true));
      bw.write(text);
      bw.newLine();
    } finally {
      if (bw != null)
        bw.close();
    }
  }
}
