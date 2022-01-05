package Model.Fields;

import Model.Player;
import gui_fields.GUI_Shipping;

import java.awt.*;

public class ShippingField extends OwnableFields{
    public ShippingField(String name, String subText, String description, int rent) {
        super(name, subText, description, rent, 4000, Color.WHITE, Color.BLACK);
    }

    @Override
    public void fieldAction(Player player) {
    }

    @Override
    public GUI_Shipping getGUIversion() {
        return new GUI_Shipping("default", this.fieldName, this.subText, this.description, this.rent + "kr.", this.backgroundColor, this.textColor);
    }
}
