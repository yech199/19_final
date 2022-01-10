package Controller;

import Model.ChanceCards.ChanceCard;
import Model.ChanceCards.MovementCard;
import Model.ChanceCards.ReceiveMoneyCard;
import Model.ChanceCards.ReleaseFromPrisonCard;
import Model.Die;
import Model.Fields.*;
import Model.GameBoard;
import Model.GlobalValues;
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
                new Player("Test 2"),
                new Player("Test 3")
        };
        gameBoard = new GameBoard();
        guiController = new StubGUIController();
        guiController.customChoice = null;
        gameController = new GameController(guiController, gameBoard, die, die, players);
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
        gameController = new GameController(guiController, gameBoard, die, die, players);

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
        gameController = new GameController(guiController, gameBoard, die, die, players);

        int tmpBalance = player.getBalance();
        gameController.playTurn(player);

        // Tjekker at spilleren kun får pengene én gang
        assertEquals(tmpBalance + money, player.getBalance());

        // Tjekker at spilleren rent faktisk har rykket sig
        assertEquals(1, player.getCurrentPos());
        assertEquals(0, player.getPreviousPos());
    }

    @Test
    public void testReceiveMoneyWhenPassingStartUsingChanceCard() {
        ChanceCard[] chanceCards = new ChanceCard[]{
                new MovementCard("", "", 2, MovementCard.MovementType.NUMBER),
                new MovementCard("", "", 1, MovementCard.MovementType.INDEX),
                new MovementCard("", "", -1, MovementCard.MovementType.NEAREST)
        };

        Field[] fields = new Field[]{
                new StartField("", "", "", Color.BLACK),
                new ShippingField(0),
                new ChanceField(),
        };

        for (ChanceCard chanceCard : chanceCards) {
            gameBoard = new GameBoard(fields, new ChanceCard[]{chanceCard});
            Player player = new Player("");
            int[] rolls = {1, 1};
            die = new StubDie(rolls);
            gameController = new GameController(guiController, gameBoard, die, die, players);

            int tmpBalance = player.getBalance();
            gameController.playTurn(player);
            ((OwnableField) fields[1]).owner = null;

            // Tjekker at spilleren kun får pengene én gang
            assertEquals(tmpBalance + GlobalValues.START_FIELD_VALUE, player.getBalance());

            // Tjekker at spilleren rent faktisk har rykket sig
            assertEquals(2, player.getPreviousPos());
            assertEquals(1, player.getCurrentPos());
        }
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
        gameController = new GameController(guiController, gameBoard, die, die, players);
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
        for (Player player : playerList) {
            player.addAmountToBalance(counter);
            counter++;
        }
        assertEquals(gameController.getWinner(playerList), playerList[playerList.length - 1]);
    }

    @Test
    public void testOwnsAllReturnCorrect() {
        ChanceCard[] chanceCards = new ChanceCard[]{
        };

        PropertyField[] fields = new PropertyField[]{
                new PropertyField("Rødovrevej", "Pris: kr. 1200", "Rødovrevej", 50, 1200,
                        new Color(0, 0, 102), new Color(255, 255, 255), 1000, new int[]{250, 750, 2250, 4000, 6000}),
                new PropertyField("Hvidovrevej", "Pris: kr. 1200", "Hvidovrevej", 50, 1200,
                        new Color(0, 0, 102), new Color(255, 255, 255), 1000, new int[]{250, 750, 2250, 4000, 6000}),
                new PropertyField("Roskildevej", "Pris: kr. 2000", "Roskildevej", 100, 2000,
                        new Color(255, 128, 0), new Color(0, 0, 0), 1000, new int[]{600, 1800, 5400, 8000, 11000}),
                new PropertyField("Valby\nLanggade", "Pris: kr. 2000", "Valby Langgade", 100, 2000,
                        new Color(255, 128, 0), new Color(0, 0, 0), 1000, new int[]{600, 1800, 5400, 8000, 11000}),
                new PropertyField("Allégade", "Pris: kr. 2400", "Allégade", 150, 2400,
                        new Color(255, 128, 0), new Color(0, 0, 0), 1000, new int[]{800, 2000, 6000, 9000, 12000})
        };

        gameBoard = new GameBoard(fields, chanceCards);
        Player player = players[0];
        int[] rolls = {};
        die = new StubDie(rolls);
        gameController = new GameController(guiController, gameBoard, die, die);

        Assert.assertFalse(gameController.ownsAll(fields[0]));
        Assert.assertFalse(gameController.ownsAll(fields[2]));

        fields[0].owner = player;
        fields[2].owner = player;

        Assert.assertFalse(gameController.ownsAll(fields[1]));
        Assert.assertFalse(gameController.ownsAll(fields[3]));

        fields[1].owner = player;
        fields[3].owner = player;
        fields[4].owner = player;

        Assert.assertTrue(gameController.ownsAll(fields[0]));
        Assert.assertTrue(gameController.ownsAll(fields[4]));
    }

    @Test
    public void testPlayerGetsOutOfJailWhenRollingAPair() {
        guiController.customChoice = "Rul 2 ens";
        gameBoard = new GameBoard(
                new Field[]{
                    new StartField("", "", "", Color.RED),
                    new FreeParkingField("", "", ""),
                    new JailField("", "", "")
                },
                new ChanceCard[]{}
        );
        Player player1 = new Player("1");
        Player player2 = new Player("2");
        player1.setCurrentPos(2);
        player2.setCurrentPos(2);
        player1.inJail = true;
        player2.inJail = true;
        int[] rolls = {1, 2, 3, 4, 0, 1};
        die = new StubDie(rolls);
        gameController = new GameController(guiController, gameBoard, die, die, players);
        gameController.playTurn(player1);

        Assert.assertTrue(player1.inJail);
        Assert.assertEquals(2, player1.getCurrentPos());

        die = new StubDie(new int[]{1, 2, 3, 4, 1, 1});
        gameController.die1 = die;
        gameController.die2 = die;
        gameController.playTurn(player2);

        Assert.assertFalse(player2.inJail);
        Assert.assertEquals(1, player2.getCurrentPos());
    }

    @Test
    public void testPlayerLosesMoneyWhenPayingToExitJail() {
        guiController.customChoice = "Betal \" + GlobalValues.JAIL_PRICE + \" kr";
        gameBoard = new GameBoard(
                new Field[]{
                    new StartField("", "", "", Color.RED),
                    new FreeParkingField("", "", ""),
                    new JailField("", "", "")
                },
                new ChanceCard[]{}
        );
        int balance = 10000;
        Player player1 = new Player("1", balance);
        player1.setCurrentPos(2);
        player1.inJail = true;
        die = new StubDie(0);
        gameController = new GameController(guiController, gameBoard, die, die, players);
        gameController.playTurn(player1);

        Assert.assertFalse(player1.inJail);
        Assert.assertEquals(2, player1.getCurrentPos());
        Assert.assertEquals(balance - GlobalValues.JAIL_PRICE, player1.getBalance());
    }

    @Test
    public void testGameEndsWhenAllPlayersExceptForOneIsBankrupt() {
        gameBoard = new GameBoard(new Field[]{new StartField("", "", "", Color.RED)}, new ChanceCard[]{});
        Player player1 = new Player("1", -1000);
        Player player2 = new Player("2", -1000);
        Player player3 = new Player("3", 1000);
        // De første 3 rul er de rul der tjekker hvem der starter vha. metoden decideStartingOrder().
        // Derefter spilles der er runder for hver spiller i spillerlisten.
        int[] rolls = {0, 3, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0};
        die = new StubDie(rolls);
        gameController = new GameController(guiController, gameBoard, die, die, new Player[]{player1, player2, player3});
        gameController.runGame();

        Assert.assertTrue(gameController.gameEnded);
    }

    @Test
    public void testGameEndsAfter40Rounds() {
        gameBoard = new GameBoard(new Field[]{new StartField("", "", "", Color.RED)}, new ChanceCard[]{});
        Player player1 = new Player("1", 1000);
        Player player2 = new Player("2", 1000);
        Player player3 = new Player("3", 1000);
        die = new StubDie(0);
        gameController = new GameController(guiController, gameBoard, die, die, new Player[]{player1, player2, player3});

        gameController.action2 = "Hurtigt spil";
        gameController.choice = "Hurtigt spil";
        for (int i = 0; i < 41; i++) {
            gameController.playRound();
        }
        Assert.assertTrue(gameController.gameEnded);
    }

    @Test
    public void testchecksWhoWantsToTryBidding() {
        gameBoard = new GameBoard(new Field[]{
                new ShippingField(-1)},
                new ChanceCard[]{});
        Player player1 = new Player("1", 0);
        Player player2 = new Player("2", 0);
        Player player3 = new Player("3", 0);
        Player[] playerList = new Player[] {player1, player2, player3};
        die = new StubDie(0);

        gameController = new GameController(guiController, gameBoard, die, die, playerList);
        OwnableField ownableField = (OwnableField) gameBoard.fields[0];

        int numOfPlayersBidding = gameController.checksWhoWantsToTryBidding(player1, ownableField, playerList);

        Assert.assertEquals(2, numOfPlayersBidding);
        Assert.assertFalse(player1.wantToTryBidding);
        Assert.assertTrue(player2.wantToTryBidding);
        Assert.assertTrue(player3.wantToTryBidding);

        guiController.customChoice = "Nej";
        numOfPlayersBidding = gameController.checksWhoWantsToTryBidding(player2, ownableField, playerList);
        Assert.assertEquals(0, numOfPlayersBidding);
        Assert.assertFalse(player1.wantToTryBidding);
        Assert.assertFalse(player2.wantToTryBidding);
        Assert.assertFalse(player3.wantToTryBidding);
    }

    @Test
    public void testbidOnAuction() {
        gameBoard = new GameBoard(new Field[]{
                new ShippingField(1000)},
                new ChanceCard[]{});
        Player player1 = new Player("1", 1000);
        Player player2 = new Player("2", 3000);
        Player player3 = new Player("3", 5000);
        Player[] playerList = new Player[] {player1, player2, player3};

        die = new StubDie(0);
        gameController = new GameController(guiController, gameBoard, die, die, playerList);
        OwnableField ownableField = (OwnableField) gameBoard.fields[0];
        int numOfPlayersBidding = playerList.length;

        gameController.bidOnAuction(ownableField, numOfPlayersBidding);

        Assert.assertEquals(1000, player1.getBalance());
        Assert.assertEquals(3000, player2.getBalance());
        Assert.assertEquals(0, player3.getBalance());
        Assert.assertFalse(player1.wantToTryBidding);
        Assert.assertFalse(player2.wantToTryBidding);
        Assert.assertTrue(player3.wantToTryBidding);

        player1 = new Player("1", 1000);
        player2 = new Player("2", 7000);
        player3 = new Player("3", 5000);
        playerList = new Player[] {player1, player2, player3};

        gameController = new GameController(guiController, gameBoard, die, die, playerList);
        ownableField = (OwnableField) gameBoard.fields[0];
        gameController.bidOnAuction(ownableField, numOfPlayersBidding);

        Assert.assertEquals(1000, player1.getBalance());
        Assert.assertEquals(0, player2.getBalance());
        Assert.assertEquals(5000, player3.getBalance());
        Assert.assertFalse(player1.wantToTryBidding);
        Assert.assertTrue(player2.wantToTryBidding);
        Assert.assertFalse(player3.wantToTryBidding);
    }
}