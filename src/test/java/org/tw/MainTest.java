package org.tw;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class MainTest {

  public static final String[] ORIGINAL_ANIMALS =
      new String[] {"fly", "spider", "bird", "cat", "dog", "cow", "horse"};
  public static final String GOLDEN_MASTER_PATH = "goldenMaster.txt";

  private ByteArrayOutputStream inMemoryStream;

  @BeforeEach
  public void setupOutput() {
    inMemoryStream = new ByteArrayOutputStream();
    PrintStream printedSong = new PrintStream(inMemoryStream);
    System.setOut(printedSong);
  }

  @Test
  void theOriginalShouldMatchGoldenMaster() {
    Main.main(ORIGINAL_ANIMALS);
    List<String> actual = read(inMemoryStream);
    List<String> expected = read(GOLDEN_MASTER_PATH);
    assertEquals(expected, actual);
  }

  private static List<String> read(ByteArrayOutputStream inMemoryStream) {
    try {
      return readStream(new ByteArrayInputStream(inMemoryStream.toByteArray()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static List<String> read(String path) {
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classloader.getResourceAsStream(path);
    try {
      return readStream(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static List<String> readStream(InputStream byteArrayInputStream) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
    List<String> result = new ArrayList<>();
    while (reader.ready()) {
      result.add(reader.readLine());
    }
    return result;
  }

  public static void main(String[] args) throws FileNotFoundException {
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    PrintStream goldenMasterFile = getPrintStream(classloader, GOLDEN_MASTER_PATH);

    System.setOut(goldenMasterFile);
    Main.main(ORIGINAL_ANIMALS);
    goldenMasterFile.close();
  }

  private static PrintStream getPrintStream(ClassLoader classloader, String path) {

    return Optional.ofNullable(classloader.getResource(path))
        .map(
            resource -> {
              try {
                return new FileOutputStream(resource.getFile());
              } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
              }
            })
        .map(PrintStream::new)
        .orElseGet(
            () -> {
              try {
                return new PrintStream(path);
              } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
              }
            });
  }
}
