package org.tw;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Song {
  public static final String FINAL_ELLIPSIS = "...";
  public static final String SEMI_COLON = ";";
  private static final String[] EXCLAMATION_FORMATS =
      new String[] {
        "",
        "That wriggled and wiggled and tickled inside her.%n",
        "How absurd to swallow a %s.%n",
        "Fancy that to swallow a %s!%n",
        "What a hog, to swallow a %s!%n",
        "I don't know how she swallowed a %s!%n",
        "...She's dead, of course!"
      };

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

    StringBuilder trailer = new StringBuilder();
    String punctuation = SEMI_COLON;

    final int lastVerse = animals.length - 1;
    if (verse == lastVerse) {
      // delivering punchline
      punctuation = FINAL_ELLIPSIS;
      trailer = new StringBuilder();
    } else {
      trailer.append(
          String.format(
              "I don't know why she swallowed a %s - perhaps she'll die!%n%n", animals[0]));
      if (verse == 0) {
        // first verse
        punctuation = ".";
      } else {
        trailer.insert(
            0,
            IntStream.range(0, verse)
                .boxed()
                .sorted(Collections.reverseOrder())
                .map(this::toCatchThe)
                .collect(Collectors.joining(",\n", "", ";\n")));
      }
    }

    trailer.insert(0, String.format(EXCLAMATION_FORMATS[verse], animals[verse]));
    trailer.insert(
        0,
        String.format("There was an old lady who swallowed a %s%s%n", animals[verse], punctuation));
    return trailer.toString();
  }

  private String toCatchThe(int index) {
    String predator = animals[index + 1];
    String meal = animals[index];
    return String.format("She swallowed the %s to catch the %s", predator, meal);
  }
}
