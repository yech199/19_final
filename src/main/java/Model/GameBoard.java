package Model;

import Model.ChanceCards.ChanceCard;
import Model.ChanceCards.ChanceCard_Factory;
import Model.Fields.*;

import java.awt.*;

public class GameBoard {
    public enum FieldType {
        SHIPPING,
        BREWERY
    }

    public final ChanceCard[] chanceCards;
    public final Field[] fields; // Array af fields
    public final int[] ferryIndices;
    public final int[] breweryIndices;

    //------------------------------------------------------------------------------------------------------------------
    // Constructor der laver et GameBoard
    //------------------------------------------------------------------------------------------------------------------
    public GameBoard(Field[] fields, ChanceCard[] chanceCards) {
        this.fields = fields;
        this.chanceCards = chanceCards;

        int[] indices = new int[]{};
        FieldType fieldType;

        fieldType = FieldType.SHIPPING;
        ferryIndices = findAllShippingAndBreweryFields(fields, indices, fieldType);

        fieldType = FieldType.BREWERY;
        breweryIndices = findAllShippingAndBreweryFields(fields, indices, fieldType);
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
                        tmpFields = addElementToOldArray(tmpFields.length, tmpFields, propertyField);
                }
            }
        }
        return tmpFields;
    }

    public int[] findAllShippingAndBreweryFields(Field[] fields, int[] indices, FieldType fieldType) {
        int[] ferryIndices = indices;
        int[] breweryIndices = indices;

        int[] fieldIndices;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] instanceof ShippingField) {
                ferryIndices = addElementToOldArray(ferryIndices.length, ferryIndices, i);
            }
            else if (fields[i] instanceof BreweryField) {
                breweryIndices = addElementToOldArray(breweryIndices.length, breweryIndices, i);
            }
        }

        switch (fieldType) {
            case SHIPPING -> indices = ferryIndices;
            case BREWERY -> indices = breweryIndices;
        }
        fieldIndices = new int[indices.length];
        for (int i = 0; i < fieldIndices.length; i++) {
            fieldIndices[i] = indices[i];
        }
        return fieldIndices;
    }

    /**
     * Funktion der tilføjer x til arrayet på plads n + 1
     *
     * @param n              Antallet af elementer i det gamle array
     * @param oldArray Det gamle array
     * @param newElement  Det der skal tilføjes i arrayet på plads n + 1
     * @return Det gamle array med et ekstra element
     */
    private static PropertyField[] addElementToOldArray(int n, PropertyField[] oldArray, PropertyField newElement) {
        int i;
        PropertyField[] newArray = new PropertyField[n + 1];

        //--------------------------------------------------------------------------------------------------------------
        // Indsætter det gamle array i det nye array
        //--------------------------------------------------------------------------------------------------------------
        for (i = 0; i < n; i++)
            newArray[i] = oldArray[i];

        newArray[n] = newElement;
        return newArray;
    }

    /**
     * Funktion der tilføjer x til arrayet på plads n + 1
     *
     * @param n          Antallet af elementer i det gamle array
     * @param oldArray   Det gamle array
     * @param newElement Det der skal tilføjes i arrayet på plads n + 1
     * @return Det gamle array med et ekstra element
     */
    private static int[] addElementToOldArray(int n, int[] oldArray, int newElement) {
        int i;
        int[] newArray = new int[n + 1];

        //--------------------------------------------------------------------------------------------------------------
        // Indsætter det gamle array i det nye array
        //--------------------------------------------------------------------------------------------------------------
        for (i = 0; i < n; i++)
            newArray[i] = oldArray[i];

        newArray[n] = newElement;
        return newArray;
    }
}