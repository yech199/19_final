package Model.ChanceCards;

import Model.GameBoard;
import Model.Player;

public class ReceiveMoneyFromBankCard extends ChanceCard {
    private final int AMOUNT;

    public ReceiveMoneyFromBankCard(String name, String text, int AMOUNT) {
        super(name, text);
        this.AMOUNT = AMOUNT;
    }

    @Override
    public void cardAction(Player player, GameBoard gameBoard) {
        player.addAmountToBalance(AMOUNT);
    }
}
