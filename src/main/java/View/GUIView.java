package View;

import Model.ChanceCards.ChanceCard;
import Model.Fields.Field;
import Model.Player;
import gui_fields.*;
import gui_main.GUI;

import java.awt.*;

/**
 * Laver alt der har noget med GUI'en at gøre. Ikke fx spilleren som ligger i Player klassen, kun GUI_Player
 */
public class GUIView extends GameView {

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
    public GUIView(Field[] fields) {
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

    @Override
    public void setUpPlayers(Player[] playerList) {
        Color[] colors = {Color.BLUE, Color.RED, Color.YELLOW, Color.PINK, Color.lightGray, Color.green};
        gui_players = new GUI_Player[playerList.length];

        //--------------------------------------------------------------------------------------------------------------
        // Sætter spillernes startbalance alt efter hvor mange spillere der spiller
        //--------------------------------------------------------------------------------------------------------------
        for (int i = 0; i < playerList.length; i++) {
            String car = "Bil", tractor = "Traktor", racecar = "Racerbil", ufo = "UFO";
            String choice = getUserButtonPressed("Hvilken brik vil " + playerList[i].name + " bruge?",
                    car, tractor, racecar, ufo);

            GUI_Car tmpCar = switch (choice) {
                case "Bil" -> new GUI_Car(colors[i], colors[i], GUI_Car.Type.CAR, gui_fields.GUI_Car.Pattern.FILL);
                case "Traktor" -> new GUI_Car(colors[i], colors[i], GUI_Car.Type.TRACTOR, gui_fields.GUI_Car.Pattern.FILL);
                case "Racerbil" -> new GUI_Car(colors[i], colors[i], GUI_Car.Type.RACECAR, gui_fields.GUI_Car.Pattern.FILL);
                case "UFO" -> new GUI_Car(colors[i], colors[i], GUI_Car.Type.UFO, gui_fields.GUI_Car.Pattern.FILL);
                default -> null;
            };

            assert tmpCar != null;

            gui_players[i] = new GUI_Player(playerList[i].name, playerList[i].getBalance(), tmpCar);
            gui.addPlayer(gui_players[i]);

            gui_fieldArray[0].setCar(gui_players[i], true);
        }
    }

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
     * Opdaterer den visuelle spiller på brættet.<p>
     * 1) Vi laver en guiPlayer vha. getGUIversion metoden <br>
     * 2) Bilen på brættet slettes fra sin forrige position (før terningekast) <br>
     * 3) Sætter bilen på currentPos (beregnes ud fra terningekast). Når man lander på et felt sker der en fieldAction <br>
     * 4) Opdaterer spillerens balance på UI
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
    public void updatePlayerBalance(Player player) {
        GUI_Player guiPlayer = getGuiVersion(player);
        guiPlayer.setBalance(player.getBalance());
    }

    @Override
    public void removeCar(Player player) {
        GUI_Player guiPlayer = getGuiVersion(player);
        gui_fieldArray[player.getCurrentPos()].setCar(guiPlayer, false);
    }

    @Override
    public void setDice(int faceValue1, int x1, int y1, int faceValue2, int x2, int y2) {
        gui.setDice(faceValue1, x1, y1, faceValue2, x2, y2);
    }

    @Override
    public String getUserButtonPressed(String msg, String... menuOptions) {
        return gui.getUserButtonPressed(msg, menuOptions);
    }

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

    @Override
    public void showMessage(String msg) {
        gui.showMessage(msg);
    }

    @Override
    public void close() {
        gui.close();
    }

    @Override
    public void displayChanceCard(ChanceCard chanceCard) {
        gui.displayChanceCard(chanceCard.cardText);
    }

    @Override
    public void setHouses(int houseCount, int index) {
        ((GUI_Street) gui_fieldArray[index]).setHouses(houseCount);
    }

    public void setOrRemoveHotel(boolean hotelStatus, int index) {
        ((GUI_Street) gui_fieldArray[index]).setHouses(0);
        ((GUI_Street) gui_fieldArray[index]).setHotel(hotelStatus);
    }

}