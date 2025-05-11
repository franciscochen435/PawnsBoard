package cs3500.pawnsboard.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.Player;

/**
 * Represents a utility class that reads a deck configuration file and
 * converts it into a list of Card for use in the game.
 * Each card in the file is expected to have a name, cost, value, and an influence grid of 5 rows.
 */
public class DeckReader {
  /**
   * Reads a deck configuration file and returns a list of Card objects.
   *
   * @param filePath The path to the deck configuration file.
   * @return A list of Card objects.
   * @throws IOException If an I/O error occurs while reading the file.
   * @throws FileNotFoundException If the file cannot be found.
   * @throws IllegalStateException If the file format is invalid
   *                               or if a card appears more than twice.
   */
  public static List<PawnsBoardCard> readDeck(String filePath, Player owner) throws IOException {
    List<PawnsBoardCard> deck = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    Map<String, Integer> names = new HashMap<>();

    String line;
    while ((line = reader.readLine()) != null) {
      line = line.trim();
      if (line.isEmpty()) {
        continue;
      }

      String[] parts = line.split(" ");
      if (parts.length != 3) {
        throw new IllegalStateException("Invalid card format");
      }

      String name = parts[0];
      names.put(name, names.getOrDefault(name, 0) + 1);
      if (names.get(name) > 2) {
        throw new IllegalStateException("Cannot have more than 2 card of the same name");
      }
      int cost = Integer.parseInt(parts[1]);
      int value = Integer.parseInt(parts[2]);

      String[] influence = new String[5];
      for (int i = 0; i < 5; i++) {
        influence[i] = reader.readLine().trim();
      }

      deck.add(new Card(name, cost, value, influence, owner));
    }
    reader.close();
    return deck;
  }
}

