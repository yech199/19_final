package Model;

import Model.ChanceCards.ChanceCard;
import Model.Fields.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class GameBoardTest {
    ChanceCard[] chanceCards;
    GameBoard gameBoard;

    @Before
    public void setUp() {
        chanceCards = new ChanceCard[]{};
    }

    @Test
    public void testFindsPropertyFieldsOfSameColor() {
        Field[] fields = new Field[]{
                new StartField("", "", "", Color.RED),
                new PropertyField("Rødovrevej", "Pris: kr. 1200", "Rødovrevej", 50, 1200,
                        new Color(0, 0, 102), new Color(255, 255, 255), new int[]{250, 750, 2250, 4000, 6000}),
                new ChanceField(),
                new PropertyField("Hvidovrevej", "Pris: kr. 1200", "Hvidovrevej", 50, 1200,
                        new Color(0, 0, 102), new Color(255, 255, 255), new int[]{250, 750, 2250, 4000, 6000}),
                new TaxField("Betal\nindkomst-\nskat", "10% el. 4000", "Betal indkomstskat\n10% eller kr. 4000,-"),
                new ShippingField("Øresund", "Pris: kr. 4000", "Øresundsredderiet", 500),
                new PropertyField("Roskildevej", "Pris: kr. 2000", "Roskildevej", 100, 2000,
                        new Color(255, 128, 0), new Color(0, 0, 0), new int[]{600, 1800, 5400, 8000, 11000}),
                new ChanceField(),
                new PropertyField("Valby\nLanggade", "Pris: kr. 2000", "Valby Langgade", 100, 2000,
                        new Color(255, 128, 0), new Color(0, 0, 0), new int[]{600, 1800, 5400, 8000, 11000}),
                new PropertyField("Allégade", "Pris: kr. 2400", "Allégade", 150, 2400,
                        new Color(255, 128, 0), new Color(0, 0, 0), new int[]{800, 2000, 6000, 9000, 12000}),
                new JailField("Fængsel", "Fængsel", "På besøg i fængslet"),
                new ChanceField(),
                new ShippingField("Ø.S.", "Pris: kr. 4000", "Ø.S. redderiet", 500),
                new BreweryField("Carlsberg", "Pris: kr. 3000", "Carlsberg bryggeri"),
                };

        gameBoard = new GameBoard(fields, chanceCards);
        int numberOfFieldsInSameColor = gameBoard.getFieldGroup(new Color(0, 0, 102)).length;
        Assert.assertEquals(2, numberOfFieldsInSameColor);

        numberOfFieldsInSameColor = gameBoard.getFieldGroup(new Color(255, 128, 0)).length;
        Assert.assertEquals(3, numberOfFieldsInSameColor);
    }
}