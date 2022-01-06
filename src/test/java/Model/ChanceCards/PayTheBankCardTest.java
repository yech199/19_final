package Model.ChanceCards;

import Model.GameBoard;
import Model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PayTheBankCardTest {
    private Player player;
    GameBoard gameBoard;

    @Before
    public void setUp() {
        this.player = new Player("");
        this.gameBoard = new GameBoard();
    }

    @Test
    public void testPlayerLosesMoney() {
        player.setBalance(1000);
        ChanceCard chanceCard = new PayTheBankCard("", "", 1000);
        chanceCard.cardAction(player, gameBoard);
        Assert.assertEquals(0, player.getBalance());
    }
}