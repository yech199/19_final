package Model.ChanceCards;


import Model.Fields.ChanceField;
import Model.Fields.Field;
import Model.Fields.ShippingField;
import Model.Fields.StartField;
import Model.GameBoard;
import Model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

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
    public void testPlayerIsMovedToCorrectPosition() {
        int index = 19;
        ChanceCard movementCard = new MovementCard("", "", index, MovementCard.MovementType.INDEX);
        movementCard.cardAction(player, gameBoard);
        Assert.assertEquals(index, player.getCurrentPos());

        player.setCurrentPos(0);
        index = 3;
        movementCard = new MovementCard("", "", index, MovementCard.MovementType.NUMBER);
        movementCard.cardAction(player, gameBoard);
        Assert.assertEquals(3, player.getCurrentPos());
    }

    @Test
    public void testPlayerMovesToNearest() {
        ChanceCard[] chanceCards = new ChanceCard[]{
                new MovementCard("", "", -1, MovementCard.MovementType.NEAREST)
        };

        Field[] fields = new Field[]{
                new StartField("", "", "", Color.BLACK),
                new ShippingField("", "", "", 0),
                new ShippingField("", "", "", 0),
                new ChanceField()
        };

        gameBoard = new GameBoard(fields, chanceCards);
        chanceCards[0].cardAction(player, gameBoard);
        Assert.assertEquals(1, player.getCurrentPos());

        player.setCurrentPos(3);
        chanceCards[0].cardAction(player, gameBoard);
        Assert.assertEquals(1, player.getCurrentPos());
    }
}