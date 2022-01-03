package Model.ChanceCards;

import Model.GameBoard;
import Model.Player;

public class ReleaseFromPrisonCard extends ChanceCard {

    public ReleaseFromPrisonCard(String name, String text) {
        super(name, text);
    }

    @Override
    public void cardAction(Player player, GameBoard gameBoard) {
        player.getOutOfJailFree = true;
    }
}
