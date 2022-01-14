package Model.ChanceCards;

import Model.GameBoard;
import Model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GoToJailCardTest {
    private Player player;
    GameBoard gameBoard;

    @Before
    public void setUp() {
        this.player = new Player("");
        this.gameBoard = new GameBoard();
    }

    @Test
    public void testPlayerIsPutInJailAndReceivesNoMoneyWhenPassingStart() {
        player.setCurrentPos(gameBoard.fields.length - 1);
        ChanceCard chanceCard = new GoToJailCard("", "");
        int prev = player.getBalance();
        chanceCard.cardAction(player, gameBoard);
        Assert.assertTrue(player.inJail);

        Assert.assertEquals(prev, player.getBalance());
    }
}