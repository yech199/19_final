package Model.Fields;

import Model.Player;
import gui_fields.GUI_Tax;

import java.awt.*;

public class OtherTaxField extends Field {
    public final int TAX;

    public OtherTaxField(String name, String subText, String description, int TAX) {
        super(name, subText, description, Color.GRAY, Color.BLACK);
        this.TAX = TAX;
    }

    @Override
    public void fieldAction(Player player) {
        player.addAmountToBalance(-TAX);
    }

    @Override
    public GUI_Tax getGUIversion() {
        return new GUI_Tax(this.fieldName, this.subText, this.description, this.backgroundColor, this.textColor);
    }
}