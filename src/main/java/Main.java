// Version 2.0.0

import Controller.GameController;

//------------------------------------------------------------------------------------------------------------------
// GameController klassen instantierer objektet gameController. Dette objekt laver altså de nødvendige objekter,
// som ligger i GameController constructoren, for at kunne køre programmet. Fx En player, et spillerbræt og en terning.
//
// Metoden rungame() som ligger i GameController klassen bruger gameController objektet til at køre spillet.
//------------------------------------------------------------------------------------------------------------------
public class Main {
    public static void main(String[] args) {
        GameController gameController = new GameController();
        gameController.runGame();
    }
}
