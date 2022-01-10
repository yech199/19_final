package stub;

import Controller.ViewController;
import Model.ChanceCards.ChanceCard;
import Model.Fields.PropertyField;
import Model.Player;

public class StubGUIController extends ViewController {
    public String customChoice = null;

    @Override
    public int getUserInteger(String msg, int min, int max) {
        return 0;
    }

    @Override
    public void setUpPlayers(Player[] playerList) {
    }

    @Override
    public String getUserString(String msg) {
        return null;
    }

    @Override
    public void updatePlayer(Player player) {
    }

    @Override
    public void removeCar(Player player) {
    }

    @Override
    public void setDice(int faceValue1, int x1, int y1, int faceValue2, int x2, int y2) {}

    @Override
    public String getUserButtonPressed(String msg, String... menuOptions) {
        return customChoice == null ? menuOptions[0] : customChoice;
    }

    @Override
    public void setOwner(Player player) {
    }

    @Override
    public void removeOwner(int index) {
    }

    @Override
    public void showMessage(String msg) {
    }

    @Override
    public void close() {
    }

    @Override
    public void displayChanceCard(ChanceCard chanceCard) {
    }

    @Override
    public void setHouses(int houseCount, PropertyField propertyField){

    }
}
