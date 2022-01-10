package Model.ChanceCards;

import Model.GameBoard;
import Model.GlobalValues;
import Model.Player;

public class GoToJailCard extends ChanceCard {
    public GoToJailCard(String name, String cardText) {
        super(name, cardText);
    }

    @Override
    public void cardAction(Player player, GameBoard gameBoard) {
        player.setCurrentPos(GlobalValues.JAIL_INDEX);
        player.inJail = true;
    }
}
