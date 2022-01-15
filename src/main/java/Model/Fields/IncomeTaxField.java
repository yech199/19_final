package Model.Fields;

import Model.Player;
import gui_fields.GUI_Tax;

import java.awt.*;

public class IncomeTaxField extends Field{
    public final int TAX;
    public final int PERCENT;

    public IncomeTaxField(String name, String subText, String description, int TAX, int percent) {
        super(name, subText, description, Color.GRAY, Color.BLACK);
        this.TAX = TAX;
        this.PERCENT = percent;
    }

    @Override
    public GUI_Tax getGUIversion() {
        return new GUI_Tax(this.fieldName, this.subText, this.description, this.backgroundColor, this.textColor);
    }
}
