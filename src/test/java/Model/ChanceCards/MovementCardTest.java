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
    public void setUp(){
        this.player = new Player("");
        this.gameBoard = new GameBoard();
    }

    @Test
    public void testMoveBackwardsWhenPassingStart() {
        ChanceCard movementCard = gameBoard.chanceCards[15];
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
}