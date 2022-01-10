package Controller;

import Model.ChanceCards.ChanceCard;
import Model.Fields.Field;
import Model.Fields.OwnableField;
import Model.Fields.PropertyField;
import Model.Player;
import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Ownable;
import gui_fields.GUI_Player;
import gui_main.GUI;
import gui_fields.GUI_Street;

import java.awt.*;

/**
 * Laver alt der har noget med GUI'en at gøre. Ikke fx spilleren som ligger i Player klassen, kun GUI_Player
 */
public class GUIController extends ViewController {
    GUI_Field[] gui_fieldArray;
    GUI gui;
    GUI_Player[] gui_players;

    /**
     * Constructor der laver GUI'en.
     *      Field arrayet laves om til en GUI version af felt arrayet, som da har typen GUI_Field.
     *      Der laves til slut en GUI vha. GUI versionen er felterne
     *
     * @param fields Arrayet af typen fields er allerede lavet i gameBoard klassen --> gameBoard.fields
     */
    public GUIController(Field[] fields) {
        gui_fieldArray = convertFieldArray(fields);
        gui = new GUI(gui_fieldArray, new Color(222, 184, 135));
    }

    /**
     * Displays a message to the user and awaits the integer response. Only values between min and max are allowed.
     *
     * @param msg The message that promts the user.
     * @param min The minimum value the user is allowed to enter.
     * @param max The maximum value the user is allowed to enter.
     * @return The integer that the user selected.
     */
    public int getUserInteger(String msg, int min, int max) {
        return gui.getUserInteger(msg, min, max);
    }

    /**
     * Laver et array af felterne som GUI'et kan bruge
     *
     * @param fields Arrayet af typen fields er allerede lavet i gameBoard klassen --> gameBoard.fields
     * @return Et array med GUI versioner af felterne
     */
    private static GUI_Field[] convertFieldArray(Field[] fields) {
        GUI_Field[] gui_fieldArray = new GUI_Field[fields.length]; // Makes a new array using the GUI_Field class from library
        for (int i = 0; i < fields.length; i++) {
            gui_fieldArray[i] = fields[i].getGUIversion();
        } // Puts field values (GUI version) into the fields in "gui_fieldArray" through their index number 0-23
        return gui_fieldArray;
    }

    /**
     * Et array af 4 farver laves da der kan være max 4 spillere.
     * Startbalance sættes alt efter hvor mange spillere der spiller.
     * Hver spillers bil sættes til en farve fra arrayet af colors
     * Der laves et array med gui_players der hver har et navn, en balance og en bil
     * Tilføjer spillerne i arrayet med gui_players til GUI'en
     * Alle spillernes biler sættes på index[0] = Start
     * Alle spillernes biler vises på index[0] = Start
     *
     * @param playerList et array med playerne
     */
    @Override
    public void setUpPlayers(Player[] playerList) {
        Color[] colors = {Color.BLUE, Color.RED, Color.YELLOW, Color.PINK, Color.lightGray, Color.green};
        gui_players = new GUI_Player[playerList.length];

        //--------------------------------------------------------------------------------------------------------------
        // Sætter spillernes startbalance alt efter hvor mange spillere der spiller
        //--------------------------------------------------------------------------------------------------------------
        for (int i = 0; i < playerList.length; i++) {
            GUI_Car tmpCar = new GUI_Car();
            tmpCar.setPrimaryColor(colors[i]);

            gui_players[i] = new GUI_Player(playerList[i].name, playerList[i].getBalance(), tmpCar);
            gui.addPlayer(gui_players[i]);

            gui_fieldArray[0].setCar(gui_players[i], true);
        }
    }

    /**
     * Displays a message to the user, and prompt the user for a text input.
     * Blocks/hangs until an input has been entered.
     *
     * @param msg The message that prompts the user.
     * @return The string that the user has entered.
     */
    @Override
    public String getUserString(String msg) {
        return gui.getUserString(msg);
    }

    /**
     * Laver en GUI version af en player
     *
     * @param player den player man vil lave en GUI_version af
     * @return gui_versionen af en player
     */
    private GUI_Player getGuiVersion(Player player) {
        GUI_Player guiPlayer = null;

        //--------------------------------------------------------------------------------------------------------------
        // For hver GUI_Player p i arrayet gui_players sker følgende
        //      TODO: Forklar bedre!
        //       "Hvis GUI_player p's navn er det samme som navnet af player den player man vil lave en gui version af.
        //       sættes guiPlayer (som ellers er null) lig med p"
        //--------------------------------------------------------------------------------------------------------------
        for (GUI_Player p : gui_players) {
            if (p.getName().equals(player.name)) {
                guiPlayer = p;
                break;
            }
        }
        return guiPlayer;
    }

    /**
     * Opdaterer den visuelle spiller på brættet.
     *      1) Vi laver en guiPlayer vha. getGUIversion metoden
     *      2) Bilen på brættet slettes fra sin forrige position (før terningekast)
     *      3) Sætter bilen på currentPos (beregnes ud fra terningekast). Når man lander på et felt sker der en fieldAction
     *      4) Lander man fx på et AmusementField der ikke er ejet, købes denne ejendom automatisk.
     *         Spillerens balance bliver altså opdateret
     *
     * @param player den aktive player
     */
    @Override
    public void updatePlayer(Player player) {
        GUI_Player guiPlayer = getGuiVersion(player);
        gui_fieldArray[player.getPreviousPos()].setCar(guiPlayer, false);
        gui_fieldArray[player.getCurrentPos()].setCar(guiPlayer, true);

        //------------------------------------------------------------------------------------------------------------------
        // nice to have: for loop der flytter bilen 1 felt ad gangen og venter mellem hvert flyt
        // indtil alle felter er flyttet.
        //------------------------------------------------------------------------------------------------------------------
        guiPlayer.setBalance(player.getBalance());
    }

    @Override
    public void removeCar(Player player) {
        GUI_Player guiPlayer = getGuiVersion(player);
        // gui_fieldArray[player.getPreviousPos()].setCar(guiPlayer, false);
        gui_fieldArray[player.getCurrentPos()].setCar(guiPlayer, false);
    }

    /**
     * Displays one die with the given value, at a random position on the board
     *
     * @param faceValue1 The value of the die. If the value is not between
     *                  1-6, the die won't be updated.
     */
    @Override
    public void setDice(int faceValue1, int x1, int y1, int faceValue2, int x2, int y2) {
        gui.setDice(faceValue1, x1, y1, faceValue2, x2, y2);
    }

    /**
     * Displays a message and prompt the user for a button press of a series of buttons.
     * The buttons are defined by the number of strings passed as the 'buttons' parameters.
     *
     * @param msg         The message that prompts the user to press the buttons
     * @param menuOptions The message is displayed ON the button
     * @return The string from the button that the user pressed.
     */
    @Override
    public String getUserButtonPressed(String msg, String... menuOptions) {
        return gui.getUserButtonPressed(msg, menuOptions);
    }

    /**
     * Giver feltet en farve rundt om feltet, som er samme farve som den spiller der køber og dermed ejer feltet
     *
     * @param player active player
     */
    @Override
    public void setOwner(Player player, int index) {
        GUI_Player guiPlayer = getGuiVersion(player);
        GUI_Ownable playerField = ((GUI_Ownable) gui_fieldArray[index]);
        playerField.setBorder(guiPlayer.getCar().getPrimaryColor());
        playerField.setOwnerName(player.name);
    }

    @Override
    public void removeOwner(int index) {
        GUI_Ownable playerField = ((GUI_Ownable) gui_fieldArray[index]);
        playerField.setBorder(null);
        playerField.setOwnerName(null);
    }

    /**
     * Displays a message to the user, along with an 'OK'-button.
     * The program stops/hangs at the method call until the button is pressed.
     *
     * @param msg The message to print
     */
    @Override
    public void showMessage(String msg) {
        gui.showMessage(msg);
    }

    /**
     * Closes the GUI window, and stops the thread it runs on.
     */
    @Override
    public void close() {
        gui.close();
    }


    @Override
    public void displayChanceCard(ChanceCard chanceCard) {
        gui.displayChanceCard(chanceCard.cardText);
    }

    @Override
    public void setHouses(int houseCount, PropertyField propertyField){
        propertyField.getGUIversion().setHouses(houseCount);
    }
}