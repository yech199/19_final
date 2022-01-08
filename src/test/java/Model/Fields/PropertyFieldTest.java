package Model.Fields;

import Model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class PropertyFieldTest {
    Player player;
    PropertyField propertyField;

    @Before
    public void setUp() {
        player = new Player("");
        propertyField = new PropertyField("", "", "", 1, 1000, Color.BLACK, Color.BLACK, 1000, new int[]{2, 3, 4, 5, 6});
    }

    @Test
    public void testPlayerPaysForBuilding() {
        int balance = 10000;
        player.setBalance(balance);

        // Tjek at propertyField har den originale rent
        Assert.assertEquals(propertyField.rents[0], propertyField.rent);

        for (int numberOfBuildings = 1; numberOfBuildings < propertyField.rents.length; numberOfBuildings++) {
            propertyField.buyBuilding(player);

            Assert.assertEquals(balance - propertyField.buildingPrice * numberOfBuildings, player.getBalance());
            Assert.assertEquals(propertyField.rents[numberOfBuildings], propertyField.rent);
        }

        int amountOfBuildings = propertyField.getAmountOfBuildings();

        // Forsøg at købe endnu en bygning, selvom der ikke kan købes flere
        propertyField.buyBuilding(player);

        // Tjek at der ikke blev købt flere bygninger og at spilleren ikke mistede penge
        Assert.assertEquals(amountOfBuildings, propertyField.getAmountOfBuildings());
        Assert.assertEquals(balance - propertyField.buildingPrice * propertyField.getAmountOfBuildings(), player.getBalance());
        Assert.assertEquals(propertyField.rents[5], propertyField.rent);
    }

    @Test
    public void testPlayerSellsBuilding() {
        for (int i = 0; i < 5; i++)
            propertyField.buyBuilding(player);

        int balance = 0;
        player.setBalance(balance);

        // Tjek at propertyField har den originale rent
        Assert.assertEquals(propertyField.rents[5], propertyField.rent);

        for (int numberOfBuildings = propertyField.getAmountOfBuildings(); numberOfBuildings > 0; numberOfBuildings--) {
            propertyField.sellBuilding(player);

            Assert.assertEquals(balance + propertyField.buildingPrice * (5 - propertyField.getAmountOfBuildings()), player.getBalance());
            Assert.assertEquals(propertyField.rents[numberOfBuildings - 1], propertyField.rent);
        }

        int amountOfBuildings = propertyField.getAmountOfBuildings();

        // Forsøg at sælge endnu en bygning, selvom der ikke kan sælges flere
        propertyField.sellBuilding(player);

        // Tjek at der ikke blev solgt flere bygninger og at spilleren ikke fik penge
        Assert.assertEquals(amountOfBuildings, propertyField.getAmountOfBuildings());
        Assert.assertEquals(balance + propertyField.buildingPrice * (5 - propertyField.getAmountOfBuildings()), player.getBalance());
        Assert.assertEquals(propertyField.rents[0], propertyField.rent);
    }
}