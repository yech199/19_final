package Model.Fields;

import Model.Player;
import gui_fields.GUI_Tax;

import java.awt.*;

public class IncomeTaxField extends Field{
    public final int tax;
    public final int percent;

    public IncomeTaxField(String name, String subText, String description, int tax, int percent) {
        super(name, subText, description, Color.GRAY, Color.BLACK);
        this.tax = tax;
        this.percent = percent;
    }

    @Override
    public void fieldAction(Player player) {
    }

    @Override
    public GUI_Tax getGUIversion() {
        return new GUI_Tax(this.fieldName, this.subText, this.description, this.backgroundColor, this.textColor);
    }
}
