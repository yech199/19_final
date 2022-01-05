package Model.Fields;

import gui_fields.GUI_Refuge;

import java.awt.*;

public class FreeParkingField extends Field {
    public FreeParkingField(String name, String subText, String description) {
        super(name, subText, description, Color.WHITE, Color.BLACK);
    }

    /**
     * Translates the fields we made to Fields in GUI format, so the GUI can use the fields
     *
     * @return the GUI version of the fields made in the FreeParkingField constructor
     */
    @Override
    public GUI_Refuge getGUIversion() {
        return new GUI_Refuge("default", this.fieldName, this.subText, this.description, this.backgroundColor, this.textColor);
    }
}
