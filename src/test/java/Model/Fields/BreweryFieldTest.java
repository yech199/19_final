package Model.Fields;

import Controller.GameController;
import Model.Die;
import Model.GameBoard;
import Model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import stub.StubDie;
import stub.StubGUIController;

public class BreweryFieldTest {
    private GameBoard gameBoard;
    private Player[] players;
    private StubGUIController guiController;
    private GameController gameController;
    private Field[] fields;

    @Before
    public void setup() {
        players = new Player[]{
                new Player("Test 1", 500),
                new Player("Test 2", 500)
        };
        fields = new Field[]{
                new BreweryField("Tuborg", "", ""),
                new BreweryField("Carlsberg", "", "")
        };

        guiController = new StubGUIController();
        gameBoard = new GameBoard(fields, null);
        Die die = new StubDie(0);
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
        Assert.assertEquals(balanceBefore, 500);
        Assert.assertEquals(balanceAfter, 400);
    }

    @Test
    public void testCheckInstanceOfBothBreweryFieldsOwned(){
        Player player1 = players[0];
        Player player2 = players[1];
        BreweryField breweryField1 = (BreweryField) fields[0];
        BreweryField breweryField2 = (BreweryField) fields[1];
        breweryField1.owner = player2;
        breweryField2.owner = player2;
        int balanceBefore = player1.getBalance();
        gameController.checkIfInstanceOf(players[0], 1, fields[0]);
        int balanceafter = player1.getBalance();

        Assert.assertTrue(balanceBefore > balanceafter);
        Assert.assertEquals(balanceBefore, 500);
        Assert.assertEquals(balanceafter, 300);



    }
}