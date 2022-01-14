package Model.ChanceCards;

import Model.GameBoard;
import Model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReceiveMoneyFromBankCardTest {
    private Player player;
    GameBoard gameBoard;

    @Before
    public void setUp() {
        this.player = new Player("", 0);
        this.gameBoard = new GameBoard();
    }

    @Test
    public void testPlayerReceivesMoney() {
        ChanceCard chanceCard = new ReceiveMoneyFromBankCard("", "", 1000);
        chanceCard.cardAction(player, gameBoard);
        Assert.assertEquals(1000, player.getBalance());
    }
}