package Model.Fields;

import Model.Player;
import gui_fields.GUI_Ownable;

import java.awt.*;

public abstract class OwnableField extends Field {
    public int rent;
    public int price;
    private boolean mortgaged = false; // = pantsat
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
        if (this.owner != null && !this.owner.inJail && !this.mortgaged) {
            player.addAmountToBalance(-rent);
            owner.addAmountToBalance(rent);
        }
    }

    @Override
    public abstract GUI_Ownable getGUIversion();

    public boolean isMortgaged() {
        return mortgaged;
    }

    public void setMortgaged(boolean mortgaged) {
        if (mortgaged) {
            this.owner.haveMortgagedField = true;
            this.owner.addAmountToBalance(this.price / 2);
            this.owner.addToNetWorth(-(this.price));
        }
        else {
            // Renten er 10% (der rundes op til nærmeste 100 kr.), og renten betales sammen
            // med lånet, når pantsætningen hæves.
            int stopMortgagePrice = (int) Math.ceil((((double) this.price / 2 * 1.1) / 100.)) * 100;
            this.owner.addAmountToBalance(-stopMortgagePrice);
            this.owner.addToNetWorth(this.price / 2);
        }
        this.mortgaged = mortgaged;
    }
}