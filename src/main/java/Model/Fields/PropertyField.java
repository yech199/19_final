package Model.Fields;

import Model.Player;
import gui_fields.GUI_Street;

import java.awt.*;

public class PropertyField extends OwnableField {
    private int amountOfHouses;
    private final int[] houseRents;
    private final int buildingPrice;

    public PropertyField(String name, String subText, String description, int rent, int price, Color color, Color textColor, int buildingPrice, int[] houseRents) {
        super(name, subText, description, rent, price, color, textColor);
        amountOfHouses = 0;
        this.houseRents = houseRents;
        this.buildingPrice = buildingPrice;

    }

    public void addHouse(Player player) {
        if (amountOfHouses < 4) {
            amountOfHouses++;
            player.addAmountToBalance(-buildingPrice);
        }
        rent = houseRents[amountOfHouses - 1];
    }

    public void removeHouse(Player player) {
        if (amountOfHouses > 0) {
            amountOfHouses--;
            player.addAmountToBalance(buildingPrice);
        }
    }

    public int getAmountOfHouses() {
        return amountOfHouses;
    }

    /**
     * Translates the fields we made to Fields in GUI format, so the GUI can use the fields
     *
     * @return the GUI version of the fields made in the AmusementField constructor
     */
    @Override
    public GUI_Street getGUIversion() {
        return new GUI_Street(this.fieldName, this.subText, this.description, this.rent + "kr.", this.backgroundColor, this.textColor);
    }
}