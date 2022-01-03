package Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KontoTest {
    Player player;

    @Before
    public void createsPlayer() {
        player = new Player("");
    }

    //Test for om balancen tilføjes korrekt
    @Test
    public void balanceAddsSetAmount() {
        player.setBalance(20);
        player.addAmountToBalance(100);

        assertEquals(120, player.getBalance());
    }

    //Tester, om kontoen forstår minus beløb
    @Test
    public void balanceRemovesSetAmount() {
        player.addAmountToBalance(-69);
        assertEquals(-69, player.getBalance());
    }
}