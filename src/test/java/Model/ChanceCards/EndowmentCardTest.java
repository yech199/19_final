package Model.ChanceCards;

import Model.GameBoard;
import Model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EndowmentCardTest {
    Player player;
    GameBoard gameBoard;

    @Before
    public void setUp() {
        this.player = new Player("", 2000);
        this.gameBoard = new GameBoard();
    }

    @Test
    public void cardAction() {
        int balance = 2000;
        int endowment = 40000;
        ChanceCard chanceCard = new EndowmentCard("", "", endowment, 1500);
        chanceCard.cardAction(player, gameBoard);
        Assert.assertEquals(balance, player.getBalance());

        balance = 1000;
        this.player = new Player("", balance);
        chanceCard.cardAction(player, gameBoard);
        Assert.assertEquals(balance + endowment, player.getBalance());
    }
}