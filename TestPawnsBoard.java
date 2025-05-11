package cs3500.pawnsboard;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cs3500.pawnsboard.model.Board;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.controller.DeckReader;
import cs3500.pawnsboard.model.Pawns;
import cs3500.pawnsboard.model.PawnsBoardInterface;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.view.PawnsBoardTextualView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * This class is used to test every constructor and method defined in this project, to make sure
 * the mechanism and the setting of pawns board game is correct and players can play it for fun.
 */
public class TestPawnsBoard {
  Pawns p1;
  Pawns p2;
  Card c1;
  Card c2;
  List<Card> redDeck = new ArrayList<>();
  List<Card> blueDeck = new ArrayList<>();
  PawnsBoardInterface board;
  PawnsBoardInterface invalidBoard;
  PawnsBoardTextualView view;
  String path1;
  String path2;


  @Before
  public void setUp() throws IOException {
    this.p1 = new Pawns(Player.Red, 1);
    this.p2 = new Pawns(Player.Blue, 2);
    String[] influence1 = { "XXXXX", "XXIXX", "XICIX", "XXIXX", "XXXXX" };
    String[] influence2 = { "XXXXX", "XXXXX", "XXCXI", "XXXXX", "XXXXX" };
    this.c1 = new Card("Security", 2, 1, influence1, Player.Red);
    this.c2 = new Card("Lobber", 1, 2, influence2, Player.Blue);
    this.path1 = "docs" + File.separator + "redDeck.config";
    this.path2 = "docs" + File.separator + "blueDeck.config";
    this.redDeck = DeckReader.readDeck(path1, Player.Red);
    this.blueDeck = DeckReader.readDeck(path2, Player.Blue);
    this.board = new Board(3, 5, new Random());
    this.invalidBoard = new Board(3, 5);
    this.board.startGame(redDeck, blueDeck, false,1);
    this.view = new PawnsBoardTextualView(board);
  }

  @Test
  public void testPawnsConstructor() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Pawns(Player.Red, 4);
    } );
    assertEquals(1, this.p1.getCount());
    assertEquals(2, this.p2.getCount());
  }

  @Test
  public void testPawnsGetOwner() {
    assertEquals(Player.Red, this.p1.getOwner());
    assertEquals(Player.Blue, this.p2.getOwner());
  }

  @Test
  public void testPawnsGetCount() {
    assertEquals(1, this.p1.getCount());
    assertEquals(2, this.p2.getCount());
  }

  @Test
  public void testPawnsCanPlacedCard() {
    assertFalse(this.p1.canPlaceCard(this.c1));
    assertTrue(this.p2.canPlaceCard(this.c1));
    assertTrue(this.p1.canPlaceCard(this.c2));
  }

  @Test
  public void testPawnsAddPawns() {
    this.p1.addPawns(Player.Red);
    assertEquals(Player.Red, this.p1.getOwner());
    assertEquals(2, this.p1.getCount());
    this.p2.addPawns(Player.Red);
    assertEquals(Player.Red, this.p2.getOwner());
    assertEquals(2, this.p2.getCount());
  }

  @Test
  public void testPawnsEquals() {
    Pawns p11 = new Pawns(Player.Red, 1);
    Pawns p22 = new Pawns(Player.Blue, 2);
    assertTrue(p11.equals(this.p1));
    assertFalse(p22.equals(this.p1));
    assertTrue(p22.equals(this.p2));
    assertTrue(p11.hashCode() == this.p1.hashCode());
    assertTrue(p22.hashCode() == this.p2.hashCode());
    assertFalse(p11.hashCode() == this.p2.hashCode());
  }

  @Test
  public void testDeckReader() throws IOException {
    String path = "docs" + File.separator + "redDeck.config";
    List<Card> expected = DeckReader.readDeck(path, Player.Red);
    assertEquals(20, expected.size());

    String path1 = "docs" + File.separator + "invalidDeck.config";
    assertThrows(IllegalStateException.class, () -> {
      DeckReader.readDeck(path1, Player.Red);
    } );
  }

  @Test
  public void testCardConstructor() {
    String[] influence1 = { "XXXXX", "XXIXX", "XICIX", "XXIXX", "XXXXX" };
    assertThrows(IllegalArgumentException.class, () -> {
      new Card(null, 1, 1, influence1, Player.Red);
    } );
    assertThrows(IllegalArgumentException.class, () -> {
      new Card("null", 4, 1, influence1, Player.Red);
    } );
    assertThrows(IllegalArgumentException.class, () -> {
      new Card("null", 1, 0, influence1, Player.Red);
    } );
    assertThrows(IllegalArgumentException.class, () -> {
      new Card("null", 1, 1, null, Player.Red);
    } );

    assertEquals(2, this.c1.getCost());
    assertEquals(1, this.c1.getValue());
    assertEquals(influence1, this.c1.getInfluence());
    assertEquals("Security", this.c1.getName());
  }

  @Test
  public void testCardGetName() {
    assertEquals("Security", this.c1.getName());
    assertEquals("Lobber", this.c2.getName());
  }

  @Test
  public void testCardOwner() {
    c1.setOwner(Player.Red);
    assertEquals(Player.Red, this.c1.getOwner());
    c2.setOwner(Player.Blue);
    assertEquals(Player.Blue, this.c2.getOwner());
  }

  @Test
  public void testCardCost() {
    assertEquals(2, this.c1.getCost());
    assertEquals(1, this.c2.getCost());
  }

  @Test
  public void testCardValue() {
    assertEquals(1, this.c1.getValue());
    assertEquals(2, this.c2.getValue());
  }

  @Test
  public void testCardInfluence() {
    String[] influence1 = { "XXXXX", "XXIXX", "XICIX", "XXIXX", "XXXXX" };
    String[] influence2 = { "XXXXX", "XXXXX", "XXCXI", "XXXXX", "XXXXX" };
    assertEquals(influence1, this.c1.getInfluence());
    assertEquals(influence2, this.c2.getInfluence());
  }

  @Test
  public void testCardToString() {
    assertEquals("Security 2 1", this.c1.toString());
    assertEquals("Lobber 1 2", this.c2.toString());
  }

  @Test
  public void testCardEquals() {
    String[] influence1 = { "XXXXX", "XXIXX", "XICIX", "XXIXX", "XXXXX" };
    String[] influence2 = { "XXXXX", "XXXXX", "XXCXI", "XXXXX", "XXXXX" };
    Card c11 = new Card("Security", 2, 1, influence1,Player.Red);
    Card c22 = new Card("Lobber", 1, 2, influence2, Player.Red);
    assertTrue(this.c1.equals(c11) && this.c2.equals(c22));
    assertFalse(this.c1.equals(c22));
    assertTrue(this.c1.equals(c11));
    assertTrue(this.c1.hashCode() == c11.hashCode());
  }

  @Test
  public void testBoardConstructor() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Board(0, 0);
    } );
    assertThrows(IllegalArgumentException.class, () -> {
      new Board(2, 2);
    } );
    assertEquals(3, this.board.getRow());
    assertEquals(5, this.board.getColumn());
    assertEquals(Player.Red, this.board.getTurn());
    Pawns[][] p1 = this.board.getBoard();
    assertEquals(1, p1[0][0].getCount());
    assertEquals(Player.Red, p1[0][0].getOwner());
    assertEquals(1, p1[0][4].getCount());
    assertEquals(Player.Blue, p1[0][4].getOwner());
  }

  @Test
  public void testBoardStartGame() throws IOException {
    assertThrows(IllegalStateException.class, () -> {
      this.board.startGame(this.redDeck, this.blueDeck, false, 1);
    } );
    assertThrows(IllegalArgumentException.class, () -> {
      this.invalidBoard.startGame(this.redDeck,this.blueDeck, false, 10);
    } );
    Board b1 = new Board(100, 99);
    assertThrows(IllegalArgumentException.class, () -> {
      b1.startGame(this.redDeck,this.blueDeck, false, 5);
    } );

    String[] influence1 = { "XXXXX", "XXIXX", "XICIX", "XXIXX", "XXXXX" };
    Card c11 = new Card("Security", 1, 2, influence1, Player.Red);
    assertEquals(Player.Red, this.board.getTurn());
    assertEquals(c11, this.board.getHand().get(0));
    assertEquals(19, this.board.getDeck().size());
  }

  @Test
  public void testBoardGetRow() throws IOException {
    assertEquals(3, this.board.getRow());
    assertEquals(3, this.invalidBoard.getRow());
  }

  @Test
  public void testBoardGetCol() throws IOException {
    assertEquals(5, this.board.getColumn());
    assertEquals(5, this.invalidBoard.getColumn());
  }

  @Test
  public void testBoardGetTurn() throws IOException {
    assertEquals(Player.Red, this.board.getTurn());
    this.board.passTurn();
    assertEquals(Player.Blue, this.board.getTurn());
    this.board.placeCard(0, 0,4);
    assertEquals(Player.Red, this.board.getTurn());
    this.board.drawCard();
    assertEquals(Player.Red, this.board.getTurn());
  }

  @Test
  public void testBoardGetHand() throws IOException {
    List<Card> c1 = new ArrayList<>();
    // Red turn and its hand
    c1.add(this.redDeck.get(0));
    assertEquals(c1, this.board.getHand());
    assertEquals(this.board.getRedHand(), this.board.getHand());
    // Blue turn and its hand
    this.board.passTurn();
    assertEquals(c1, this.board.getHand());
    assertEquals(this.board.getBlueHand(), this.board.getHand());
    assertThrows(IllegalStateException.class, () -> {
      this.invalidBoard.getHand();
    } );
  }

  @Test
  public void testBoardGetDeck() throws IOException {
    assertThrows(IllegalStateException.class, () -> {
      this.invalidBoard.getDeck();
    } );
    // Red turn and its deck
    List<Card> expected1 = this.redDeck.subList(1, this.redDeck.size());
    assertEquals(expected1, this.board.getDeck());
    this.board.passTurn();
    // Blue turn and its deck
    List<Card> expected2 = this.blueDeck.subList(1, this.redDeck.size());
    assertEquals(expected2, this.board.getDeck());
    this.board.drawCard();
    List<Card> expected3 = this.blueDeck.subList(2, this.redDeck.size());
    assertEquals(expected3, this.board.getDeck());
  }

  @Test
  public void testBoardGetBoard() throws IOException {
    assertThrows(IllegalStateException.class, () -> {
      this.invalidBoard.getBoard();
    } );

    assertEquals(1, this.board.getBoard()[0][0].getCount());
    assertEquals(1, this.board.getBoard()[1][0].getCount());
    assertEquals(1, this.board.getBoard()[2][0].getCount());
    assertEquals(Player.Red, this.board.getBoard()[0][0].getOwner());
    assertEquals(Player.Red, this.board.getBoard()[1][0].getOwner());
    assertEquals(Player.Red, this.board.getBoard()[2][0].getOwner());
    assertEquals(1, this.board.getBoard()[0][4].getCount());
    assertEquals(1, this.board.getBoard()[1][4].getCount());
    assertEquals(1, this.board.getBoard()[2][4].getCount());
    assertEquals(Player.Blue, this.board.getBoard()[0][4].getOwner());
    assertEquals(Player.Blue, this.board.getBoard()[1][4].getOwner());
    assertEquals(Player.Blue, this.board.getBoard()[2][4].getOwner());
  }

  @Test
  public void testBoardGetPlacedCard() throws IOException {
    assertThrows(IllegalStateException.class, () -> {
      this.invalidBoard.getPlacedCards();
    } );
    this.board.placeCard(0, 0, 0);
    assertEquals(Player.Red, this.board.getPlacedCards()[0][0].getOwner());
    assertEquals("Security", this.board.getPlacedCards()[0][0].getName());
    assertEquals(1, this.board.getPlacedCards()[0][0].getCost());
    assertEquals(2, this.board.getPlacedCards()[0][0].getValue());
    String[] influence1 = { "XXXXX", "XXIXX", "XICIX", "XXIXX", "XXXXX" };
    assertEquals(influence1, this.board.getPlacedCards()[0][0].getInfluence());
  }

  @Test
  public void testPlaceCard() throws IOException {
    assertThrows(IllegalStateException.class, () -> {
      this.invalidBoard.placeCard(1, 0, 0);
    } );
    assertThrows(IllegalArgumentException.class, () -> {
      this.board.placeCard(3, 0, 0);
    } );
    assertThrows(IllegalArgumentException.class, () -> {
      this.board.placeCard(0, 5, 0);
    } );
    assertThrows(IllegalArgumentException.class, () -> {
      this.board.placeCard(0, 0, 10);
    } );

    Pawns[][] pBoard = this.board.getBoard();
    this.board.placeCard(0,0,0);
    assertEquals(2, pBoard[1][0].getCount());
    assertEquals(1, pBoard[0][1].getCount());
    assertEquals(Player.Red, pBoard[1][0].getOwner());
    assertEquals(Player.Red, pBoard[0][1].getOwner());

    this.board.placeCard(0,0,4);
    assertEquals(2, pBoard[1][4].getCount());
    assertEquals(1, pBoard[0][3].getCount());
    assertEquals(Player.Blue, pBoard[1][4].getOwner());
    assertEquals(Player.Blue, pBoard[0][3].getOwner());

    assertThrows(IllegalStateException.class, () -> {
      this.board.placeCard(0, 0, 0);
    } );

    String[] influence1 = { "XXXXX", "XXIXX", "XICIX", "XXIXX", "XXXXX" };
    Card c1 = new Card("abc", 3, 1, influence1, Player.Red);
    List<Card> deck1 = new ArrayList<>();
    deck1.add(c1);
    deck1.add(c1);
    deck1.add(this.redDeck.get(0));
    deck1.add(this.redDeck.get(0));
    deck1.add(this.redDeck.get(1));
    deck1.add(this.redDeck.get(1));
    deck1.add(this.redDeck.get(2));
    deck1.add(this.redDeck.get(2));
    deck1.add(this.redDeck.get(3));
    deck1.add(this.redDeck.get(3));
    Board b1 = new Board(3, 3);
    b1.startGame(deck1, deck1,false,1);
    assertThrows(IllegalStateException.class, () -> {
      b1.placeCard(0, 0, 0);
    } );
  }

  @Test
  public void testDrawCard() throws IOException {
    assertThrows(IllegalStateException.class, () -> {
      this.invalidBoard.drawCard();
    } );

    this.board.drawCard();
    assertEquals(2, this.board.getHand().size());

    for (int i = 0; i < 18; i++) {
      this.board.drawCard();
    }
    assertThrows(IllegalStateException.class, () -> {
      this.board.drawCard();
    } );
  }

  @Test
  public void testBoardPass() throws IOException {
    assertThrows(IllegalStateException.class, () -> {
      this.invalidBoard.passTurn();
    } );
    this.board.passTurn();
    assertEquals(1, this.board.getPassCount());
    assertEquals(Player.Blue, this.board.getTurn());
    this.board.passTurn();
    assertEquals(2, this.board.getPassCount());
    this.board.placeCard(0, 0, 0);
    assertEquals(0, this.board.getPassCount());
  }

  @Test
  public void testGetScore() {
    assertThrows(IllegalStateException.class, () -> {
      this.invalidBoard.getScore();
    } );
    assertEquals("[[0, 0], [0, 0], [0, 0]]", Arrays.deepToString(board.getScore()));
    board.placeCard(0, 0, 0);
    board.drawCard();
    assertEquals("[[2, 0], [0, 0], [0, 0]]", Arrays.deepToString(board.getScore()));
    board.placeCard(0, 1, 4);
    board.drawCard();
    assertEquals("[[2, 0], [0, 2], [0, 0]]", Arrays.deepToString(board.getScore()));
  }

  @Test
  public void testTie() {
    assertThrows(IllegalStateException.class, () -> {
      this.invalidBoard.getWinner();
    } );
    board.passTurn();
    board.passTurn();
    assertNull(board.getWinner());
  }

  @Test
  public void testRedWin() {
    assertThrows(IllegalStateException.class, () -> {
      this.invalidBoard.getWinner();
    } );
    board.placeCard(0, 0, 0);
    board.passTurn();
    board.passTurn();
    assertEquals(Player.Red, board.getWinner());
  }

  @Test
  public void testBlueWin() {
    assertThrows(IllegalStateException.class, () -> {
      this.invalidBoard.getWinner();
    } );
    board.passTurn();
    board.placeCard(0, 1, 4);
    board.passTurn();
    board.passTurn();
    assertEquals(Player.Blue, board.getWinner());
  }

  @Test
  public void testBoardIsGameOver() {
    assertThrows(IllegalStateException.class, () -> {
      this.invalidBoard.isGameOver();
    } );

    this.board.passTurn();
    this.board.passTurn();
    assertTrue(this.board.isGameOver());
  }

  @Test
  public void testBoardEquals() throws IOException {
    assertTrue(this.board.equals(this.invalidBoard));
    Board b1 = new Board(3, 3);
    assertFalse(this.board.equals(b1));
    assertTrue(this.board.hashCode() == this.invalidBoard.hashCode());
    assertFalse(this.board.hashCode() == b1.hashCode());
  }

  @Test
  public void testInitialBoard() {
    assertEquals(
            "0 1___1 0\n" +
                    "0 1___1 0\n" +
                    "0 1___1 0\n" +
                    "Red: [Security 1 2]\n" +
                    "Blue: [Security 1 2]", view.toString());
  }

  @Test
  public void playWholeGame() {
    board.drawCard();
    board.placeCard(0, 0, 0);
    board.drawCard();
    assertEquals(
            "2 R1__1 0\n" +
                    "0 2___1 0\n" +
                    "0 1___1 0\n" +
                    "Red: [Security 1 2]\n" +
                    "Blue: [Security 1 2, Security 1 2]", view.toString());
    board.placeCard(0, 1, 4);
    board.drawCard();
    assertEquals(
            "2 R1__2 0\n" +
                    "0 2__1B 2\n" +
                    "0 1___2 0\n" +
                    "Red: [Security 1 2, Lobber 2 1]\n" +
                    "Blue: [Security 1 2]", view.toString());
    board.placeCard(0, 1, 0);
    board.drawCard();
    assertEquals(
            "2 R1__2 0\n" +
                    "2 R1_1B 2\n" +
                    "0 2___2 0\n" +
                    "Red: [Lobber 2 1]\n" +
                    "Blue: [Security 1 2, Lobber 2 1]", view.toString());
    board.placeCard(0, 1, 3);
    board.drawCard();
    assertEquals(
            "2 R1_12 0\n" +
                    "2 R11BB 4\n" +
                    "0 2__12 0\n" +
                    "Red: [Lobber 2 1, Lobber 2 1]\n" +
                    "Blue: [Lobber 2 1]", view.toString());
    board.placeCard(0, 2, 0);
    board.drawCard();
    assertEquals(
            "2 R1_12 0\n" +
                    "2 R11BB 4\n" +
                    "1 R_112 0\n" +
                    "Red: [Lobber 2 1]\n" +
                    "Blue: [Lobber 2 1, Lobber 2 1]", view.toString());
    board.placeCard(1, 0, 4);
    board.drawCard();
    assertEquals(
            "2 R111B 1\n" +
                    "2 R11BB 4\n" +
                    "1 R_112 0\n" +
                    "Red: [Lobber 2 1, Mandragora 1 2]\n" +
                    "Blue: [Lobber 2 1]", view.toString());
    board.placeCard(1, 1, 1);
    board.drawCard();
    assertEquals(
            "2 R211B 1\n" +
                    "4 RR1BB 4\n" +
                    "1 R_112 0\n" +
                    "Red: [Lobber 2 1]\n" +
                    "Blue: [Lobber 2 1, Mandragora 1 2]", view.toString());
    board.placeCard(0, 2, 4);
    board.drawCard();
    assertEquals(
            "2 R211B 1\n" +
                    "4 RR1BB 4\n" +
                    "1 R_11B 1\n" +
                    "Red: [Lobber 2 1, Mandragora 1 2]\n" +
                    "Blue: [Mandragora 1 2]", view.toString());
    board.placeCard(0, 0, 1);
    board.drawCard();
    assertEquals(
            "3 RR11B 1\n" +
                    "4 RR1BB 4\n" +
                    "1 R_11B 1\n" +
                    "Red: [Mandragora 1 2]\n" +
                    "Blue: [Mandragora 1 2, Mandragora 1 2]", view.toString());
    board.placeCard(0, 0, 2);
    board.drawCard();
    assertEquals(
            "3 RRB1B 3\n" +
                    "4 RR1BB 4\n" +
                    "1 R_11B 1\n" +
                    "Red: [Mandragora 1 2, Star 1 2]\n" +
                    "Blue: [Mandragora 1 2]", view.toString());
    board.placeCard(0, 0, 3);
    board.drawCard();
    assertEquals(
            "5 RRBRB 3\n" +
                    "4 RR1BB 4\n" +
                    "1 R_11B 1\n" +
                    "Red: [Star 1 2]\n" +
                    "Blue: [Mandragora 1 2, Star 1 2]", view.toString());
    board.placeCard(0, 2, 2);
    board.drawCard();
    assertEquals(
            "5 RRBRB 3\n" +
                    "4 RR1BB 4\n" +
                    "1 R1B1B 3\n" +
                    "Red: [Star 1 2, Star 1 2]\n" +
                    "Blue: [Star 1 2]", view.toString());
    board.passTurn();
    board.drawCard();
    assertEquals(
            "5 RRBRB 3\n" +
                    "4 RR1BB 4\n" +
                    "1 R1B1B 3\n" +
                    "Red: [Star 1 2, Star 1 2]\n" +
                    "Blue: [Star 1 2, Star 1 2]", view.toString());

    board.placeCard(0, 1, 2);
    board.drawCard();
    assertEquals(
            "5 RRBRB 3\n" +
                    "4 RRBBB 6\n" +
                    "1 R2B2B 3\n" +
                    "Red: [Star 1 2, Star 1 2, Super 2 3]\n" +
                    "Blue: [Star 1 2]", view.toString());

    board.passTurn();
    board.placeCard(0, 2, 3);
    board.drawCard();
    assertEquals(
            "5 RRBRB 3\n" +
                    "4 RRBBB 6\n" +
                    "1 R2BBB 5\n" +
                    "Red: [Star 1 2, Star 1 2, Super 2 3, Super 2 3]\n" +
                    "Blue: []", view.toString());
    board.passTurn();
    board.drawCard();
    board.placeCard(0,2,1);
    assertEquals(
            "5 RRBRB 3\n" +
                    "4 RRBBB 6\n" +
                    "1 RBBBB 8\n" +
                    "Red: [Star 1 2, Star 1 2, Super 2 3, Super 2 3]\n" +
                    "Blue: []", view.toString());

    assertTrue(this.board.isGameOver());
    assertEquals(Player.Blue, this.board.getWinner());
  }
}
