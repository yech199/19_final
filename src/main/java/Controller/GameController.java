package Controller;

import Model.ChanceCards.ChanceCard;
import Model.Die;
import Model.Fields.AmusementField;
import Model.Fields.ChanceField;
import Model.Fields.Field;
import Model.GameBoard;
import Model.Player;

import java.util.Random;

public class GameController {
    public ViewController guiController;
    public Die die;
    public GameBoard gameBoard;
    public Player[] playerList;
    private boolean gameEnded;

    /**
     * Denne controller laver alle de objekter som spillet skal bruge for at kunne køre
     *   Fx en terning, et gameBoard og x antal spillere.
     *     Der laves et array med "playerCount" antal spillere. playerListen er af typen Player.
     *     Player constructoren skal bruge et navn i parameteren, desuden sættes den enkelte spillers start balance til 0 automatisk
     */
    public GameController(ViewController guiController, GameBoard gameBoard, Die die) {
        this.die = die;
        this.gameBoard = gameBoard;
        this.guiController = guiController;

        int playerCount = guiController.getUserInteger("Hvor mange spillere (3-6)?", 3, 6);

        // Laver x antal nye spillere med navn
        playerList = new Player[playerCount];
        for (int i = 0; i < playerList.length; i++) {
            playerList[i] = new Player(this.guiController.getUserString("Player " + (i + 1) + " skriv dit navn:"));
        }

        this.guiController.setUpPlayers(playerList);
    }

    /**
     * Denne constructor sætter default terning og GUIController
     * Dette er et mellemled mellem de to andre constructors, fordi this skal stå først i constructeren.
     * <p> Denne constructor kalder på ovenstående constructor
     */
    private GameController(GameBoard gameBoard) {
        this(new GUIController(gameBoard.fields), gameBoard, new Die(1, 6));
    }

    /**
     * Denne constructor sætter default terning, GUIController og GameBoard.
     * <p>
     * Denne constructor kalder på ovenstående constructor
     */
    public GameController() {
        this(new GameBoard());
    }

    /**
     * Kører spillet indtil spillet slutter (Dette sker hvis en player får en balance under nul -  Se if statement i playTurn)
     */
    public void runGame() {
        while (!gameEnded) {
            playRound();
        }
        setGameEnded();
    }

    /**
     * Spiller en runde for hver spiller i player listen
     */
    public void playRound() {
        for (Player player : playerList) {
            playTurn(player);
            if (gameEnded) {
                break;
            }
        }
    }

    /**
     * Logik til den enkeltes spillers tur
     */
    public void playTurn(Player player) {
        guiController.getUserButtonPressed(player.name + " skal rulle med terningen!", "Rul");
        int faceValue = die.roll();
        guiController.setDie(faceValue);

        if (player.inJail) {
            if (player.getOutOfJailFree) {
                player.getOutOfJailFree = false;
            }
            else player.addAmountToBalance(-1);
        }
        movePlayerForward(player, faceValue);
        Field landedOn = gameBoard.fields[player.getCurrentPos()];
        landedOn.fieldAction(player);

        if (landedOn instanceof AmusementField amusementField) {

            if (amusementField.owner == null) {
                // Køb felt og ændr farve
                player.addAmountToBalance(-amusementField.rent);
                amusementField.owner = player;
                guiController.setOwner(player);

                //------------------------------------------------------------------------------------------------------
                // Tjekker om ejeren af det nyligt købte felt også ejer det andet af samme farve
                //------------------------------------------------------------------------------------------------------
                if (ownsBoth(amusementField)) {

                    AmusementField[] tmpFields = gameBoard.getPair(amusementField.backgroundColor);
                    tmpFields[0].rent += tmpFields[0].rent;
                    tmpFields[1].rent += tmpFields[1].rent;
                    // Tilføjer renten til sig selv for at fordoble den.
                    // OBS!! Denne metode er kun brugbar når ejeren ikke kan ændres.
                }
            }
            else guiController.updatePlayer(amusementField.owner);
        }
        else if (landedOn instanceof ChanceField) {
            ChanceCard chanceCard = drawChanceCard();
            guiController.displayChanceCard(chanceCard);
            chanceCard.cardAction(player, gameBoard);
        }

        guiController.updatePlayer(player);

        //--------------------------------------------------------------------------------------------------------------
        // Tjekker om den aktive spillers balance er under nul. Er balance under nul slutter spillet
        //--------------------------------------------------------------------------------------------------------------
        if (player.getBalance() <= 0) {
            gameEnded = true;
        }
    }

    /**
     * Rykker spilleren frem og sørger for at man kan gå i ring på brættet vha. Modulos
     * Modulus tjekker om currentPos bliver højere end antallet af felter, og fjerner antallet af felter i dette tilfælde.
     * (Feltet vil altid være mindre end længden af brættet)
     *
     * @param player    Den aktive player
     * @param faceValue terningens faceValue
     */
    public void movePlayerForward(Player player, int faceValue) {
        player.setCurrentPos((player.getCurrentPos() + faceValue) % gameBoard.fields.length);

        // Spilleren passerer Start
        if (player.getCurrentPos() < player.getPreviousPos()) player.addAmountToBalance(2);
        guiController.updatePlayer(player);
    }

    /**
     * Bruger getPair for at få det andet felt med samme farve.
     *
     * @param field AmusementField input
     * @return boolean output der siger om ejeren har begge felter med denne farve eller ej.
     */
    public boolean ownsBoth(AmusementField field) {

        Field[] tmpFields = gameBoard.getPair(field.backgroundColor);

        //--------------------------------------------------------------------------------------------------------------
        // Tjekker om nogle af felterne ikke har en ejer, da dette er nødvendigt for at kunne sammenligne i
        // return statementet.
        //--------------------------------------------------------------------------------------------------------------
        if (((AmusementField) tmpFields[0]).owner == null || ((AmusementField) tmpFields[1]).owner == null) {
            return false;
        }

        return ((AmusementField) tmpFields[0]).owner == ((AmusementField) tmpFields[1]).owner;
    }

    /**
     * Tjekker hvilken spiller der har vundet spillet.
     * Giver en slut-besked med spillernes endelige scorer.
     */
    public void setGameEnded() {
        String winner = getWinner(playerList).name;

        if (playerList.length == 2) {
            guiController.showMessage("Spillet er slut!\n" +
                    playerList[0].name + " har " + playerList[0].getBalance() + " point.\n" +
                    playerList[1].name + " har " + playerList[1].getBalance() + " point.\n" +
                    winner + " har vundet!");
        }
        else if (playerList.length == 3) {
            guiController.showMessage("Spillet er slut!\n" +
                    playerList[0].name + " har " + playerList[0].getBalance() + " point.\n" +
                    playerList[1].name + " har " + playerList[1].getBalance() + " point.\n" +
                    playerList[2].name + " har " + playerList[2].getBalance() + " point.\n" +
                    winner + " har vundet!");
        }
        else guiController.showMessage("Spillet er slut!\n" +
                    playerList[0].name + " har " + playerList[0].getBalance() + " point.\n" +
                    playerList[1].name + " har " + playerList[1].getBalance() + " point.\n" +
                    playerList[2].name + " har " + playerList[2].getBalance() + " point.\n" +
                    playerList[3].name + " har " + playerList[3].getBalance() + " point.\n" +
                    winner + " har vundet!");

        guiController.showMessage("Luk spillet?");
        guiController.close();
    }

    /**
     * Tjekker hver spiller mod hinanden. Hvis den nuværende spiller har en lavere score end den man tjekker mod,
     * bliver de til den nye vinder.
     *
     * @param playerList tager listen af players som input
     * @return returnerer den spiller der har vundet
     */
    public Player getWinner(Player[] playerList) {
        Player winner = new Player(""); //tom spiller, da den udskiftes med en ny spiller efter første runde i for-loop
        for (Player player : playerList) {
            if (winner.getBalance() < player.getBalance()) {
                winner = player;
            }
        }
        return winner;
    }

    public ChanceCard drawChanceCard() {
        int rng = new Random().nextInt(gameBoard.chanceCards.length);
        return gameBoard.chanceCards[rng];
    }
}