package stub;

import Controller.ViewController;
import Model.ChanceCards.ChanceCard;
import Model.Player;

public class StubGUIController extends ViewController {

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
    public void setDie(int faceValue) {

    }

    @Override
    public String getUserButtonPressed(String msg, String... menuOptions) {
        return null;
    }

    @Override
    public void setOwner(Player player) {

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
}
