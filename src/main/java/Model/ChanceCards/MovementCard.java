package Model.ChanceCards;

import Model.Fields.Field;
import Model.GameBoard;
import Model.Player;

/**
 * This class includes cards where you have to move either x fields backwards or forwards or
 * to a specific field/fieldColor.
 * Alle constructorer skal have forskellige tupler som parameter for at vide hvilken parameter man refererer til når
 * man instantierer dem
 */
public class MovementCard extends ChanceCard {
    public enum MovementType {
        NUMBER,
        INDEX
    }

    private final int fieldIndex;
    private final MovementType movementType;

    /**
     * This card move you to a specific field. The field's location is given through its index number in the FieldArray.
     * Constructoren tager imod (String, String, int, boolean)
     *
     * @param name         of the card, so it's easy to identify
     * @param text         describing what the card does
     * @param movementType
     */
    public MovementCard(String name, String text, int fieldIndex, MovementType movementType) {
        super(name, text);
        this.fieldIndex = fieldIndex;
        this.movementType = movementType;
    }

    public void cardAction(Player player, GameBoard gameBoard) {
        switch (movementType) {
            case NUMBER -> {
                player.setCurrentPos(player.getCurrentPos() + fieldIndex % gameBoard.fields.length);
            }
            case INDEX -> {
                player.setCurrentPos(fieldIndex);
            }
        }

    }
}
