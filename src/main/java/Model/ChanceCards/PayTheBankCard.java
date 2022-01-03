package Model.ChanceCards;

import Model.GameBoard;
import Model.Player;

public class PayTheBankCard extends ChanceCard {
    private final int amount;

    public PayTheBankCard(String name, String text, int amount) {
        super(name, text);
        this.amount = amount;
    }

    @Override
    public void cardAction(Player player, GameBoard gameBoard) {
        player.addAmountToBalance(-amount);
    }
}
