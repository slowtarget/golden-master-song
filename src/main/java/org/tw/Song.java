package org.tw;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Song {
  public static final String FIRST_POINT = ".";
  public static final String SEMI_COLON = ";";
  public static final String FINAL_ELLIPSIS = "...";
  private static final String[] EXCLAMATIONS =
      new String[] {
        "",
        "That wriggled and wiggled and tickled inside her.%n",
        "How absurd to swallow a %s.%n",
        "Fancy that to swallow a %s!%n",
        "What a hog, to swallow a %s!%n",
        "I don't know how she swallowed a %s!%n",
        "...She's dead, of course!"
      };

  // "fly", "spider", "bird", "cat", "dog", "cow", "horse"
//    "idea", "thread", "word", "chat", "blog", "flow", "program"
  public static final String THERE_WAS_AN_OLD_LADY_WHO_SWALLOWED_A = "There was an old lady who swallowed a %s%s%n";
  public static final String TO_CATCH_THE = "She swallowed the %s to catch the %s";
  public static final String TO_CATCH_DELIMITER = String.format(",%n");
  public static final String TO_CATCH_SUFFIX = String.format(";%n");
  public static final String I_DON_T_KNOW_WHY_SHE_SWALLOWED_PERHAPS_SHE_LL_DIE = "I don't know why she swallowed a %s - perhaps she'll die!%n%n";

  private final String[] animals;

  public Song(String[] animals) {
    this.animals = animals;
  }

  String song(String[] animals) {
    return IntStream.range(0, animals.length)
        .mapToObj(this::thereWasAnOldLadyWhoSwallowedA)
        .collect(Collectors.joining());
  }

  private String thereWasAnOldLadyWhoSwallowedA(int verse) {

    StringBuilder verseBuilder = new StringBuilder();
    String punctuation = SEMI_COLON;

    final int lastVerse = animals.length - 1;
    if (verse == lastVerse) {
      // delivering punchline
      punctuation = FINAL_ELLIPSIS;
    } else {
      verseBuilder.append(
          String.format(I_DON_T_KNOW_WHY_SHE_SWALLOWED_PERHAPS_SHE_LL_DIE, animals[0]));
      if (verse == 0) {
        // first verse
        punctuation = FIRST_POINT;
      } else {
        verseBuilder.insert(
            0,
            IntStream.range(0, verse)
                .boxed()
                .sorted(Collections.reverseOrder())
                .map(this::toCatchThe)
                .collect(Collectors.joining(TO_CATCH_DELIMITER, "", TO_CATCH_SUFFIX)));
      }
    }

    verseBuilder.insert(0, String.format(EXCLAMATIONS[verse], animals[verse]));
    verseBuilder.insert(
        0,
        String.format(THERE_WAS_AN_OLD_LADY_WHO_SWALLOWED_A, animals[verse], punctuation));
    return verseBuilder.toString();
  }

  private String toCatchThe(int index) {
    String predator = animals[index + 1];
    String meal = animals[index];
    return String.format(TO_CATCH_THE, predator, meal);
  }
}
