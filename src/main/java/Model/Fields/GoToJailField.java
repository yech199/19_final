package Model.Fields;

import Model.Player;
import gui_fields.GUI_Jail;

import java.awt.*;

public class GoToJailField extends Field {
    public GoToJailField(String name, String subText, String description, Color color) {
        super(name, subText, description, color);
    }

    @Override
    public void fieldAction(Player player) {
        player.setCurrentPos(6);
        player.inJail = true;
    }

    /**
     * Translates the fields we made to Fields in GUI format, so the GUI can use the fields
     *
     * @return the GUI version of the fields made in the GoToJailField constructor
     */
    @Override
    public GUI_Jail getGUIversion() {
        return new GUI_Jail("default", this.fieldName, this.subText, this.description, this.backgroundColor, this.textColor);
    }
}
