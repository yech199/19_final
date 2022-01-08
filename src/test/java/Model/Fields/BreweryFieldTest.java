package Model.Fields;

import Controller.GameController;
import Model.Die;
import Model.GameBoard;
import Model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import stub.StubGUIController;

public class BreweryFieldTest {
    private GameBoard gameBoard;
    private Player[] players;
    private Die die;
    private StubGUIController guiController;
    private GameController gameController;
    private Field[] fields;

    @Before
    public void setup() {
        players = new Player[]{
                new Player("Test 1", 0),
                new Player("Test 2", 0)
        };
        fields = new Field[]{
                new BreweryField("", "", ""),
        };

        guiController = new StubGUIController();
        gameBoard = new GameBoard(fields, null);
        gameController = new GameController(guiController, gameBoard, die, die);
    }

    @Test
    public void testCheckInstanceOf() {
        Player player1 = players[0];
        Player player2 = players[1];
        BreweryField breweryField = (BreweryField) fields[0];
        breweryField.owner = player2;
        int balanceBefore = player1.getBalance();
        gameController.checkIfInstanceOf(players[0], 1, fields[0]);
        int balanceAfter = player1.getBalance();

        Assert.assertTrue(balanceBefore > balanceAfter);
        Assert.assertEquals(balanceBefore, 0);
        Assert.assertEquals(balanceAfter, -100);
    }

    @Test
    public void fieldAction() {
    }

    @Test
    public void getGUIversion() {
    }

    @Test
    public void setFaceValue() {
    }
}