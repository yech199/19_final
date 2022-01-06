package Model.Fields;

import Model.Player;
import gui_fields.GUI_Street;

import java.awt.*;

public class PropertyField extends OwnableFields {
    private int amountOfHouses;
    private int[] houseRent;

    public PropertyField(String name, String subText, String description, int rent, int price, Color color, Color textColor, int [] houseRent) {
        super(name, subText, description, rent, price, color, textColor);
        amountOfHouses = 0;
        this.houseRent = houseRent;
    }

    public void addHouse(int house){
        amountOfHouses = amountOfHouses + house;
        if (amountOfHouses > 1){
            amountOfHouses = 1;
        }
    }

    public void removeHouse(int house){
        amountOfHouses = amountOfHouses - house;
        if (amountOfHouses < 0){
            amountOfHouses = 0;
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