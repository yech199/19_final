package Model.Fields;

import gui_fields.GUI_Chance;

import java.awt.*;

public class ChanceField extends Field {

    public ChanceField(String name, String subText, String description, Color color) {
        super(name, subText, description, color);
    }

    /**
     * Translates the fields we made to Fields in GUI format, so the GUI can use the fields
     *
     * @return the GUI version of the fields made in the Field constructor
     */
    @Override
    public GUI_Chance getGUIversion() {
        return new GUI_Chance(this.fieldName, this.subText, this.description, this.backgroundColor, this.textColor);
    }
}
