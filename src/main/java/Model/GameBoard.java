package Model;

import Model.ChanceCards.ChanceCard;
import Model.ChanceCards.ChanceCard_Factory;
import Model.Fields.PropertyField;
import Model.Fields.Field;
import Model.Fields.FieldFactory;

import java.awt.*;


public class GameBoard {
    public final ChanceCard[] chanceCards;
    public final Field[] fields; // Array af fields

    //------------------------------------------------------------------------------------------------------------------
    // Constructor der laver et GameBoard
    //------------------------------------------------------------------------------------------------------------------
    public GameBoard() {
        this.fields = FieldFactory.createFields(); // Makes a new array using Field abstract class from Model package
        this.chanceCards = ChanceCard_Factory.createChanceCards();
    }

    public GameBoard(Field[] fields, ChanceCard[] chanceCards) {
        this.fields = fields;
        this.chanceCards = chanceCards;
    }

    /**
     * Bruger input til at finde det andet felt med samme farve, og returner et Field[] array.
     *
     * @param color Java.awt.Color input
     * @return Field[] output
     */
    public PropertyField[] getPair(Color color) {
        PropertyField[] tmpFields = new PropertyField[2];
        int counter = 0;

        for (Field field : fields) {
            if (field instanceof PropertyField propertyField) {
                if (field.backgroundColor.equals(color)) {
                    tmpFields[counter++] = propertyField;
                }
                if (counter == 2) break;
            }
        }
        return tmpFields;
    }
}