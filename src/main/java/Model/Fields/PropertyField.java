package Model.Fields;

import Model.GlobalValues;
import Model.Player;
import gui_fields.GUI_Street;

import java.awt.*;

public class PropertyField extends OwnableField {
    public int amountOfBuildings;
    public final int[] rents;
    public final int buildingPrice;

    public PropertyField(String name, String subText, String description, int rent, int price, Color color, Color textColor, int buildingPrice, int[] buildingRents) {
        super(name, subText, description, rent, price, color, textColor);
        assert buildingRents.length == 5;
        amountOfBuildings = 0;
        this.rents = new int[] {rent, buildingRents[0], buildingRents[1], buildingRents[2], buildingRents[3], buildingRents[4]};
        this.buildingPrice = buildingPrice;

    }

    public void buyBuilding(Player player) {
        if (this.amountOfBuildings < GlobalValues.MAX_AMOUNT_OF_BUILDINGS) {
            this.amountOfBuildings++;
            player.addAmountToBalance(-this.buildingPrice);
            // Bygninger er mindre værd hvis den skal sælges igen. Derfor tilføjes der til netWorth igen
            // Kun halvdelen af prisen man betaler for bygningen tilføjer værdi til din netWorth
            player.addToNetWorth(this.buildingPrice);
            this.rent = this.rents[this.amountOfBuildings];
        }
    }

    public void sellBuilding(Player player) {
        if (this.amountOfBuildings > 0) {
            this.amountOfBuildings--;
            // Bygninger sælges for halv pris
            player.addAmountToBalance(this.buildingPrice / 2);
            player.addToNetWorth(-(this.buildingPrice));
            this.rent = this.rents[this.amountOfBuildings];
        }
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