package Model.ChanceCards;

import Model.Fields.PropertyField;
import Model.GameBoard;
import Model.GlobalValues;
import Model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class BuildingTaxCardTest {
    Player player;
    GameBoard gameBoard;

    @Before
    public void setUp() {
        int balance = 30000;
        this.player = new Player("", balance);
        this.gameBoard = new GameBoard();
    }

    @Test
    public void cardAction() {
        int housePrice = 500;
        int hotelPrice = 2000;

        ChanceCard[] chanceCards = new ChanceCard[]{new BuildingTaxCard("Oliepriser", "Oliepriserne er steget, " +
                "og De skal betale kr 500 pr hus og kr 2000 pr hotel.", housePrice, hotelPrice),};
        PropertyField[] fields = new PropertyField[]{
                new PropertyField("Rødovrevej", "Pris: kr. 1200", "Rødovrevej", 50, 1200,
                        new Color(0, 0, 102), new Color(255, 255, 255), 1000, new int[]{250, 750, 2250, 4000, 6000}),
                new PropertyField("Hvidovrevej", "Pris: kr. 1200", "Hvidovrevej", 50, 1200,
                        new Color(0, 0, 102), new Color(255, 255, 255), 1000, new int[]{250, 750, 2250, 4000, 6000}),
                new PropertyField("Roskildevej", "Pris: kr. 2000", "Roskildevej", 100, 2000,
                        new Color(255, 128, 0), new Color(0, 0, 0), 1000, new int[]{600, 1800, 5400, 8000, 11000}),
                new PropertyField("Valby\nLanggade", "Pris: kr. 2000", "Valby Langgade", 100, 2000,
                        new Color(255, 128, 0), new Color(0, 0, 0), 1000, new int[]{600, 1800, 5400, 8000, 11000}),
                new PropertyField("Allégade", "Pris: kr. 2400", "Allégade", 150, 2400,
                        new Color(255, 128, 0), new Color(0, 0, 0), 1000, new int[]{800, 2000, 6000, 9000, 12000})
        };

        gameBoard = new GameBoard(fields, chanceCards);
        int balance = player.getBalance();
        int houseCount = 0;
        int hotelCount = 0;

        for (int i = 0; i < fields.length; i++) {
            PropertyField field = fields[i];
            for (int j = 0; j < GlobalValues.MAX_AMOUNT_OF_BUILDINGS - i; j++) {
                field.buyBuilding(player);
                houseCount++;

                if (field.amountOfBuildings == GlobalValues.MAX_AMOUNT_OF_BUILDINGS) {
                    houseCount -= 5;
                    hotelCount++;
                }
            }
            field.owner = player;
        }
        balance = player.getBalance();
        // System.out.println("Huse: " + houseCount);
        // System.out.println("Hotel: " + hotelCount);

        chanceCards[0].cardAction(player, gameBoard);

        Assert.assertEquals(10, houseCount);
        Assert.assertEquals(1, hotelCount);
        Assert.assertEquals(balance - houseCount * housePrice - hotelCount * hotelPrice, player.getBalance());
    }
}