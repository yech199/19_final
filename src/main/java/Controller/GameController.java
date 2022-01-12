package Controller;

import Model.ChanceCards.ChanceCard;
import Model.ChanceCards.MovementCard;
import Model.Die;
import Model.Fields.*;
import Model.GameBoard;
import Model.GlobalValues;
import Model.Player;
import View.GUIView;
import View.GameView;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class GameController {
    public GameView guiController;
    public Die die1, die2;
    public GameBoard gameBoard;
    public Player[] playerList;
    public boolean gameEnded;
    public boolean afterAuction = false;
    public int roundCounter;
    private Player[] tmpPlayerList;
    private Player[] playerRankList = new Player[]{};
    public String choice;
    public String action1;
    public String action2;
    private int turnCounter;
    private boolean extraTurn;

    public GameController(GameView guiController, GameBoard gameBoard, Die die1, Die die2, Player[] players) {
        this.die1 = die1;
        this.die2 = die2;
        this.gameBoard = gameBoard;
        this.guiController = guiController;
        this.playerList = players;
        this.tmpPlayerList = this.playerList;
    }

    /**
     * Denne controller laver alle de objekter som spillet skal bruge for at kunne køre
     * Fx en terning, et gameBoard og x antal spillere.
     * Der laves et array med "playerCount" antal spillere. playerListen er af typen Player.
     * Player constructoren skal bruge et navn i parameteren, desuden sættes den enkelte spillers start balance til 0 automatisk
     */
    public GameController(GameView guiController, GameBoard gameBoard, Die die1, Die die2) {
        this(guiController, gameBoard, die1, die2, setUpPlayers(guiController));
    }

    /**
     * Denne constructor sætter default terning og GUIController
     * Dette er et mellemled mellem de to andre constructors, fordi this skal stå først i constructeren.
     * <p> Denne constructor kalder på ovenstående constructor
     */
    private GameController(GameBoard gameBoard) {
        this(new GUIView(gameBoard.fields), gameBoard, new Die(), new Die());
    }

    /**
     * Denne constructor sætter default terning, GUIController og GameBoard.
     * <p>
     * Denne constructor kalder på ovenstående constructor
     */
    public GameController() {
        this(new GameBoard());
    }

    private static Player[] setUpPlayers(GameView guiController) {
        int playerCount = guiController.getUserInteger(String.format("Hvor mange spillere (%d-%d)?",
                        GlobalValues.MIN_PLAYERS, GlobalValues.MAX_PLAYERS),
                GlobalValues.MIN_PLAYERS, GlobalValues.MAX_PLAYERS);

        // Laver x antal nye spillere med navn
        Player[] playerList = new Player[playerCount];
        for (int i = 0; i < playerList.length; i++) {
            playerList[i] = new Player(i, guiController.getUserString("Player " + (i + 1) + " skriv dit navn:"));
        }

        guiController.setUpPlayers(playerList);

        return playerList;
    }

    /**
     * Kører spillet indtil spillet slutter (Dette sker hvis en player får en balance under nul - Se if statement i playTurn)
     */
    public void runGame() {
        action1 = "Normalt spil";
        action2 = "Hurtigt spil";
        choice = guiController.getUserButtonPressed("""
                Hvordan vil I slutte spillet?
                \t1. Spil til der kun er en spiller tilbage.
                \t2. Slut spillet efter 40 runder""", action1, action2);

        makeStartingOrderPlayerList();
        while (!gameEnded) {
            playRound();
        }
        setGameEnded();
    }

    /**
     * Spiller en runde for hver spiller i player listen
     */
    public void playRound() {
        if (tmpPlayerList.length != 1) {
            roundCounter++;
            guiController.showMessage("Runde " + roundCounter);
        }
        for (Player player : playerList) {

            if (choice.equals(action2)) {
                if (roundCounter > 40) {
                    gameEnded = true;
                    break;
                }
            }

            if (tmpPlayerList.length == 1) {
                gameEnded = true;
                playerRankList = makePlayerRankArray(playerRankList, player);
                return;
            }
            playTurn(player);
        }
    }

    /**
     * Logik til den enkeltes spillers tur
     */
    public void playTurn(Player player) {
        int faceValue1;
        int faceValue2;
        int faceValue = 0;

        if (!player.inJail) {
            faceValue = rollDice(player);
        }
        else {

            if (player.getOutOfJailFree) {
                guiController.getUserButtonPressed(player.name + " er røget i fængsel, " +
                        "men har et benådelseskort fra Kongen, og kommer derfor gratis ud af fængslet", "OK");
                player.getOutOfJailFree = false;
                player.inJail = false;
            }
            else {

                action1 = "Betal " + GlobalValues.JAIL_PRICE + " kr";
                action2 = "Rul 2 ens";
                choice = guiController.getUserButtonPressed(player.name + " er røget i fængsel." +
                        "Hvordan vil du komme ud?", action1, action2);
                if (choice.equals(action2)) {

                    if (player.jailTryRollCounter < 3) {
                        for (int i = 0; i < 3; i++) {
                            guiController.getUserButtonPressed("Rul med terningen for at komme ud", "rul");
                            faceValue1 = this.die1.roll();
                            faceValue2 = this.die1.roll();
                            faceValue = faceValue1 + faceValue2;

                            guiController.setDice(faceValue1, 2, 8, faceValue2, 3, 8);

                            // Tjekker om der er blevet rullet 2 ens
                            if (faceValue1 == faceValue2) {
                                extraTurn = true;
                                player.inJail = false;
                                i = 3; //stopper loopet
                                player.jailTryRollCounter = 1;
                            }
                        }
                        player.jailTryRollCounter++;

                        if (player.inJail && player.jailTryRollCounter == 3) {
                            player.jailTryRollCounter = 1;
                            guiController.showMessage("Du har haft 3 forsøg af 3 runder og har stadig ikke rulles 2 ens. " +
                                    "\nDu er derfor nødt til at betale dig ud af fængslet. " +
                                    "\nDu har slået " + faceValue + ". Du kan nu fortsætte din tur ");
                            player.addAmountToBalance(-GlobalValues.JAIL_PRICE);
                            player.inJail = false;
                        }
                        if (player.inJail) return;
                    }
                }
                else {
                    player.jailTryRollCounter = 1;
                    player.addAmountToBalance(-GlobalValues.JAIL_PRICE);
                    player.inJail = false;
                    faceValue = rollDice(player);
                }
            }
        }

        movePlayerForward(player, faceValue);

        Field landedOn = gameBoard.fields[player.getCurrentPos()];

        checkInstanceOf(player, faceValue, landedOn);

        if (!afterAuction) landedOn.fieldAction(player);
        else afterAuction = false;

        guiController.updatePlayer(player);

        //--------------------------------------------------------------------------------------------------------------
        // Tjekker om den aktive spillers balance er under nul. Er balance under nul slutter spillet
        //--------------------------------------------------------------------------------------------------------------
        if (player.getBalance() <= 0) {
            extraTurn = false;
            guiController.showMessage(player.name + " har mistet alle dine penge og har derfor tabt spillet");
            // Slet næste linje hvis du vil sætte taberen i endnu mere evig skam
            guiController.removeCar(player);

            for (int i = 0; i < gameBoard.fields.length; i++) {
                Field f = gameBoard.fields[i];
                if (f instanceof OwnableField ownableField && player == ownableField.owner) {
                    if (tmpPlayerList.length != 2)
                        doAuction(player, ownableField);

                    if (ownableField.owner == player) {
                        ownableField.owner = null;
                        guiController.removeOwner(i);
                    }
                }
            }
            tmpPlayerList = removeElementFromOldArray(tmpPlayerList, player.getIndex());
            playerRankList = makePlayerRankArray(playerRankList, player);
            afterAuction = false;
        }
        if ((player == playerList[playerList.length - 1] && tmpPlayerList.length != playerList.length) ||
                tmpPlayerList.length == 1) {
            playerList = tmpPlayerList;
        }

        //--------------------------------------------------------------------------------------------------------------
        // Tjekker om spilleren har fået en ekstra tur ved at slå 2 ens. Hvis de har slået 2 ens 3 gange i træk ryger
        // de i fængsel.
        //--------------------------------------------------------------------------------------------------------------
        if (extraTurn) {
            extraTurn = false;
            if (turnCounter < 2) {
                turnCounter++;
                guiController.showMessage("Du har slået 2 ens. Du får en tur til!");
                playTurn(player);
            }
            else {
                guiController.showMessage("Du har slået 2 ens 3 gange i træk. Du fængsles");
                player.putInJail();
                guiController.updatePlayer(player);
                turnCounter = 0;
            }
        }
    }

    private void checkInstanceOf(Player player, int faceValue, Field landedOn) {
        if (landedOn instanceof IncomeTaxField incomeTaxField) {
            action1 = "4000 kr.";
            action2 = "10 %";
            choice = guiController.getUserButtonPressed("Du skal betale indkomstskat. Du har nu følgende valgmuligheder: " +
                    "\n\t1. Betal 4000 kr.\n\t2. Betal 10% af alle dine værdier", action1, action2);

            if (choice.equals(action1))
                player.addAmountToBalance(-incomeTaxField.tax);
            else {
                player.addAmountToBalance(-(player.getNetWorth() / 100 * incomeTaxField.percent));
            }
        }
        else if (landedOn instanceof ChanceField) {
            ChanceCard chanceCard = drawChanceCard();
            guiController.displayChanceCard(chanceCard);
            doCardAction(player, faceValue, chanceCard);
        }
        // if instanceof OwnableField
        else {
            updateOwnerAndRent(player, faceValue, landedOn);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Metoder der bruges ovenover
    //------------------------------------------------------------------------------------------------------------------
    private int rollDice(Player player) {
        int faceValue2;
        int faceValue1;
        int faceValue;
        guiController.getUserButtonPressed(player.name + " skal rulle med terningen!", "Rul");
        faceValue1 = die1.roll();
        faceValue2 = die2.roll();
        guiController.setDice(faceValue1, 2, 8, faceValue2, 3, 8);
        faceValue = faceValue1 + faceValue2;

        if (faceValue1 == faceValue2) {
            extraTurn = true;
        }
        return faceValue;
    }

    private boolean rollDiceToDecideStartingOrder(HashMap<Integer, Player> dieValues) {
        boolean duplicates = false;
        for (Player player : playerList) {
            guiController.getUserButtonPressed(player.name + " skal rulle med terningen for, " +
                    "at se hvem der skal starte!", "Rul");
            int rollResult1 = die1.roll();
            int rollResult2 = die2.roll();
            int rollResult = rollResult1 + rollResult2;
            guiController.setDice(rollResult1, 2, 8, rollResult2, 3, 8);
            if (dieValues.containsKey(rollResult)) {
                duplicates = true;
                dieValues.clear();
                break;
            }
            else {
                dieValues.put(rollResult, player);
            }
        }
        return duplicates;
    }

    private void makeStartingOrderPlayerList() {
        HashMap<Integer, Player> dieValues = new HashMap<>();
        Player[] orderOfPlayers = playerList;

        boolean rollAgain = true;
        while (rollAgain) {
            rollAgain = rollDiceToDecideStartingOrder(dieValues);
            if (!rollAgain) {
                Object[] keys = dieValues.keySet().toArray();
                Arrays.sort(keys, Collections.reverseOrder());
                for (int i = 0; i < orderOfPlayers.length; i++) {
                    orderOfPlayers[i] = dieValues.get(keys[i]);
                }
            }
            else {
                guiController.getUserButtonPressed("Der er ens antal øjne " + playerList[0].name + " skal rulle" +
                        " igen med terningen for hvem der skal starte!", "Rul");
            }
        }
        for (int i = 0; i < orderOfPlayers.length; i++) {
            playerList[i].setIndex(i);
        }
    }

    public void updateOwnerAndRent(Player player, int faceValue, Field landedOn) {
        if (landedOn instanceof OwnableField ownableField) {
            if (ownableField.owner == null) {
                // Køb felt og ændr farve

                action1 = "Ja";
                action2 = "Nej";
                choice = guiController.getUserButtonPressed("Du er landet på " + landedOn.fieldName +
                        ". Vil du købe denne ejendom?", action1, action2);

                if (choice.equals(action1)) {
                    player.addAmountToBalance(-ownableField.price);
                    player.addToNetWorth(ownableField.price);
                    ownableField.owner = player;
                    guiController.setOwner(player, player.getCurrentPos());

                    // Opdaterer spillerens ejede færgers rente efter spilleren køber en færge
                    if (ownableField instanceof ShippingField) {
                        updateShippingFieldRent(player);
                    }

                    // FIXME fjern hustjek
                    //  OBS!! Denne metode er kun brugbar når ejeren ikke kan ændres.
                    if (landedOn instanceof PropertyField propertyField) {
                        ownsAll(propertyField);
                    }
                }
                // TODO: Test Auktion
                else {
                    doAuction(player, ownableField);
                }
            }

            else {
                if (landedOn instanceof PropertyField propertyField) {
                    action1 = "Ja";
                    action2 = "Nej";

                    if (propertyField.owner == player && ownsAll(propertyField)) {

                        // Køb x antal huse, hvis du har 0-3 huse
                        if (propertyField.getAmountOfBuildings() <= (GlobalValues.MAX_AMOUNT_OF_HOUSES - 1)) {
                            choice = guiController.getUserButtonPressed("Du ejer alle felter af denne farve. " +
                                    "Vil du købe huse for " + propertyField.buildingPrice + " kr. til "
                                    + propertyField.fieldName + "?", action1, action2);

                            if (choice.equals(action1)) {
                                int max = GlobalValues.MAX_AMOUNT_OF_HOUSES - propertyField.getAmountOfBuildings();
                                int houseCount = guiController.getUserInteger("Hvor mange huse vil du købe?", 1, max);
                                for (int i = 0; i < houseCount; i++) {
                                    propertyField.buyBuilding(player);
                                }
                                for (int i = 0; i < gameBoard.fields.length; i++) {
                                    Field field = gameBoard.fields[i];
                                    if (field == propertyField) {
                                        guiController.setHouses(houseCount, i);
                                    }
                                }
                            }
                        }

                        // Køb hotel, hvis du ejer 4 huse allerede
                        if (propertyField.getAmountOfBuildings() == GlobalValues.MAX_AMOUNT_OF_HOUSES &&
                                guiController.getUserButtonPressed("Du ejer 4 huse på dette felt. " +
                                                "Vil du købe et hotel for " + propertyField.buildingPrice + " kr?",
                                        action1, action2).equals(action1)) {
                            propertyField.buyBuilding(player);
                            guiController.setOrRemoveHotel(true, player.getCurrentPos());
                        }
                    }
                }

                // Har brug for en faceValue og står derfor ikke samme sted som shippingField
                else if (ownableField instanceof BreweryField) {
                    Player owner = ownableField.owner;
                    int counter = 0;
                    for (BreweryField f : gameBoard.breweryFields)
                        if (f.owner == owner)
                            counter++;

                    ownableField.rent = faceValue * (100 * counter);
                }
                guiController.updatePlayer(ownableField.owner);
            }
        }
        guiController.updatePlayer(player);
    }

    private void doCardAction(Player player, int faceValue, ChanceCard chanceCard) {
        Field landedOn;
        int tmpPos = player.getCurrentPos();
        chanceCard.cardAction(player, gameBoard);

        if (chanceCard instanceof MovementCard) {
            guiController.updatePlayer(player);

            // Sørger for at man laver den handling der svarer til det felt man lander på
            landedOn = gameBoard.fields[player.getCurrentPos()];
            landedOn.fieldAction(player);
            checkInstanceOf(player, faceValue, landedOn);
        }
        guiController.getUserButtonPressed("Tryk OK for at fortsætte", "OK");
        // Sørger for at man ikke trækker et nyt chancekort, hvis man ikke rykker sig
        if (player.getCurrentPos() != tmpPos)
            updateOwnerAndRent(player, faceValue, gameBoard.fields[player.getCurrentPos()]);
    }

    /**
     * @param player       Der ikke har købt feltet eller som skal af med feltet
     * @param ownableField Det felt der bydes på
     */
    public void doAuction(Player player, OwnableField ownableField) {
        guiController.showMessage("Alle andre spillere har nu mulighed for at byde på " + ownableField.fieldName +
                " såfremt at de ikke er i fængsel \nog har råd til at købe " + ownableField.fieldName + ". Laveste bud starter på "
                + ownableField.price + " kr. \nHvis man er i fængsel eller ikke har penge nok til at betale mindsteprisen mister man sin ret til at deltage i auktionen.");

        int numOfPlayersBidding = checksWhoIsBiddingOnAuction(player, ownableField, tmpPlayerList);

        if (numOfPlayersBidding > 0) {
            bidOnAuction(ownableField, numOfPlayersBidding);
            afterAuction = true;
        }
    }

    /**
     * @param player       der ikke købte feltet
     * @param ownableField Det felt der sættes på auktion
     */
    public int checksWhoIsBiddingOnAuction(Player player, OwnableField ownableField, Player[] tmpPlayerList) {
        int numOfPlayersBidding = tmpPlayerList.length;
        action1 = "Ja";
        action2 = "Nej";

        for (Player p : tmpPlayerList) {
            p.wantToTryBidding = true;
            if (p == player || p.inJail || p.getBalance() < ownableField.price) {

                if (numOfPlayersBidding == 1) {
                    choice = guiController.getUserButtonPressed(p.name + " er den eneste der kan være med i auktionen. Vil " + p.name + " købe "
                            + ownableField.fieldName + " til mindsteprisen, som er " + ownableField.price + ".", action1, action2);
                    if (choice.equals(action2)) return 0;
                    else return 1;
                }
                else if (numOfPlayersBidding == 0) return 0;
                
                p.wantToTryBidding = false;
                numOfPlayersBidding -= 1;
            }

            if (p.wantToTryBidding) {
                choice = guiController.getUserButtonPressed("Vil " + p.name + " byde på "
                        + ownableField.fieldName + "? Buddet starter på " + ownableField.price + ".", action1, action2);
                if (choice.equals(action2)) {
                    p.wantToTryBidding = false;
                    numOfPlayersBidding -= 1;
                }

            }
        }
        if (numOfPlayersBidding == 1) {
            guiController.showMessage("Da du er den eneste spiller der har valgt at byde på " + ownableField.fieldName + ", " +
                    "\nfår du grunden til mindsteprisen, som er " + ownableField.price + ".");
        }
        return numOfPlayersBidding;
    }

    /**
     * @param ownableField Det felt der sættes på auktion
     */
    public void bidOnAuction(OwnableField ownableField, int numOfPlayersBidding) {
        action1 = "Ja";
        action2 = "Nej";
        int prevBid = ownableField.price;
        int bid;
        Player prevPlayer = null;

        while (numOfPlayersBidding > 1) {
            for (Player p : tmpPlayerList) {
                if (numOfPlayersBidding == 1) break;

                if (p.wantToTryBidding) {

                    if (p.getBalance() < prevBid && prevPlayer != p) {
                        guiController.showMessage(p.name + " har desværre ikke nok penge til at overbyde den forrige spiller, " +
                                "og udgår derfor fra denne auktion");
                        p.wantToTryBidding = false;
                        numOfPlayersBidding -= 1;
                        continue;
                    }
                    if (prevPlayer != null && prevPlayer != p) {
                        choice = guiController.getUserButtonPressed(prevPlayer.name + " bød " + prevBid + " kr. " +
                                "Vil " + p.name + " stadig byde på " + ownableField.fieldName + "?", action1, action2);
                        if (choice.equals(action2)) {
                            p.wantToTryBidding = false;
                            numOfPlayersBidding -= 1;
                            continue;
                        }

                        bid = guiController.getUserInteger("Hvad vil " + p.name + " byde på " + ownableField.fieldName
                                + "? Buddet starter på " + (prevBid + 1) + " kr.", (prevBid + 1), p.getBalance());
                    }
                    else {
                        bid = guiController.getUserInteger("Hvad vil " + p.name + " byde på " + ownableField.fieldName
                                + "? Buddet starter på " + prevBid + " kr.", prevBid, p.getBalance());
                    }
                    // Det er ikke muligt at byde lavere. Derfor er dette ikke inkluderet i if statementet
                    while (bid == prevBid && prevPlayer != null && prevPlayer != p) {
                        bid = guiController.getUserInteger("Dette bud er ugyldigt. Giv et nyt bud" +
                                "\nHvad vil " + p.name + " byde på " + ownableField.fieldName + "? Buddet starter på "
                                + prevBid + " kr.", prevBid, p.getBalance());
                    }
                    prevPlayer = p;
                    prevBid = bid;
                }
            }
        }

        if (numOfPlayersBidding == 1 && prevPlayer == null) {
            guiController.showMessage("Du er den eneste spiller, der har valgt at byde på " + ownableField.fieldName + ", " +
                    "\nog får derfor " + ownableField.fieldName + " til grundens originale pris, da buddet ville have startet på "
                    + ownableField.price + "kr.");
        }

        for (Player p : tmpPlayerList) {
            if (p.wantToTryBidding) {
                ownableField.owner = p;
                p.addAmountToBalance(-prevBid);
                guiController.updatePlayerBalance(p);

                for (int i = 0; i < gameBoard.fields.length; i++) {
                    Field f = gameBoard.fields[i];
                    if (f == ownableField) {
                        guiController.setOwner(p, i);
                    }
                }
            }
        }

        if (ownableField instanceof ShippingField)
            updateShippingFieldRent(prevPlayer);

        if (ownableField instanceof PropertyField propertyField) {
            ownsAll(propertyField);
        }
    }

    private void updateShippingFieldRent(Player player) {
        int count = 0;
        for (ShippingField f : gameBoard.shippingFields)
            if (f.owner == player)
                count++;

        int rent = GlobalValues.SHIPPING_RENT * ((int) Math.pow(2, count - 1));
        for (ShippingField f : gameBoard.shippingFields)
            if (f.owner == player)
                f.rent = rent;
    }

    /**
     * Rykker spilleren frem og sørger for at man kan gå i ring på brættet vha. Modulos
     * Modulus tjekker om currentPos bliver højere end antallet af felter, og fjerner antallet af felter i dette tilfælde.
     * (Feltet vil altid være mindre end længden af brættet)
     *
     * @param player    Den aktive player
     * @param faceValue terningens faceValue
     */
    private void movePlayerForward(Player player, int faceValue) {
        player.setCurrentPos((player.getCurrentPos() + faceValue) % gameBoard.fields.length);

        // Spilleren passerer Start
        if (player.getCurrentPos() < player.getPreviousPos()) player.addAmountToBalance(GlobalValues.START_FIELD_VALUE);
        guiController.updatePlayer(player);
    }

    /**
     * Bruges til at tjekke om spilleren ejer alle felter med samme farve.
     * Hvis alle grunde af samme farve er ejet fordobles renten af disse felter
     * <p>
     * OBS! Brug kun lige efter køb af et OwnableField
     *
     * @param propertyField PropertyField med den farve vi vil tjekke om spilleren ejer
     * @return boolean output der siger om ejeren ejer alle felter med denne farve
     */
    public boolean ownsAll(PropertyField propertyField) {
        PropertyField[] tmpFields = gameBoard.findAllPropertyFieldsOfSameColor(propertyField.backgroundColor);
        boolean ownsAll = false;

        //--------------------------------------------------------------------------------------------------------------
        // Tjekker om nogle af felterne ikke har en ejer, da dette er nødvendigt for at kunne sammenligne i
        // return statementet.
        //--------------------------------------------------------------------------------------------------------------
        if (tmpFields[0].owner != null) {
            if (tmpFields.length == 2 && tmpFields[0].owner == tmpFields[1].owner) {
                // Tjekker om ejeren af første, andet og tredje felt er den samme. Hvis ikke returnerer den false
                ownsAll = true;
            }
            else if (tmpFields.length == 3 && tmpFields[0].owner == tmpFields[1].owner && tmpFields[1].owner == tmpFields[2].owner) {
                ownsAll = true;
            }
        }

        if (ownsAll) {
            // Fordobler renten
            //FIXME tjek om renten er blevet fordoblet allerede
            for (int i = 0, k = 0; i < gameBoard.fields.length && k < tmpFields.length; i++) {
                Field field = gameBoard.fields[i];
                if (field instanceof PropertyField pField && pField == tmpFields[k]) {
                    k++;
                    // Da denne grund lige er købt ved vi at HouseCount == 0
                    pField.rent = pField.rents[0];
                    pField.rent *= 2;
                }
            }
        }

        return ownsAll;
    }

    /**
     * Tjekker hvilken spiller der har vundet spillet.
     * Giver en slut-besked med spillernes endelige scorer.
     */
    public void setGameEnded() {
        Player winner = getWinner(playerList);
        StringBuilder winnerMessage = null;

        if (tmpPlayerList.length == 1) {
            for (int i = playerRankList.length - 1, k = 1; i >= 0; i--, k++) {
                Player player = playerRankList[i];
                if (winnerMessage != null) {
                    winnerMessage.append(player.name).append(" fik ").append(k).append(". pladsen.\n");
                }
                else winnerMessage = new StringBuilder(player.name + " fik " + k + ". pladsen.\n");
            }
            guiController.showMessage("Spillet er slut!\n" +
                    winner.name + " er den eneste spiller tilbage og\n" +
                    winner.name + " har derfor vundet med " + winner.getBalance() + " kr.\n");
        }
        else {
            for (Player player : playerList) {
                if (winnerMessage != null) {
                    winnerMessage.append(player.name).append(" har ").append(player.getBalance()).append(" point.\n");
                }
                else winnerMessage = new StringBuilder(player.name + " har " + player.getBalance() + " point.\n");
            }
        }
        winnerMessage.append(winner.name).append(" har vundet! ");

        guiController.showMessage(winnerMessage.toString());
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
    Player getWinner(Player[] playerList) {
        Player winner = new Player("", 0); //tom spiller, da den udskiftes med en ny spiller efter første runde i for-loop
        for (Player player : playerList) {
            if (winner.getBalance() < player.getBalance()) {
                winner = player;
            }
        }
        return winner;
    }

    private ChanceCard drawChanceCard() {
        int rng = new Random().nextInt(gameBoard.chanceCards.length);
        return gameBoard.chanceCards[rng];
    }

    /**
     * Fjerner en player fra et array af typen Player
     *
     * @param oldArray Det array vi vil fjerne et element fra
     * @param index    Den spiller vi vil fjerne fra arrayet
     * @return Det gamle array uden den spiller vi hat fjernet
     */
    private Player[] removeElementFromOldArray(Player[] oldArray, int index) {
        // Hvis array'et er tomt, eller hvis index'et ikke er i array rækkevidden
        // returneres det originale array
        if (oldArray == null || index < 0 || index >= oldArray.length) {
            return oldArray;
        }

        // Laver et array der er et element mindre end det originale array
        Player[] anotherArray = new Player[oldArray.length - 1];

        // Kopierer alle elementer bortset fra index'et fra det originale array ind i det nye array
        for (int i = 0, k = 0; i < oldArray.length; i++) {

            // Hvis index'et er det element man vil fjerne
            if (i == index) {
                continue;
            }

            // Hvis index'et ikke er den element man vil fjerne
            anotherArray[k++] = oldArray[i];
        }
        for (int i = 0; i < anotherArray.length; i++) {
            anotherArray[i].setIndex(i);
        }
        return anotherArray;
    }

    private static Player[] makePlayerRankArray(Player[] oldArray, Player newElement) {
        int n = oldArray.length;
        Player[] newArray = new Player[n + 1];

        //--------------------------------------------------------------------------------------------------------------
        // Indsætter det gamle array i det nye array
        //--------------------------------------------------------------------------------------------------------------
        for (int i = 0; i < n; i++)
            newArray[i] = oldArray[i];

        newArray[n] = newElement;
        return newArray;
    }
}