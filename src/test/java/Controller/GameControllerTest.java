package Controller;

import Model.Die;
import Model.GameBoard;
import Model.Player;
import org.junit.Before;
import org.junit.Test;
import stub.StubDie;
import stub.StubGUIController;

import static org.junit.Assert.assertEquals;

public class GameControllerTest {
    private GameController gameController;
    private StubGUIController guiController;
    private GameBoard gameBoard;
    private Die die;
    private Player[] players;

    @Before
    public void setUp() {
        players = new Player[]{
                new Player("Test 1"),
                new Player("Test 2")
        };
        gameBoard = new GameBoard();
        guiController = new StubGUIController();
        gameController = new GameController(guiController, gameBoard, die);
        gameController.playerList = players;
    }

    @Test
    public void playerGetsMoneyWhenPassingStart() {
        Player player = players[0];
        int[] rolls = {1};
        die = new StubDie(rolls);
        gameController.die = die;
        player.setCurrentPos(gameController.gameBoard.fields.length - 1);

        int moneyBefore = player.getBalance();

        gameController.playTurn(player);

        assertEquals(moneyBefore + 2, player.getBalance());
    }
}