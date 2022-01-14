package Model.ChanceCards;

import Model.GameBoard;
import Model.Player;

public class EndowmentCard extends ChanceCard {
    private final int ENDOWMENT;
    private final int MIN_NET_WORTH;
    public EndowmentCard(String name, String cardText, int ENDOWMENT, int MIN_NET_WORTH) {
        super(name, cardText);
        this.ENDOWMENT = ENDOWMENT;
        this.MIN_NET_WORTH = MIN_NET_WORTH;
    }

    @Override
    public void cardAction(Player player, GameBoard gameBoard) {
        if (player.getNetWorth() <= this.MIN_NET_WORTH) {
            player.addAmountToBalance(this.ENDOWMENT);
        }
    }
}