package Model.Fields;

import Controller.GameController;
import Model.Die;
import Model.GameBoard;
import Model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import stub.StubDie;
import stub.StubView;

public class BreweryFieldTest {
    private GameBoard gameBoard;
    private Player[] players;
    private StubView guiController;
    private GameController gameController;
    private Field[] fields;

    @Before
    public void setup() {
        players = new Player[]{
                new Player("Test 1", 50000),
                new Player("Test 2", 50000)
        };
        fields = new Field[]{
                new BreweryField("Tuborg", "", ""),
                new BreweryField("Carlsberg", "", "")
        };

        guiController = new StubView();
        gameBoard = new GameBoard(fields, null);
        Die die = new StubDie(0);
        gameController = new GameController(guiController, gameBoard, die, die);
    }

    @Test
    public void testCheckInstanceOfBothBreweryFieldsOwned(){
        Player player1 = players[0];
        Player player2 = players[1];
        BreweryField breweryField1 = (BreweryField) fields[0];
        BreweryField breweryField2 = (BreweryField) fields[1];
        int faceValue = 1;
        gameController.updateOwnableFields(player1, faceValue, breweryField1);
        gameController.updateOwnableFields(player2, faceValue, breweryField1);

        Assert.assertEquals(faceValue * 100, breweryField1.rent);

        gameController.updateOwnableFields(player1, faceValue, breweryField2);
        gameController.updateOwnableFields(player2, faceValue, breweryField2);

        Assert.assertEquals(faceValue * 200, breweryField2.rent);
    }
}