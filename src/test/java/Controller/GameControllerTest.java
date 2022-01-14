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
import stub.StubView;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GameControllerTest {
    private GameController gameController;
    private StubView UI;
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
        UI = new StubView();
        UI.customChoice = null;
        gameController = new GameController(UI, gameBoard, die, die, players);
    }

    @Test
    public void testPlayerGetsMoneyWhenPassingStart() {
        Player player = players[0];
        int[] rolls = {2, 0};
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
        gameController = new GameController(UI, gameBoard, die, die, players);

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
        gameController = new GameController(UI, gameBoard, die, die, players);

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
            int[] rolls = {2, 0};
            die = new StubDie(rolls);
            gameController = new GameController(UI, gameBoard, die, die, players);

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
        gameController = new GameController(UI, gameBoard, die, die, players);
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
        this.UI.setUpPlayers(playerList);
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
        ChanceCard[] chanceCards = new ChanceCard[]{};

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
        die = new StubDie(-1);
        gameController = new GameController(UI, gameBoard, die, die);

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
        UI.customChoice = "Rul 2 ens";
        gameBoard = new GameBoard(
                new Field[]{
                    new StartField("", "", "", Color.RED),
                    new FreeParkingField("", "", ""),
                    new JailField("", "", "")
                },
                new ChanceCard[]{}
        );
        Player player1 = new Player("1");
        int prevBalance = player1.getBalance();
        Player player2 = new Player("2");
        player1.setCurrentPos(2);
        player2.setCurrentPos(2);
        player1.inJail = true;
        player2.inJail = true;
        int[] rolls;
        for (int i = 0; i < 3; i++) {
            rolls = new int[]{1, 2, 3, 4, -1, 1};
            die = new StubDie(rolls);
            gameController = new GameController(UI, gameBoard, die, die, players);
            gameController.playTurn(player1);
            if (i == 2) {
                Assert.assertFalse(player1.inJail);
                Assert.assertEquals(2, player1.getCurrentPos());
                Assert.assertEquals(prevBalance - GlobalValues.JAIL_PRICE, player1.getBalance());
            }
            else {
                Assert.assertTrue(player1.inJail);
                Assert.assertEquals(2, player1.getCurrentPos());
            }
        }
        // Det sidste terningekast sørger for at spilleren bliver stående efter sin ekstra tur
        die = new StubDie(new int[]{1, 2, 3, 4, 1, 1, -1, 1});
        gameController.die1 = die;
        gameController.die2 = die;
        gameController.playTurn(player2);

        Assert.assertFalse(player2.inJail);
        Assert.assertEquals(1, player2.getCurrentPos());
    }

    @Test
    public void testPlayerLosesMoneyWhenPayingToExitJail() {
        UI.customChoice = "Betal " + GlobalValues.JAIL_PRICE + " kr";
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
        int[] rolls = {-1, 1, -1, 1};
        die = new StubDie(rolls);
        gameController = new GameController(UI, gameBoard, die, die, players);
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
        int[] rolls = {0, 3, 0, 2, 0, 1, -1, 1, -1, 1, -1, 1};
        die = new StubDie(rolls);
        gameController = new GameController(UI, gameBoard, die, die, new Player[]{player1, player2, player3});
        gameController.runGame();

        Assert.assertTrue(gameController.gameEnded);
    }

    @Test
    public void testGameEndsAfter40Rounds() {
        gameBoard = new GameBoard(new Field[]{new StartField("", "", "", Color.RED)}, new ChanceCard[]{});
        int balance = 500000;
        Player player1 = new Player("1", balance);
        Player player2 = new Player("2", balance);
        Player player3 = new Player("3", balance);
        Die die1 = new StubDie(-1);
        Die die2 = new StubDie(1);
        gameController = new GameController(UI, gameBoard, die1, die2, new Player[]{player1, player2, player3});
        gameController.quickGame = true;

        gameController.runGame();

        Assert.assertTrue(gameController.gameEnded);
        Assert.assertEquals(40, gameController.roundCounter);
    }

    @Test
    public void testchecksWhoWantsToTryBidding() {
        gameBoard = new GameBoard(new Field[]{
                new ShippingField(-1)},
                new ChanceCard[]{});
        Player player1 = new Player("1", 0);
        Player player2 = new Player("2", 0);
        Player player3 = new Player("3", 0);
        Player[] playerList = new Player[]{player1, player2, player3};
        die = new StubDie(0);

        gameController = new GameController(UI, gameBoard, die, die, playerList);
        OwnableField ownableField = (OwnableField) gameBoard.fields[0];

        int numOfPlayersBidding = gameController.checksWhoIsBiddingOnAuction(player1, ownableField, playerList);

        Assert.assertEquals(2, numOfPlayersBidding);
        Assert.assertFalse(player1.wantToTryBidding);
        Assert.assertTrue(player2.wantToTryBidding);
        Assert.assertTrue(player3.wantToTryBidding);

        UI.customChoice = "Nej";
        numOfPlayersBidding = gameController.checksWhoIsBiddingOnAuction(player2, ownableField, playerList);
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
        Player[] playerList = new Player[]{player1, player2, player3};

        die = new StubDie(0);
        gameController = new GameController(UI, gameBoard, die, die, playerList);
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
        playerList = new Player[]{player1, player2, player3};

        gameController = new GameController(UI, gameBoard, die, die, playerList);
        ownableField = (OwnableField) gameBoard.fields[0];
        gameController.bidOnAuction(ownableField, numOfPlayersBidding);

        Assert.assertEquals(1000, player1.getBalance());
        Assert.assertEquals(0, player2.getBalance());
        Assert.assertEquals(5000, player3.getBalance());
        Assert.assertFalse(player1.wantToTryBidding);
        Assert.assertTrue(player2.wantToTryBidding);
        Assert.assertFalse(player3.wantToTryBidding);

        player1 = new Player("1", 1000);
        player2 = new Player("2", 7000);
        player3 = new Player("3", 5000);
        playerList = new Player[]{player1, player2, player3};
        UI.customChoice = "Nej";

        gameController = new GameController(UI, gameBoard, die, die, playerList);
        ownableField = (OwnableField) gameBoard.fields[0];
        gameController.bidOnAuction(ownableField, numOfPlayersBidding);

        Assert.assertEquals(0, player1.getBalance());
        Assert.assertEquals(7000, player2.getBalance());
        Assert.assertEquals(5000, player3.getBalance());
        Assert.assertTrue(player1.wantToTryBidding);
        Assert.assertFalse(player2.wantToTryBidding);
        Assert.assertFalse(player3.wantToTryBidding);
    }

    @Test
    public void testMortgageNotPropertFields() {
        ChanceCard[] chanceCards = new ChanceCard[]{};

        OwnableField[] fields = new OwnableField[]{
                new ShippingField("D.F.D.S.", "Pris: kr. 4000", "D.F.D.S.\nMols-Linien", GlobalValues.SHIPPING_RENT),
                new ShippingField("Øresund", "Pris: kr. 4000", "Øresundsredderiet\nHelsingør-Helsingborg", GlobalValues.SHIPPING_RENT),
                new BreweryField("Tuborg", "Pris: kr. 3000", "Tuborg bryggeri"),
        };
        gameBoard = new GameBoard(fields,chanceCards);

        int balance = 30000;
        Player player1 = new Player("1", balance);
        Player[] playerList = new Player[]{player1};
        Die die1 = new StubDie(1);
        Die die2 = new StubDie(0);
        gameController = new GameController(UI, gameBoard, die1, die2, playerList);

        // Køber begge felter
        gameController.playTurn(player1);
        gameController.playTurn(player1);

        Assert.assertEquals(player1, fields[1].owner);
        Assert.assertEquals(player1, fields[2].owner);
        Assert.assertEquals(balance - fields[1].price - fields[2].price, player1.getBalance());

        // netWorth bliver også opdateret i addAmountToBalance-metoden
        player1.addAmountToBalance(-player1.getBalance());
        balance = player1.getBalance();

        // Tjekker metoden mortgageField
        for (int i = 1; i < gameBoard.fields.length; i++) {
            Field field = gameBoard.fields[i];
            if (field instanceof OwnableField ownableField && ownableField.owner == player1) {
                gameController.mortgageField(player1, i, ownableField);
                Assert.assertTrue(fields[i].isMortgaged());
                Assert.assertEquals(balance + (ownableField.price / 2), player1.getBalance());
            }
            balance = player1.getBalance();
        }

        // Sætter samme værdier som før mortgageField-metoden kørte
        fields[1].setMortgaged(false);
        fields[2].setMortgaged(false);

        player1.setCurrentPos(0);
        player1.addToNetWorth(fields[1].price + (fields[1].price / 2) + fields[2].price + (fields[2].price));
        player1.addAmountToBalance(-player1.getBalance());
        balance = player1.getBalance();

        // Tjekker om mortgageField virker når man spiller en tur
        gameController.playTurn(player1);
        Assert.assertEquals(balance + (fields[1].price / 2) + (fields[2].price / 2), player1.getBalance());
    }

    @Test
    public void testSellHotel() {
        ChanceCard[] chanceCards = new ChanceCard[]{};

        PropertyField[] fields = new PropertyField[]{
                new PropertyField("Rødovrevej", "Pris: kr. 1200", "Rødovrevej", 50, 1200,
                        new Color(0, 0, 102), new Color(255, 255, 255), 1000, new int[]{250, 750, 2250, 4000, 6000}),
                new PropertyField("Hvidovrevej", "Pris: kr. 1200", "Hvidovrevej", 50, 1200,
                        new Color(0, 0, 102), new Color(255, 255, 255), 1000, new int[]{250, 750, 2250, 4000, 6000}),
        };
        gameBoard = new GameBoard(fields,chanceCards);

        int balance = 30000;
        Player player1 = new Player("1", balance);
        Player[] playerList = new Player[]{player1};
        Die die1 = new StubDie(1);
        Die die2 = new StubDie(0);
        gameController = new GameController(UI, gameBoard, die1, die2, playerList);

        fields[0].amountOfBuildings = GlobalValues.MAX_AMOUNT_OF_BUILDINGS;
        gameController.sellHotel(player1, 0, fields[0]);

        Assert.assertEquals(0, fields[0].amountOfBuildings);
        Assert.assertEquals(balance + fields[0].buildingPrice / 2 * GlobalValues.MAX_AMOUNT_OF_BUILDINGS, player1.getBalance());
    }

    @Test
    public void testMortgagePropertFields() {
        ChanceCard[] chanceCards = new ChanceCard[]{};

        OwnableField[] fields = new OwnableField[]{
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
        gameBoard = new GameBoard(fields,chanceCards);

        int balance = 30000;
        Player player1 = new Player("1", balance);
        Player player2 = new Player("2", balance);
        Player player3 = new Player("3", balance);
        Player[] playerList = new Player[]{player1, player2, player3};

        die = new StubDie(0);
        gameController = new GameController(UI, gameBoard, die, die, playerList);
        OwnableField ownableField = (OwnableField) gameBoard.fields[0];
        int numOfPlayersBidding = playerList.length;
    }
}