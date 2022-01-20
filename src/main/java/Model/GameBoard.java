package Model;

import Model.ChanceCards.ChanceCard;
import Model.ChanceCards.ChanceCard_Factory;
import Model.Fields.*;

import java.awt.*;

public class GameBoard {
    public enum FieldType {
        OWNABLE,
        SHIPPING,
        BREWERY
    }

    public final ChanceCard[] chanceCards;
    public final Field[] fields; // Array af fields
    public final int[] ownableIndices;
    public final int[] ferryIndices;
    public final int[] breweryIndices;
    public final ShippingField[] shippingFields;
    public final BreweryField[] breweryFields;


    //------------------------------------------------------------------------------------------------------------------
    // Constructor der laver et GameBoard
    //------------------------------------------------------------------------------------------------------------------
    public GameBoard(Field[] fields, ChanceCard[] chanceCards) {
        this.fields = fields;
        this.chanceCards = chanceCards;

        ownableIndices = findAllOwnableFieldIndices(fields, FieldType.OWNABLE);
        ferryIndices = findAllOwnableFieldIndices(fields, FieldType.SHIPPING);
        breweryIndices = findAllOwnableFieldIndices(fields, FieldType.BREWERY);

        shippingFields = findAllShippingFields();
        breweryFields = findAllBreweryFields();
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
                        tmpFields = addToOldArray(tmpFields, propertyField);
                }
            }
        }
        return tmpFields;
    }

    public ShippingField[] findAllShippingFields() {
        ShippingField[] tmpFields = new ShippingField[]{};

        for (Field field : this.fields) {
            if (field instanceof ShippingField shippingField) {
                tmpFields = addToOldArray(tmpFields, shippingField);
            }
        }
        return tmpFields;
    }

    public BreweryField[] findAllBreweryFields() {
        BreweryField[] tmpFields = new BreweryField[]{};

        for (Field field : this.fields) {
            if (field instanceof BreweryField breweryField) {
                tmpFields = addToOldArray(tmpFields, breweryField);
            }
        }
        return tmpFields;
    }

    public int[] findAllOwnableFieldIndices(Field[] fields, FieldType fieldType) {
        int[] ownableIndices = new int[]{};
        int[] ferryIndices = new int[]{};
        int[] breweryIndices = new int[]{};

        for (int i = 0; i < fields.length; i++) {
            if (fields[i] instanceof OwnableField)
                ownableIndices = addToOldArray(ownableIndices, i);
            {
                if (fields[i] instanceof ShippingField) {
                    ferryIndices = addToOldArray(ferryIndices, i);
                }
                else if (fields[i] instanceof BreweryField) {
                    breweryIndices = addToOldArray(breweryIndices, i);
                }
            }
        }

        switch (fieldType) {
            case OWNABLE -> {
                return ownableIndices;
            }
            case SHIPPING -> {
                return ferryIndices;
            }
            case BREWERY -> {
                return breweryIndices;
            }
        }
        // Denne case sker aldrig        
        return new int[]{};
    }

    /**
     * Funktion der tilføjer x til arrayet på plads n + 1
     *
     * @param oldArray   Det gamle array
     * @param newElement Det der skal tilføjes i arrayet på plads n + 1
     * @return Det gamle array med et ekstra element
     */
    private static PropertyField[] addToOldArray(PropertyField[] oldArray, PropertyField newElement) {
        int n = oldArray.length;
        PropertyField[] newArray = new PropertyField[n + 1];

        //--------------------------------------------------------------------------------------------------------------
        // Indsætter det gamle array i det nye array
        //--------------------------------------------------------------------------------------------------------------
        for (int i = 0; i < n; i++)
            newArray[i] = oldArray[i];

        newArray[n] = newElement;
        return newArray;
    }

    private static ShippingField[] addToOldArray(ShippingField[] oldArray, ShippingField newElement) {
        int n = oldArray.length;
        ShippingField[] newArray = new ShippingField[n + 1];

        //--------------------------------------------------------------------------------------------------------------
        // Indsætter det gamle array i det nye array
        //--------------------------------------------------------------------------------------------------------------
        for (int i = 0; i < n; i++)
            newArray[i] = oldArray[i];

        newArray[n] = newElement;
        return newArray;
    }

    private static BreweryField[] addToOldArray(BreweryField[] oldArray, BreweryField newElement) {
        int n = oldArray.length;
        BreweryField[] newArray = new BreweryField[n + 1];

        //--------------------------------------------------------------------------------------------------------------
        // Indsætter det gamle array i det nye array
        //--------------------------------------------------------------------------------------------------------------
        for (int i = 0; i < n; i++)
            newArray[i] = oldArray[i];

        newArray[n] = newElement;
        return newArray;
    }

    /**
     * Funktion der tilføjer x til arrayet på plads n + 1
     *
     * @param oldArray   Det gamle array
     * @param newElement Det der skal tilføjes i arrayet på plads n + 1
     * @return Det gamle array med et ekstra element
     */
    private static int[] addToOldArray(int[] oldArray, int newElement) {
        int n = oldArray.length;
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