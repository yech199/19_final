package Controller;

import Model.ChanceCards.ChanceCard;
import Model.ChanceCards.MovementCard;
import Model.ChanceCards.ReceiveMoneyCard;
import Model.ChanceCards.ReleaseFromPrisonCard;
import Model.Die;
import Model.Fields.ChanceField;
import Model.Fields.Field;
import Model.Fields.JailField;
import Model.Fields.StartField;
import Model.GameBoard;
import Model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import stub.StubDie;
import stub.StubGUIController;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
        gameController = new GameController(guiController, gameBoard, die, die);
        gameController.playerList = players;
    }

    @Test
    public void testPlayerGetsMoneyWhenPassingStart() {
        Player player = players[0];
        int[] rolls = {1, 1};
        die = new StubDie(rolls);
        gameController.die1 = die;
        gameController.die2 = die;
        player.setCurrentPos(gameController.gameBoard.fields.length - 2);

        int moneyBefore = player.getBalance();

        gameController.playTurn(player);

        assertEquals(moneyBefore + 4000, player.getBalance());
    }

    @Test
    public void testPlayerContinuouslyDrawsChanceCards() {
        ChanceCard[] chanceCards = new ChanceCard[]{
                new MovementCard("", "", 1, MovementCard.MovementType.NUMBER),
                new MovementCard("", "", 1, MovementCard.MovementType.NUMBER)
        };

        Field[] fields = new Field[]{
                new StartField("", "", "", Color.BLACK),
                new ChanceField(),
                new ChanceField(),
        };

        gameBoard = new GameBoard(fields, chanceCards);
        Player player = players[0];
        int[] rolls = {1, 0};
        die = new StubDie(rolls);
        gameController = new GameController(guiController, gameBoard, die, die);

        gameController.playTurn(player);

        assertEquals(0, player.getCurrentPos());
        // Tjekker at spilleren rent faktisk har rykket sig
        assertNotEquals(0, player.getPreviousPos());
    }

    @Test
    public void testPlayerDoesNotDrawTwiceOnTheSameChanceField() {
        int money = 1000;
        ChanceCard[] chanceCards = new ChanceCard[]{
                new ReceiveMoneyCard("", "", money, ReceiveMoneyCard.ReceivingFrom.BANK),
        };

        Field[] fields = new Field[]{
                new StartField("", "", "", Color.BLACK),
                new ChanceField(),
        };

        gameBoard = new GameBoard(fields, chanceCards);
        Player player = players[0];
        int[] rolls = {1, 0};
        die = new StubDie(rolls);
        gameController = new GameController(guiController, gameBoard, die, die);

        int tmpBalance = player.getBalance();
        gameController.playTurn(player);

        // Tjekker at spilleren kun får pengene én gang
        assertEquals(tmpBalance + money, player.getBalance());

        // Tjekker at spilleren rent faktisk har rykket sig
        assertEquals(1, player.getCurrentPos());
        assertEquals(0, player.getPreviousPos());
    }

    @Test
    public void testPlayerGetsReleasedFromPrisonWhenHavingCardAndDoesNotLoseMoney() {
        int money = 1000;
        ChanceCard[] chanceCards = new ChanceCard[]{
                new ReleaseFromPrisonCard("", "")
        };

        Field[] fields = new Field[]{
                new StartField("", "", "", Color.BLACK),
                new ChanceField(),
                new JailField("", "", ""),
        };

        gameBoard = new GameBoard(fields, chanceCards);
        Player player = players[0];
        int[] rolls = {1, 0, 0, 0};
        die = new StubDie(rolls);
        gameController = new GameController(guiController, gameBoard, die, die);
        player.setBalance(money);

        gameController.playTurn(player);
        player.inJail = true;
        gameController.playTurn(player);

        Assert.assertFalse(player.inJail);
        Assert.assertEquals(money, player.getBalance());
    }

    @Test
    public void correctWinnerIsDecided() {
        Player[] playerList = new Player[6];
        this.guiController.setUpPlayers(playerList);
        for (int i = 0; i < playerList.length; i++) {
            playerList[i] = new Player("" + i);
        }
        int counter = 0;
        for (Player player : playerList){
            player.addAmountToBalance(10+counter);
            counter++;
        }
        assertEquals(gameController.getWinner(playerList), playerList[playerList.length-1]);
    }
}