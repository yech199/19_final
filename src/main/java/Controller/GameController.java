package Controller;

import Model.ChanceCards.ChanceCard;
import Model.Die;
import Model.Fields.*;
import Model.GameBoard;
import Model.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class GameController {
    public ViewController guiController;
    public Die die1, die2;
    public GameBoard gameBoard;
    public Player[] playerList;
    private boolean gameEnded;
    public int roundCounter;

    /**
     * Denne controller laver alle de objekter som spillet skal bruge for at kunne køre
     * Fx en terning, et gameBoard og x antal spillere.
     * Der laves et array med "playerCount" antal spillere. playerListen er af typen Player.
     * Player constructoren skal bruge et navn i parameteren, desuden sættes den enkelte spillers start balance til 0 automatisk
     */
    public GameController(ViewController guiController, GameBoard gameBoard, Die die1, Die die2) {
        this.die1 = die1;
        this.die2 = die2;
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
        this(new GUIController(gameBoard.fields), gameBoard, new Die(1, 6), new Die(1, 6));
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
     * Kører spillet indtil spillet slutter (Dette sker hvis en player får en balance under nul - Se if statement i playTurn)
     */
    public void runGame() {
        decideStartingOrder();
        while (!gameEnded) {
            playRound();
        }
        setGameEnded();
    }

    /**
     * Spiller en runde for hver spiller i player listen
     */
    public void playRound() {
        roundCounter++;
        guiController.showMessage("Runde " + roundCounter);
        for (Player player : playerList) {
            if (roundCounter > 40) {
                gameEnded = true;
                break;
            }
            playTurn(player);
        }
    }

    /**
     * Logik til den enkeltes spillers tur
     */
    public void playTurn(Player player) {
        guiController.getUserButtonPressed(player.name + " skal rulle med terningen!", "Rul");
        int faceValue1 = die1.roll();
        int faceValue2 = die2.roll();
        guiController.setDice(faceValue1, 2, 8, faceValue2, 3, 8);
        int faceValue = faceValue1 + faceValue2;

        if (player.inJail) {
            if (player.getOutOfJailFree) {
                guiController.getUserButtonPressed(player.name + " er røget i fængsel, men du har et " +
                        "benådelseskort fra Kongen.", "OK");
                player.getOutOfJailFree = false;
                player.inJail = false;
            } else {
                if (guiController.getUserButtonPressed(player.name + " er røget i fængsel." +
                        "Hvordan vil du komme ud?", "Betal 1000 kr", "Rul 2 ens").equals("Rul 2 ens")) {
                    for (int i = 0; i < 3; i++) {
                        guiController.getUserButtonPressed("Rul med terningen for at komme ud", "rul");
                        int die1 = this.die1.roll();
                        int die2 = this.die1.roll();

                        guiController.setDice(die1, 2, 8, die2, 3, 8);

                        if (die1 == die2) { //tjekker om der er blevet rullet 2 ens
                            player.inJail = false;
                            faceValue = die1 + die2;
                            i = 3; //stopper loopet
                        } else faceValue = 0;
                    }
                } else {
                    player.addAmountToBalance(-1000);
                    player.inJail = false;
                }
            }
        }

        movePlayerForward(player, faceValue);

        Field landedOn = gameBoard.fields[player.getCurrentPos()];

        landedOn.fieldAction(player);
        checkIfInstanceOf(player, faceValue, landedOn);

        //--------------------------------------------------------------------------------------------------------------
        // Tjekker om den aktive spillers balance er under nul. Er balance under nul slutter spillet
        //--------------------------------------------------------------------------------------------------------------
        if (player.getBalance() <= 0) {
            gameEnded = true;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Metoder der bruges ovenover
    //------------------------------------------------------------------------------------------------------------------
    private boolean rollDice(HashMap<Integer, Player> dieValues) {
        boolean duplicates = false;
        for (Player player : playerList) {
            guiController.getUserButtonPressed(player.name + " skal rulle med terningen for, at se hvem der skal starte!", "Rul");
            int rollResult1 = die1.roll();
            int rollResult2 = die2.roll();
            int rollResult = rollResult1 + rollResult2;
            guiController.setDice(rollResult1, 2, 8, rollResult2, 3, 8);
            if (dieValues.containsKey(rollResult)) {
                duplicates = true;
                dieValues.clear();
                break;
            } else {
                dieValues.put(rollResult, player);
            }
        }
        return duplicates;
    }

    private void decideStartingOrder() {
        HashMap<Integer, Player> dieValues = new HashMap<>();
        boolean rollAgain = true;
        while (rollAgain) {
            rollAgain = rollDice(dieValues);
            if (!rollAgain){
                Object[] keys = dieValues.keySet().toArray();
                Arrays.sort(keys, Collections.reverseOrder());
                for (int i = 0; i < playerList.length; i++){
                    playerList[i] = dieValues.get(keys[i]);
                }
            }
            else {
                guiController.getUserButtonPressed("Der er ens antal øjne " + playerList[0].name + " skal rulle igen med terningen for hvem der skal starte!", "Rul");
            }
        }
    }

    private void checkIfInstanceOf(Player player, int faceValue, Field landedOn) {
        if (landedOn instanceof OwnableField ownableField) {
            if (ownableField instanceof BreweryField breweryField) {
                int rent = faceValue * 100;
                if (breweryField.owner != null) {
                    player.addAmountToBalance(-rent);
                    breweryField.owner.addAmountToBalance(rent);
                }
            }
            else if (ownableField instanceof ShippingField shippingField) {
                if (shippingField.owner != null) {
                    int rent = shippingField.rent;
                    player.addAmountToBalance(-rent);
                    shippingField.owner.addAmountToBalance(rent);
                } else if (shippingField.owner != null) {
                    int rent = shippingField.rent * 2;
                    player.addAmountToBalance(-rent);
                    shippingField.owner.addAmountToBalance(rent);
                } else if (shippingField.owner != null) {
                    int rent = shippingField.rent * 4;
                    player.addAmountToBalance(-rent);
                    shippingField.owner.addAmountToBalance(rent);
                } else if (shippingField.owner != null) {
                    int rent = shippingField.rent * 8;
                    player.addAmountToBalance(-rent);
                    shippingField.owner.addAmountToBalance(rent);
                }
            }
            if (ownableField.owner == null) {
                // Køb felt og ændr farve
                if (guiController.getUserButtonPressed("Du er landet på " + landedOn.fieldName + ". Vil du købe denne ejendom?", "Ja", "Nej").equals("Ja")) {
                    player.addAmountToBalance(-ownableField.price);
                    ownableField.owner = player;
                    guiController.setOwner(player);
                }
                //------------------------------------------------------------------------------------------------------
                // Tjekker om ejeren af det nyligt købte felt også ejer det andet af samme farve
                //------------------------------------------------------------------------------------------------------
                // FIXME fjern køb hus og hustjek
                if (landedOn instanceof PropertyField propertyField) {
                    if (ownsAll(propertyField) && propertyField.getAmountOfHouses() == 0) {
                        PropertyField[] tmpFields = gameBoard.findAllPropertyFieldsOfSameColor(propertyField.backgroundColor);
                        for (PropertyField tmpField : tmpFields) {
                            tmpField.rent += tmpField.rent;
                        }

                        // Tilføjer renten til sig selv for at fordoble den.
                        // OBS!! Denne metode er kun brugbar når ejeren ikke kan ændres.
                    }
                }
            } else {
                if (landedOn instanceof PropertyField propertyField) {
                    if (ownsAll(propertyField) && propertyField.getAmountOfHouses() == 0 &&
                            ((PropertyField) landedOn).owner.equals(player) &&
                            guiController.getUserButtonPressed("Du ejer alle felter af denne farve. Vil du købe " +
                                    "et hus for 4.000 kr til dette felt?", "Ja", "Nej").equals("Ja")) {
                        player.addAmountToBalance(-4000);
                        propertyField.addHouse(1);
                        guiController.setHouses(1, (PropertyField)landedOn);
                    }
                }

                guiController.updatePlayer(ownableField.owner);
            }

        } else if (landedOn instanceof ChanceField) {
            ChanceCard chanceCard = drawChanceCard();
            guiController.displayChanceCard(chanceCard);
            int tmpPos = player.getCurrentPos();
            chanceCard.cardAction(player, gameBoard);
            guiController.getUserButtonPressed("Tryk OK for at fortsætte", "OK");
            if (player.getCurrentPos() != tmpPos)
                checkIfInstanceOf(player, faceValue, gameBoard.fields[player.getCurrentPos()]);
        }

        guiController.updatePlayer(player);
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
        if (player.getCurrentPos() < player.getPreviousPos()) player.addAmountToBalance(4000);
        guiController.updatePlayer(player);
    }

    /**
     * Bruger getPair for at få det andet felt med samme farve.
     *
     * @param field AmusementField input
     * @return boolean output der siger om ejeren har begge felter med denne farve eller ej.
     */
    // FIXME
    public boolean ownsAll(PropertyField field) {

        Field[] tmpFields = gameBoard.findAllPropertyFieldsOfSameColor(field.backgroundColor);

        //--------------------------------------------------------------------------------------------------------------
        // Tjekker om nogle af felterne ikke har en ejer, da dette er nødvendigt for at kunne sammenligne i
        // return statementet.
        //--------------------------------------------------------------------------------------------------------------
        if (tmpFields.length == 3) {
            if (((PropertyField) tmpFields[0]).owner == null || ((PropertyField) tmpFields[1]).owner == null ||
                    ((PropertyField) tmpFields[2]).owner == null) {
                return false;
            }
            //tjekker om ejeren af første, andet og tredje felt er den samme. Hvis ikke returnerer den false
            return ((PropertyField) tmpFields[0]).owner == ((PropertyField) tmpFields[1]).owner &&
                    ((PropertyField) tmpFields[2]).owner == ((PropertyField) tmpFields[1]).owner;
        } else return ((PropertyField) tmpFields[0]).owner == ((PropertyField) tmpFields[1]).owner;
    }

    /**
     * Tjekker hvilken spiller der har vundet spillet.
     * Giver en slut-besked med spillernes endelige scorer.
     */
    public void setGameEnded() {
        String winner = null;

        for (Player player : playerList) {
            if (winner != null) {
                winner = winner + player.name + " har " + player.getBalance() + " point.\n";
            }
            else winner = player.name + " har " + player.getBalance() + " point.\n";
        }
        winner = winner + getWinner(playerList).name + " har vundet!";

        guiController.showMessage(winner);
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