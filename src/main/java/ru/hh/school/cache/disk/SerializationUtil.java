package ru.hh.school.cache.disk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializationUtil {
  /**
   * @return true if new file created. Otherwise false
   */
  public static boolean writeObjectToFile(Serializable value, File dest) throws IOException {
    ObjectOutputStream objOut = null;
    try {
      boolean exists = dest.exists();
      objOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dest)));
      objOut.writeObject(value);
      return !exists;
    } finally {
      if (objOut != null)
        objOut.close();
    }
  }

  /**
   * @return object stored in specified file or null if file does not exists
   */
  public static Serializable readObjectFromFile(File src) throws IOException, ClassNotFoundException {
    ObjectInputStream objIn = null;
    try {
      if (!src.exists())
        return null;
      objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(src)));
      return (Serializable) objIn.readObject();
    } finally {
      if (objIn != null)
        objIn.close();
    }
  }
}
