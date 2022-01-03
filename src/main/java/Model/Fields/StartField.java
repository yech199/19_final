package Model.Fields;

import gui_fields.GUI_Start;

import java.awt.*;

public class StartField extends Field {

    public StartField(String name, String subText, String description, Color color) {
        super(name, subText, description, color);
    }

    /**
     * Translates the fields we made to Fields in GUI format, so the GUI can use the fields
     *
     * @return the GUI version of the fields made in the StartField constructor
     */
    @Override
    public GUI_Start getGUIversion() {
        return new GUI_Start(this.fieldName, this.subText, this.description, this.backgroundColor, this.textColor);
    }
}
