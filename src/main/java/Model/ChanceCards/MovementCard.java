package Model.ChanceCards;

import Model.GameBoard;
import Model.GlobalValues;
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
        INDEX,
        NEAREST
    }

    private final int fieldIndex;
    private final MovementType movementType;

    /**
     * This card move you to a specific field. The field's location is given through its index number in the FieldArray.
     * Constructoren tager imod (String, String, int, boolean)
     *
     * @param name         of the card, so it's easy to identify
     * @param text         describing what the card does
     */
    public MovementCard(String name, String text, int fieldIndex, MovementType movementType) {
        super(name, text);
        this.fieldIndex = fieldIndex;
        this.movementType = movementType;
    }

    @Override
    public void cardAction(Player player, GameBoard gameBoard) {
        switch (movementType) {
            case NUMBER -> {
                int newPos = (player.getCurrentPos() + fieldIndex) % gameBoard.fields.length;

                // Tjekker om den nye position er negativ.
                if (newPos < 0) {
                    newPos += gameBoard.fields.length;
                }
                player.setCurrentPos(newPos);
            }
            case INDEX -> {
                player.setCurrentPos(fieldIndex);
            }
            case NEAREST -> {
                int pos = player.getCurrentPos();
                int nearest = gameBoard.ferryIndices[0];
                int nearestDistance = nearest - pos;

                if (nearestDistance < 0) {
                    nearestDistance += gameBoard.fields.length;
                }

                for (int ferryIndex : gameBoard.ferryIndices) {
                    int distance = ferryIndex - pos;
                    if (distance < 0) {
                        distance += gameBoard.fields.length;
                    }

                    if (distance < nearestDistance) {
                        nearest = ferryIndex;
                        nearestDistance = distance;
                    }
                }
                // Vi har nu fundet den færge der er tættest på spillerens position (fremadgående)

                player.setCurrentPos(nearest);
            }
        }

        // Tjekker om spilleren passerede start
        if (player.getCurrentPos() < player.getPreviousPos())
            player.addAmountToBalance(GlobalValues.START_FIELD_VALUE);
    }
}
