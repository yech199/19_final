package Model.Fields;

import gui_fields.GUI_Tax;

import java.awt.*;

public class TaxField extends Field {

    public TaxField(String name, String subText, String description) {
        super(name, subText, description, Color.GRAY, Color.BLACK);
    }

    @Override
    public GUI_Tax getGUIversion() {
        return new GUI_Tax(this.fieldName, this.subText, this.description, this.backgroundColor, this.textColor);
    }
}