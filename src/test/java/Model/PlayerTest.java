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

    @Test
    public void testSetCurrentPos() {
        player.setCurrentPos(2);
        player.setCurrentPos(4);

        assertEquals(2, player.getPreviousPos());
        assertEquals(4, player.getCurrentPos());
    }
}