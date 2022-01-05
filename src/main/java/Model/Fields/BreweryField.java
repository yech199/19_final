package Model.Fields;

import Model.Player;
import gui_fields.GUI_Brewery;
import Model.Die;
import gui_fields.GUI_Player;

import java.awt.*;

public class BreweryField extends OwnableFields{
    public BreweryField(String name, String subText, String description) {
        super(name, subText, description, 0, 3000, Color.BLACK, Color.WHITE);
    }

    private int faceValue;

    @Override
    public void fieldAction(Player player) {
        rent = faceValue * 100;
        if (this.owner != null) {
            player.addAmountToBalance(-rent);
            owner.addAmountToBalance(rent);
        }
    }

    @Override
    public GUI_Brewery getGUIversion() {
        return new GUI_Brewery("default", this.fieldName, this.subText, this.description, this.rent + "kr.", this.backgroundColor, this.textColor);
    }

    public void setFaceValue(int value){
        faceValue = value;
    }
}