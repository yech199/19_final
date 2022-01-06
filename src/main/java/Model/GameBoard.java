package Model;

import Model.ChanceCards.ChanceCard;
import Model.ChanceCards.ChanceCard_Factory;
import Model.Fields.*;

import java.awt.*;


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

        int[] indices = new int[]{};

        ferryIndices = findAllShippingFields(fields, indices);
    }

    public GameBoard() {
        this(FieldFactory.createFields(), ChanceCard_Factory.createChanceCards());
    }

    /**
     * Finder alle PropertyFields med samme farve, og returner et PropertyField[] array.
     *
     * @param color Den farve felter vi gerne vil finde
     * @return Field[] output
     */
    public PropertyField[] findAllPropertyFieldsOfSameColor(Color color) {
        PropertyField[] tmpFields = new PropertyField[2];
        int counter = 0;

        for (Field field : this.fields) {
            if (field instanceof PropertyField propertyField) {
                if (propertyField.backgroundColor.equals(color) && counter != 2) {
                    tmpFields[counter] = propertyField;
                    counter++;
                }
                else if (counter == 2) {
                    if (propertyField != tmpFields[0] && propertyField != tmpFields[1] && propertyField.backgroundColor.equals(color))
                        tmpFields = addX(tmpFields.length, tmpFields, propertyField);
                }
            }
        }
        return tmpFields;
    }

    public int[] findAllShippingFields(Field[] fields, int[] indices) {
        final int[] ferryIndices;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] instanceof ShippingField) {
                indices = addX(indices.length, indices, i);
            }
        }

        ferryIndices = new int[indices.length];
        for (int i = 0; i < ferryIndices.length; i++) {
            ferryIndices[i] = indices[i];
        }
        return ferryIndices;
    }

    /**
     * Funktion der tilføjer x til arrayet på plads n + 1
     *
     * @param n              Antallet af elementer i det gamle array
     * @param propertyFields Det gamle array
     * @param propertyField  Det der skal tilføjes i arrayet på plads n + 1
     * @return Det gamle array med et ekstra element
     */
    public static PropertyField[] addX(int n, PropertyField[] propertyFields, PropertyField propertyField) {
        int i;
        PropertyField[] newArray = new PropertyField[n + 1];

        //--------------------------------------------------------------------------------------------------------------
        // Indsætter det gamle array i det nye array
        //--------------------------------------------------------------------------------------------------------------
        for (i = 0; i < n; i++)
            newArray[i] = propertyFields[i];

        newArray[n] = propertyField;
        return newArray;
    }

    public static int[] addX(int n, int[] propertyFields, int propertyField) {
        int i;
        int[] newArray = new int[n + 1];

        //--------------------------------------------------------------------------------------------------------------
        // Indsætter det gamle array i det nye array
        //--------------------------------------------------------------------------------------------------------------
        for (i = 0; i < n; i++)
            newArray[i] = propertyFields[i];

        newArray[n] = propertyField;
        return newArray;
    }
}