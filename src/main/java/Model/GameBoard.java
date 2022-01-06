package Model;

import Model.ChanceCards.ChanceCard;
import Model.ChanceCards.ChanceCard_Factory;
import Model.Fields.Field;
import Model.Fields.FieldFactory;
import Model.Fields.PropertyField;
import Model.Fields.ShippingField;

import java.awt.*;
import java.util.ArrayList;


public class GameBoard {
    public final ChanceCard[] chanceCards;
    public final Field[] fields; // Array af fields
    public final int[] ferryIndices;

    //------------------------------------------------------------------------------------------------------------------
    // Constructor der laver et GameBoard
    //------------------------------------------------------------------------------------------------------------------
    public GameBoard(Field[] fields, ChanceCard[] chanceCards) {
        this.fields = fields;
        this.chanceCards = chanceCards;

        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] instanceof ShippingField) {
                indices.add(i);
            }
        }
        this.ferryIndices = new int[indices.toArray().length];
        for (int i = 0; i < ferryIndices.length; i++) {
            ferryIndices[i] = indices.get(i);
        }
    }

    public GameBoard() {
        this(FieldFactory.createFields(), ChanceCard_Factory.createChanceCards());
    }

    /**
     * Bruger input til at finde det andet felt med samme farve, og returner et Field[] array.
     *
     * @param color Java.awt.Color input
     * @return Field[] output
     */
    public PropertyField[] getFieldGroup(Color color) {
        PropertyField[] tmpFields = new PropertyField[3];
        int counter = 0;

        for (Field field : fields) {
            if (field instanceof PropertyField propertyField) {
                if (field.backgroundColor.equals(color)) {
                    tmpFields[counter] = propertyField;
                    counter++;
                }
                if (counter == 3) {
                    break;
                }
            }
        }
        return tmpFields;
    }
}