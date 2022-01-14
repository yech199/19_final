package stub;

import View.GameView;
import Model.ChanceCards.ChanceCard;
import Model.Player;

import java.util.Random;

public class StubView extends GameView {
    public String customChoice = null;
    public int customNumber = 0;

    @Override
    public int getUserInteger(String msg, int min, int max) {
        return customNumber == 0 ? max : customNumber;
    }

    @Override
    public void setUpPlayers(Player[] playerList) {}

    @Override
    public String getUserString(String msg) {
        byte[] array = new byte[3];
        new Random().nextBytes(array);
        return new String(array);
    }

    @Override
    public void updatePlayer(Player player) {}

    @Override
    public void updatePlayerBalance(Player player) {}

    @Override
    public void removeCar(Player player) {}

    @Override
    public void setDice(int faceValue1, int x1, int y1, int faceValue2, int x2, int y2) {}

    @Override
    public String getUserButtonPressed(String msg, String... menuOptions) {
        return customChoice == null ? menuOptions[0] : customChoice;
    }

    @Override
    public void setOwner(Player player, int index) {}

    @Override
    public void viewAsMortgaged(Player player, int index) {}

    @Override
    public void removeOwner(int index) {}

    @Override
    public void showMessage(String msg) {}

    @Override
    public void close() {}

    @Override
    public void displayChanceCard(ChanceCard chanceCard) {}

    @Override
    public void updateAmountOfHouses(int houseCount, int index){}

    @Override
    public void setOrRemoveHotel(boolean hotelStatus, int index){}
}
