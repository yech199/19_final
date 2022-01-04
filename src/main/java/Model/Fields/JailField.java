package Model.Fields;

import gui_fields.GUI_Jail;

import java.awt.*;

public class JailField extends Field {
    public JailField(String name, String subText, String description) {
        super(name, subText, description, new Color(125, 125, 125), Color.WHITE);
    }

    /**
     * Translates the fields we made to Fields in GUI format, so the GUI can use the fields.
     *
     * @return the GUI version of the fields made in the JailField constructor
     */
    @Override
    public GUI_Jail getGUIversion() {
        return new GUI_Jail("default", this.fieldName, this.subText, this.description, this.backgroundColor, this.textColor);
    }
}
