package Model.Fields;

import Model.Player;
import gui_fields.GUI_Shipping;

import java.awt.*;

public class ShippingField extends OwnableField {
    public final int[] shippingRents;

    public ShippingField(String name, String subText, String description, int rent, int price) {
        super(name, subText, description, rent, price, Color.WHITE, Color.BLACK);
        this.shippingRents = new int[]{rent, rent * 2, rent * 4, rent * 8};
    }

    public ShippingField(String name, String subText, String description, int rent) {
        this(name, subText, description, rent, 4000);
    }

    public ShippingField(int rent, int price) {
        this("", "", "", rent, price);
    }

    @Override
    public void fieldAction(Player player) {
    }

    @Override
    public GUI_Shipping getGUIversion() {
        return new GUI_Shipping("default", this.fieldName, this.subText, this.description, this.rent + "kr.", this.backgroundColor, this.textColor);
    }
}
