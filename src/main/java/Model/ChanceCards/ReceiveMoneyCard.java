package Model.ChanceCards;

import Model.GameBoard;
import Model.Player;

public class ReceiveMoneyCard extends ChanceCard {
    public enum ReceivingFrom {
        BANK,
        OTHER_PLAYERS
    }

    private final ReceivingFrom receivingFrom;
    private final int amount;

    public ReceiveMoneyCard(String name, String text, int amount, ReceivingFrom receivingFrom) {
        super(name, text);
        this.amount = amount;
        this.receivingFrom = receivingFrom;
    }

    @Override
    public void cardAction(Player player, GameBoard gameBoard) {
        switch (receivingFrom) {
            case BANK ->
                player.addAmountToBalance(amount);
            case OTHER_PLAYERS -> {
                // FIXME:
            }
        }
    }
}
