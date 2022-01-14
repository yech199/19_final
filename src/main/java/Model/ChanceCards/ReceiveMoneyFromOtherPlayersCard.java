package Model.ChanceCards;

import Model.GameBoard;
import Model.Player;

public class ReceiveMoneyFromOtherPlayersCard extends ChanceCard {
    public final int AMOUNT;
    public ReceiveMoneyFromOtherPlayersCard(String name, String cardText, int AMOUNT) {
        super(name, cardText);
        this.AMOUNT = AMOUNT;
    }

    @Override
    public void cardAction(Player player, GameBoard gameBoard) {
    }
}
