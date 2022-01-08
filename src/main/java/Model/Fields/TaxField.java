package Model.Fields;

import Model.Player;
import gui_fields.GUI_Tax;

import java.awt.*;

public class TaxField extends Field {
    public final int tax;

    public TaxField(String name, String subText, String description, int tax) {
        super(name, subText, description, Color.GRAY, Color.BLACK);
        this.tax = tax;
    }

    @Override
    public void fieldAction(Player player) {
        player.addAmountToBalance(-tax);
    }

    @Override
    public GUI_Tax getGUIversion() {
        return new GUI_Tax(this.fieldName, this.subText, this.description, this.backgroundColor, this.textColor);
    }
}