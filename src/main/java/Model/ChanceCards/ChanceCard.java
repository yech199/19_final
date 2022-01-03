package Model.ChanceCards;

import Model.GameBoard;
import Model.Player;

/**
 * This abstract class makes chance cards of different types, with different cardActions
 */
public abstract class ChanceCard {
    public final String name;
    public final String cardText;

    public ChanceCard(String name, String cardText) {
        this.name = name;
        this.cardText = cardText;
    }

    public abstract void cardAction(Player player, GameBoard gameBoard);

}

