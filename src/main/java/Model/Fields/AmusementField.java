package Model.Fields;

import Model.Player;
import gui_fields.GUI_Street;

import java.awt.*;

public class AmusementField extends Field {
    public int rent;
    public Player owner; // public fordi get og set metoder bare getter og sætter værdien

    public AmusementField(String name, String subText, String description, int rent, Color color) {
        super(name, subText, description, color);
        this.rent = rent;
    }

    /**
     * Hvis grunden er ejet, betal ejeren
     *
     * @param player the player who pays rent to the owner
     */
    @Override
    public void fieldAction(Player player) {
        if (this.owner != null) {
            player.addAmountToBalance(-rent);
            owner.addAmountToBalance(rent);
        }
    }

    /**
     * Translates the fields we made to Fields in GUI format, so the GUI can use the fields
     *
     * @return the GUI version of the fields made in the AmusementField constructor
     */
    @Override
    public GUI_Street getGUIversion() {
        return new GUI_Street(this.fieldName, this.subText, this.description, this.rent + "$", this.backgroundColor, this.textColor);
    }

}
