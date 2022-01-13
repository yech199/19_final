package View;

import Model.ChanceCards.ChanceCard;
import Model.Player;

public abstract class GameView {
    public abstract int getUserInteger(String msg, int min, int max);

    /**
     * Et array af 6 farver laves da der kan være max 6 spillere.<br>
     * Hver spiller kan vælge hvilken brik de vil bruge på brættet.<br>
     * Hver spillers brik sættes til en farve fra arrayet af colors.<br>
     * Der laves et array med gui_players der hver har et navn, en balance og en bil.<br>
     * Tilføjer spillerne i arrayet med gui_players til GUI'en.<br>
     * Alle spillernes biler sættes på index[0] = Start<br>
     * Alle spillernes biler vises på index[0] = Start<br>
     *
     * @param playerList et array med playerne
     */
    public abstract void setUpPlayers(Player[] playerList);

    /**
     * Viser en besked til brugeren, og beder brugeren om et tekst-input.<br>
     * Denne besked vises indtil et input er indtastet.
     *
     * @param msg Beskeden der vises i UI
     * @return Den String som brugeren indtaster
     */
    public abstract String getUserString(String msg);

    /**
     * Opdaterer den visuelle spiller på brættet.<p>
     *      1) Vi laver en guiPlayer vha. getGUIversion metoden <br>
     *      2) Bilen på brættet slettes fra sin forrige position (før terningekast) <br>
     *      3) Sætter bilen på currentPos (beregnes ud fra terningekast). Når man lander på et felt sker der en fieldAction <br>
     *      4) Opdaterer spillerens balance på UI
     *
     * @param player den aktive player
     */
    public abstract void updatePlayer(Player player);

    public abstract void updatePlayerBalance(Player player);

    public abstract void removeCar(Player player);

    /**
     * Viser to terninger med givne værdier, på en bestemt position af spillebrættet
     *
     * @param faceValue1 The value of the die. If the value is not between
     *                  1-6, the die won't be updated.
     */
    public abstract void setDice(int faceValue1, int x1, int y1, int faceValue2, int x2, int y2);

    /**
     * Viser en besked og beder brugeren om at trykke på en af de viste knapper. <br>
     * Knapperne er defineres af Strings givet som 'buttons' parameters.
     *
     * @param msg         Besked der beder brugeren om at trykke på en af de valgte knapper
     * @param menuOptions Beskeden der vises PÅ knappen
     * @return En String fra den knap brugeren trykkede på.
     */
    public abstract String getUserButtonPressed(String msg, String... menuOptions);

    /**
     * Giver feltet en farve rundt om feltet, som er samme farve som den spiller der køber og dermed ejer feltet
     *
     * @param player Den spiller der køber feltet
     */
    public abstract void setOwner(Player player, int index);

    public abstract void viewAsMortgaged(Player player, int index);

    public abstract void removeOwner(int index);

    /**
     * Viser en besked til brugeren sammen med en 'OK'-knap. <br>
     * Programmet kører først videre når knappen er blevet trykket på.
     *
     * @param msg The message to print
     */
    public abstract void showMessage(String msg);

    /**
     * Lukker UI, og stopper tråden den kører på.
     */
    public abstract void close();

    public abstract void displayChanceCard(ChanceCard chanceCard);

    public abstract void updateAmountOfHouses(int houseCount, int index);

    public abstract void setOrRemoveHotel(boolean hotelStatus, int index);
}
