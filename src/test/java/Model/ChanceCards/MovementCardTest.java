package Model.ChanceCards;


import Model.GameBoard;
import Model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MovementCardTest {
    private Player player;
    GameBoard gameBoard;

    @Before
    public void setUp() {
        this.player = new Player("");
        this.gameBoard = new GameBoard();
    }

    @Test
    public void testMoveBackwardsWhenPassingStart() {
        MovementCard movementCard = new MovementCard("", "", -3, MovementCard.MovementType.NUMBER);
        movementCard.cardAction(player, gameBoard);
        Assert.assertEquals(gameBoard.fields.length - 3, player.getCurrentPos());
    }

    @Test
    public void testReceiveMoneyWhenPassingStart() {
        player.setCurrentPos(gameBoard.fields.length - 1);
        MovementCard movementCard = new MovementCard("", "", 1, MovementCard.MovementType.NUMBER);
        int prev = player.getBalance();
        movementCard.cardAction(player, gameBoard);
        Assert.assertEquals(prev + 4000, player.getBalance());

        player.setCurrentPos(gameBoard.fields.length - 1);
        movementCard = new MovementCard("", "", 1, MovementCard.MovementType.INDEX);
        prev = player.getBalance();
        movementCard.cardAction(player, gameBoard);
        Assert.assertEquals(prev + 4000, player.getBalance());
    }

    @Test
    public void testPlayerIsPutInJailAndReceivesNoMoneyWhenPassingStart() {
        player.setCurrentPos(gameBoard.fields.length - 1);
        ChanceCard movementCard = new MovementCard("", "", 10, MovementCard.MovementType.INDEX);
        int prev = player.getBalance();
        movementCard.cardAction(player, gameBoard);
        Assert.assertTrue(player.inJail);

        Assert.assertEquals(prev, player.getBalance());
    }

    @Test
    public void testPlayerIsMovedToCorrectPosition() {
        int index = 19;
        ChanceCard movementCard = new MovementCard("", "", index, MovementCard.MovementType.INDEX);
        movementCard.cardAction(player, gameBoard);
        Assert.assertEquals(index, player.getCurrentPos());
    }
}