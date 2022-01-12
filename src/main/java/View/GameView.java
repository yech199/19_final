package View;

import Model.ChanceCards.ChanceCard;
import Model.Fields.PropertyField;
import Model.Player;

public abstract class GameView {
    public abstract int getUserInteger(String msg, int min, int max);

    public abstract void setUpPlayers(Player[] playerList);

    public abstract String getUserString(String msg);

    public abstract void updatePlayer(Player player);

    public abstract void updatePlayerBalance(Player player);

    public abstract void removeCar(Player player);

    public abstract void setDice(int faceValue1, int x1, int y1, int faceValue2, int x2, int y2);

    public abstract String getUserButtonPressed(String msg, String... menuOptions);

    public abstract void setOwner(Player player, int index);

    public abstract void removeOwner(int index);

    public abstract void showMessage(String msg);

    public abstract void close();

    public abstract void displayChanceCard(ChanceCard chanceCard);

    public abstract void setHouses(int houseCount, int index);

    public abstract void setOrRemoveHotel(boolean hotelStatus, int index);
}