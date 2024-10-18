package com.library.management.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class FileReader {

  private FileReader() {
    throw new IllegalStateException("Utility class");
  }

  public static String fromFile(String filename) {
    try {
      File file = resolveToFile(filename);
      return new String(Files.readAllBytes(file.toPath()));
    } catch (URISyntaxException | IOException e) {
      throw new IllegalArgumentException("Could not read file: " + filename);
    }
  }

  public static File resolveToFile(String filePath) throws URISyntaxException {
    return new File(FileReader.class.getClassLoader().getResource(filePath).toURI());
  }
}
