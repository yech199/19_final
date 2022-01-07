package Model.Fields;

import Model.Player;
import gui_fields.GUI_Street;

import java.awt.*;

public class PropertyField extends OwnableField {
    private final int[] houseRents;
    private final int buildingPrice;
    private int amountOfBuildings;

    public PropertyField(String name, String subText, String description, int rent, int price, Color color, Color textColor, int buildingPrice, int[] houseRents) {
        super(name, subText, description, rent, price, color, textColor);
        this.houseRents = houseRents;
        amountOfBuildings = 0;
        this.buildingPrice = buildingPrice;

    }

    public void buyBuilding(Player player) {
        if (amountOfBuildings < 5) {
            amountOfBuildings++;
            player.addAmountToBalance(-buildingPrice);
            rent = rents[amountOfBuildings];
        }
    }

    public void sellBuilding(Player player) {
        if (amountOfBuildings > 0) {
            amountOfBuildings--;
            player.addAmountToBalance(buildingPrice);
            rent = rents[amountOfBuildings];
        }
    }

    public int getAmountOfBuildings() {
        return amountOfBuildings;
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