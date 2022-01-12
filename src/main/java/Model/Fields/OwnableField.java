package Model.Fields;

import Model.Player;
import gui_fields.GUI_Field;
import gui_fields.GUI_Ownable;

import java.awt.*;

public abstract class OwnableField extends Field {
    public int rent;
    public int price;
    public Player owner; // public fordi get og set metoder bare getter og sætter værdien

    public OwnableField(String name, String subText, String description, int rent, int price, Color color, Color textColor) {
        super(name, subText, description, color, textColor);
        this.rent = rent;
        this.price = price;
    }

    /**
     * Hvis grunden er ejet, betal ejeren
     *
     * @param player the player who pays rent to the owner
     */
    @Override
    public void fieldAction(Player player) {
        // Der opkræves ingen leje hvis ejeren af feltet er i fængsel
        if (this.owner != null && !this.owner.inJail) {
            player.addAmountToBalance(-rent);
            owner.addAmountToBalance(rent);
        }
    }

    @Override
    public abstract GUI_Ownable getGUIversion();
    }