package Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PlayerTest {
    Player player;
    GameBoard gameBoard;

    @Before
    public void createsPlayer() {
        player = new Player("");
        gameBoard = new GameBoard();
    }

    //Test for, om get og set position metoderne fungerer
    @Test
    public void playerMovesSetAmount() {
        player.setCurrentPos(2);
        player.setPreviousPos(2);

        assertEquals(player.getCurrentPos(), player.getPreviousPos());
    }

    //Test for, at være sikker på, at positionen bliver opdateret
    @Test
    public void playerMovesSetAmountOne() {
        player.setPreviousPos(2);
        player.setCurrentPos(4);
        assertNotEquals(player.getCurrentPos(), player.getPreviousPos());
    }
}