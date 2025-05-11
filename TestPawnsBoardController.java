package cs3500.pawnsboard;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs3500.pawnsboard.controller.PawnsBoardVisualController;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.MockPawnsBoard;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.player.HumanPlayer;
import cs3500.pawnsboard.player.MachinePlayer;
import cs3500.pawnsboard.strategy.FillFirst;
import cs3500.pawnsboard.strategy.Strategy;
import cs3500.pawnsboard.view.MockPawnsFrame;
import static org.junit.Assert.assertEquals;

/**
 * Test the controller functions with mocks of model and view.
 * To make sure the controller can work correctly.
 */
public class TestPawnsBoardController {
  MockPawnsBoard model;
  List<Card> redDeck = new ArrayList<>();
  List<Card> blueDeck = new ArrayList<>();
  StringBuilder log;
  Strategy strategy;

  @Before
  public void setUp() {
    this.strategy = new FillFirst();
    String[] influence = {"XXXXX", "XXIXX", "XICIX", "XXIXX", "XXXXX"};

    this.redDeck = List.of(
            new Card("A1", 1, 5, influence, Player.Red),
            new Card("A2", 1, 2, influence, Player.Red),
            new Card("A3", 2, 3, influence, Player.Red),
            new Card("A1", 1, 5, influence, Player.Red),
            new Card("A2", 1, 2, influence, Player.Red),
            new Card("A3", 2, 3, influence, Player.Red),
            new Card("A11", 1, 5, influence, Player.Red),
            new Card("A22", 1, 2, influence, Player.Red),
            new Card("A33", 2, 3, influence, Player.Red)
    );

    this.blueDeck = List.of(
            new Card("B1", 1, 4, influence, Player.Blue),
            new Card("B2", 1, 2, influence, Player.Blue),
            new Card("B3", 2, 3, influence, Player.Blue),
            new Card("B1", 1, 4, influence, Player.Blue),
            new Card("B2", 1, 2, influence, Player.Blue),
            new Card("B3", 2, 3, influence, Player.Blue),
            new Card("B11", 1, 4, influence, Player.Blue),
            new Card("B22", 1, 2, influence, Player.Blue),
            new Card("B33", 2, 3, influence, Player.Blue)
    );
  }

  @Test
  public void testSimpleGame() throws IOException {
    this.log = new StringBuilder();
    this.model = new MockPawnsBoard(log);
    MockPawnsFrame fakeView = new MockPawnsFrame(log,0, new int[]{1, 0}, Player.Red);
    HumanPlayer player = new HumanPlayer(model);
    PawnsBoardVisualController controller = new PawnsBoardVisualController(model, fakeView, player, false);
    assertEquals(Player.Red, model.getTurn());
    controller.onCardSelected(0);
    controller.onCellSelected(1, 0);
    controller.onConfirm();
    String expect = "placeCard(0, 1, 0)\n" +
            "drawCard";
    assertEquals(expect.trim(), log.toString().trim());

  }

  @Test
  public void testTwoHumanPlayer() throws IOException {
    this.log = new StringBuilder();
    this.model = new MockPawnsBoard(log);
    HumanPlayer player1 = new HumanPlayer(model);
    MockPawnsFrame mockView1 = new MockPawnsFrame(log,0, new int[]{0, 0}, Player.Red);
    HumanPlayer player2 = new HumanPlayer(model);
    MockPawnsFrame mockView2 = new MockPawnsFrame(log,0, new int[]{0, 2}, Player.Blue);
    PawnsBoardVisualController mockController1 = new PawnsBoardVisualController(
            model, mockView1, player1, false);
    mockController1.onCardSelected(0);
    mockController1.onCellSelected(0,0);
    mockController1.onConfirm();

    PawnsBoardVisualController mockController2 = new PawnsBoardVisualController(
            model, mockView2, player2, false);
    mockController2.onCardSelected(0);
    mockController2.onCellSelected(0,2);
    mockController2.onConfirm();
    String expect1 = "placeCard(0, 0, 0)\n" +
            "drawCard\n" +
            "placeCard(0, 0, 2)\n" +
            "drawCard";
    assertEquals(expect1.trim(), log.toString().trim());

    mockController1.onPass();
    mockController2.onPass();

    String expect2 = "placeCard(0, 0, 0)\n" +
            "drawCard\n" +
            "placeCard(0, 0, 2)\n" +
            "drawCard\n" +
            "passTurn\n" +
            "drawCard\n" +
            "passTurn\n" +
            "drawCard";
    assertEquals(expect2.trim(), log.toString().trim());
  }

  @Test
  public void testHumanAndMachine() {
    this.log = new StringBuilder();
    this.model = new MockPawnsBoard(log);
    HumanPlayer player1 = new HumanPlayer(model);
    MockPawnsFrame mockView1 = new MockPawnsFrame(log,0, new int[]{0, 0}, Player.Red);
    MachinePlayer player2 = new MachinePlayer(model);
    MockPawnsFrame mockView2 = new MockPawnsFrame(log,0, new int[]{0, 2}, Player.Blue);
    player2.addStrategy(strategy);
    PawnsBoardVisualController mockController1 = new PawnsBoardVisualController(
            model, mockView1, player1, false);
    PawnsBoardVisualController mockController2 = new PawnsBoardVisualController(
            model, mockView2, player2, false);

    player1.addView(mockView1);
    player2.addView(mockView2);
//    mockController1.setOpponent(mockController2);
//    mockController2.setOpponent(mockController1);

    mockController1.onCardSelected(0);
    mockController1.onCellSelected(0,0);
    mockController1.onConfirm();
    mockController2.playGame();

    // Consider it is a mock of the board, so strategy cannot work here,
    // in other words, the move of machine player is always invalid, so it can just pass turn.
    String expect = "placeCard(0, 0, 0)\n" +
            "drawCard\n" +
            "passTurn";
    assertEquals(expect.trim(), log.toString().trim());

    mockController1.onCardSelected(0);
    mockController1.onCellSelected(1,0);
    mockController1.onConfirm();
    mockController2.playGame();

    String expect1 = "placeCard(0, 0, 0)\n" +
            "drawCard\n" +
            "passTurn\n" +
            "placeCard(0, 1, 0)\n" +
            "drawCard\n" +
            "passTurn";
    assertEquals(expect1.trim(), log.toString().trim());

    mockController1.onPass();
    String expect2 = "placeCard(0, 0, 0)\n" +
            "drawCard\n" +
            "passTurn\n" +
            "placeCard(0, 1, 0)\n" +
            "drawCard\n" +
            "passTurn\n" +
            "passTurn\n" +
            "drawCard";
    assertEquals(expect2.trim(), log.toString().trim());



  }
}
