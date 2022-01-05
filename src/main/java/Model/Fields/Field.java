package Model.Fields;

import Model.Player;
import gui_fields.GUI_Field;

import java.awt.*;

/**
 * Model.Field.Model.Field klassen skaber objekter med seperate informationer omkring et felt.
 */

public abstract class Field {
    public final String fieldName;
    public final String subText;
    public final String description;
    public final Color backgroundColor;
    public final Color textColor;

    public Field(String name, String subText, String description, Color color, Color textColor) {
        this.fieldName = name;
        this.subText = subText;
        this.description = description;
        this.backgroundColor = color;
        this.textColor = textColor;
    }

    public void fieldAction(Player player) {
    }

    /**
     * Translates the fields we made to Fields in GUI format, so the GUI can use the fields
     *
     * @return the GUI version of the fields made in the Field constructor
     */
    public abstract GUI_Field getGUIversion();
}