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
    public GameView UI;
    public Die die1, die2;
    public GameBoard gameBoard;
    public boolean gameEnded;
    public boolean afterAuction = false;
    public int roundCounter;
    public Player[] playerList;
    private Player[] tmpPlayerList;
    private final Player[] quickGamePlayerList;
    private Player[] playerRankList = new Player[]{};
    private int turnCounter;
    private boolean extraTurn;
    public boolean quickGame;
    private boolean afterMortage = false;

    public GameController(GameView UI, GameBoard gameBoard, Die die1, Die die2, Player[] players) {
        this.die1 = die1;
        this.die2 = die2;
        this.gameBoard = gameBoard;
        this.UI = UI;
        this.playerList = players;
        this.tmpPlayerList = this.playerList;
        this.quickGamePlayerList = this.playerList;
    }

    /**
     * Denne controller laver alle de objekter som spillet skal bruge for at kunne køre
     * Fx en terning, et gameBoard og x antal spillere.
     * Der laves et array med "playerCount" antal spillere. playerListen er af typen Player.
     * Player constructoren skal bruge et navn i parameteren, desuden sættes den enkelte spillers start balance til 0 automatisk
     */
    public GameController(GameView UI, GameBoard gameBoard, Die die1, Die die2) {
        this(UI, gameBoard, die1, die2, setUpPlayers(UI));
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

    private static Player[] setUpPlayers(GameView UI) {
        int playerCount = UI.getUserInteger(String.format("Hvor mange spillere (%d-%d)?",
                        GlobalValues.MIN_PLAYERS, GlobalValues.MAX_PLAYERS),
                GlobalValues.MIN_PLAYERS, GlobalValues.MAX_PLAYERS);

        // Laver x antal nye spillere med navn
        Player[] playerList = new Player[playerCount];
        for (int i = 0; i < playerList.length; i++) {
            String playerName = UI.getUserString("Player " + (i + 1) + " skriv dit navn:");

            for (int j = 0; j < i; j++) {
                if (playerList[j].name.equals(playerName)) {
                    j = -1;
                    playerName = UI.getUserString("Dette navn er allerede i brug. " +
                            "\nPlayer " + (i + 1) + " vælg venligst et nyt navn:");
                }
            }

            playerList[i] = new Player(i, playerName);
        }

        UI.setUpPlayers(playerList);

        return playerList;
    }

    public void setupGame() {
        String gameAction1 = "Normalt spil";
        String gameAction2 = "Hurtigt spil";
        String gameChoice = UI.getUserButtonPressed("""
                Hvordan vil I slutte spillet?
                \t1. Spil til der kun er en spiller tilbage.
                \t2. Slut spillet efter 40 runder""", gameAction1, gameAction2);
        this.quickGame = gameChoice.equals(gameAction2);

        makeStartingOrderPlayerList();
    }

    /**
     * Kører spillet indtil spillet slutter (Dette sker hvis en player får en balance under nul - Se if statement i playTurn)
     */
    public void runGame() {
        while (!gameEnded) {
            playRound();
            if (quickGame && roundCounter >= 40) {
                gameEnded = true;
            }
        }

        setGameEnded();
    }

    /**
     * Spiller en runde for hver spiller i player listen
     */
    public void playRound() {
        if (tmpPlayerList.length != 1) {
            roundCounter++;

            UI.showMessage("Runde " + roundCounter);
        }
        for (Player player : playerList) {
            turnCounter = 0;

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
                UI.getUserButtonPressed(player.name + " er røget i fængsel, " +
                        "men har et benådelseskort fra Kongen, og kommer derfor gratis ud af fængslet", "OK");
                player.getOutOfJailFree = false;
                player.inJail = false;
            }
            else {

                String action1 = "Betal " + GlobalValues.JAIL_PRICE + " kr";
                String action2 = "Rul 2 ens";
                String choice = UI.getUserButtonPressed(player.name + " er røget i fængsel." +
                        "Hvordan vil du komme ud?", action1, action2);
                if (choice.equals(action2)) {

                    if (player.jailTryRollCounter < 3) {
                        for (int i = 0; i < 3; i++) {
                            UI.getUserButtonPressed("Rul med terningen for at komme ud", "rul");
                            faceValue1 = this.die1.roll();
                            faceValue2 = this.die1.roll();
                            faceValue = faceValue1 + faceValue2;

                            UI.setDice(faceValue1, 2, 8, faceValue2, 3, 8);

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
                            UI.showMessage("Du har haft 3 forsøg af 3 runder og har stadig ikke rulles 2 ens. " +
                                    "\nDu er derfor nødt til at betale dig ud af fængslet. " +
                                    "\nDu har slået " + faceValue + ". Du kan nu fortsætte din tur.");
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

        UI.updatePlayer(player);

        if (player.getBalance() <= 0) {
            int tmpBalance = (player.getNetWorth() / 2) + player.getBalance();

            if (tmpBalance > 0) {
                String action1 = "Ja";
                String action2 = "Nej";
                String choice = UI.getUserButtonPressed(player.name + " har mistet alle sine penge og står nu i gæld til banken, " +
                        "men du har muligheden for at sælge dine bygninger og ejendomme, og dermed spille videre. " +
                        "Vil du sælge dine ejendomme?", action1, action2);
                if (choice.equals(action1)) {
                    for (int i = 0; i < gameBoard.fields.length; i++) {
                        Field field = gameBoard.fields[i];
                        if (field instanceof OwnableField ownableField && ownableField.owner == player) {

                            if (ownableField instanceof PropertyField propertyField && propertyField.amountOfBuildings > 0) {
                                if (propertyField.amountOfBuildings == GlobalValues.MAX_AMOUNT_OF_BUILDINGS) {
                                    sellHotel(player, i, propertyField);
                                    tmpBalance -= (propertyField.buildingPrice / 2 * GlobalValues.MAX_AMOUNT_OF_BUILDINGS);
                                }
                                else {
                                    sellHouse(player, i, propertyField);
                                    tmpBalance -= (propertyField.buildingPrice / 2 * propertyField.amountOfBuildings);
                                }

                                if (propertyField.amountOfBuildings > 0) continue;
                                if (!(tmpBalance > 0)) break;
                            }
                            // Pantsætning
                            if (tmpBalance > 0) {
                                mortgageField(player, i, ownableField);
                                tmpBalance -= (ownableField.price / 2);
                            }
                        }
                        UI.updatePlayerBalance(player);
                    }
                    afterMortage = true;
                }
            }
            if (player.getBalance() <= 0) {
                updateGameWhenPlayerGoBankerupt(player);
            }
        }

        if (player.haveMortgagedField && !afterMortage) {
            for (int i = 0; i < gameBoard.fields.length; i++) {
                Field field = gameBoard.fields[i];
                if (field instanceof OwnableField ownableField && ownableField.isMortgaged()) {
                    unmortgageFields(player, i, ownableField);
                }
            }
            UI.updatePlayerBalance(player);
        }

        // Man bliver tilbudt at købe huse HVIS man ejer alle felter af samme farve og ingen af felterne er pantsat
        if (!afterMortage){
            outerloop:
            for (int i = 0; i < gameBoard.fields.length; i++) {
                Field field = gameBoard.fields[i];
                if (field instanceof PropertyField propertyField && propertyField.owner == player && ownsAll(propertyField)) {

                    if (propertyField.amountOfBuildings == GlobalValues.MAX_AMOUNT_OF_BUILDINGS) continue;

                    for (int j = 0; j < gameBoard.fields.length; j++) {
                        Field f = gameBoard.fields[j];
                        if (f instanceof PropertyField p && p.isMortgaged() && propertyField.backgroundColor.equals(p.backgroundColor)) {
                            continue outerloop;
                        }
                    }

                    String action1 = "Ja";
                    String action2 = "Nej";
                    // Køb x antal huse, hvis du har 0-3 huse
                    if (propertyField.amountOfBuildings <= (GlobalValues.MAX_AMOUNT_OF_HOUSES - 1)) {
                        String choice = UI.getUserButtonPressed("Du ejer alle felter af denne farve. " +
                                "Vil du købe huse for " + propertyField.buildingPrice + " kr. til "
                                + propertyField.fieldName + "?", action1, action2);

                        if (choice.equals(action1)) {
                            int max = GlobalValues.MAX_AMOUNT_OF_HOUSES - propertyField.amountOfBuildings;
                            int houseCount = UI.getUserInteger("Hvor mange huse vil du købe? Du kan købe max " + max + " hus(e).", 1, max);
                            for (int j = 0; j < houseCount; j++) {
                                propertyField.buyBuilding(player);
                            }
                            houseCount += propertyField.amountOfBuildings;

                            for (int j = 0; j < gameBoard.fields.length; j++) {
                                Field f = gameBoard.fields[j];
                                if (f == propertyField) {
                                    UI.updateAmountOfHouses(houseCount, j);
                                }
                            }
                        }
                    }

                    // Køb hotel, hvis du ejer 4 huse allerede
                    if (propertyField.amountOfBuildings == GlobalValues.MAX_AMOUNT_OF_HOUSES &&
                            UI.getUserButtonPressed("Du ejer 4 huse på dette felt. " +
                                            "Vil du købe et hotel for " + propertyField.buildingPrice + " kr?",
                                    action1, action2).equals(action1)) {
                        propertyField.buyBuilding(player);
                        UI.setOrRemoveHotel(true, player.getCurrentPos());
                    }
                    UI.updatePlayerBalance(player);
                }
            }
        }
        afterMortage = false;

        if (extraTurn) {
            doExtraTurn(player);
        }

        // Opdater playerList når den sidste spiller i listen har spillet sin tur færdig
        if ((player == playerList[playerList.length - 1] && tmpPlayerList.length != playerList.length) ||
                tmpPlayerList.length == 1) {
            playerList = tmpPlayerList;
        }
    }

    /**
     * @param player Spilleren som kan ophæve pantsætningen af sine ejendomme
     * @param i Index til det felt man kan ophæve pantsætningen af
     * @param ownableField Det felt man kan ophæve pantsætningen af
     */
    private void unmortgageFields(Player player, int i, OwnableField ownableField) {
        // Renten er 10% (der rundes op til nærmeste 100 kr.), og renten betales sammen
        // med lånet, når pantsætningen hæves.
        int stopMortgagePrice = (int) Math.ceil((((double) ownableField.price / 2 * 1.1) / 100.)) * 100;
        if (player.getBalance() > (stopMortgagePrice + 1)) {
            String action1 = "Ja";
            String action2 = "Nej";
            String choice = UI.getUserButtonPressed("Vil " + player.name + " ophæve pantsætningen af "
                    + ownableField.fieldName + " for " + stopMortgagePrice +
                    "?\nDu har en balance på " + player.getBalance() + " kr.", action1, action2);
            if (choice.equals(action1)) {
                ownableField.setMortgaged(false);
                UI.setOwner(player, i);

                // Tjekker om spilleren har flere pantsatte ejendomme tilbage
                for (Field f : gameBoard.fields) {
                    int counter = 0;
                    if (f instanceof OwnableField ownableField2 && ownableField2.isMortgaged()) {
                        counter++;
                    }
                    if (counter == 0)
                        player.haveMortgagedField = false;
                }
            }
        }
    }

    public void sellHotel(Player player, int i, PropertyField propertyField) {
        String action1 = "Ja";
        String action2 = "Nej";
        String choice = UI.getUserButtonPressed("Vil " + player.name + " sælge dit hotel?", action1, action2);
        if (choice.equals(action1)) {
            UI.setOrRemoveHotel(false, i);
            for (int j = 0; j < GlobalValues.MAX_AMOUNT_OF_BUILDINGS; j++) {
                propertyField.sellBuilding(player);
            }
        }
    }

    public void sellHouse(Player player, int i, PropertyField propertyField) {
        int buildingCount = UI.getUserInteger("Hvor mange huse vil " + player.name + " sælge på " + propertyField.fieldName + "?" +
                        "\nHusk du kun kan pantsætte din grund, hvis du ikke har nogen bebyggelse på denne grund"
                , 1, propertyField.amountOfBuildings);

        UI.updateAmountOfHouses(buildingCount, i);

        for (int j = 0; j < buildingCount; j++) {
            propertyField.sellBuilding(player);
        }
    }

    /**
     * Pantsætning af ejendomme
     *
     * @param player       Den spiller der kan pantsætte sin ejendom
     * @param i            Index på den ejendom der kan pantsættes
     * @param ownableField Den ejendom der kan pantsættes
     */
    public void mortgageField(Player player, int i, OwnableField ownableField) {
        String action1 = "Ja";
        String action2 = "Nej";
        String choice = UI.getUserButtonPressed("Vil " + player.name + " pantsætte " + ownableField.fieldName + " for "
                + (ownableField.price / 2) + " kr?", action1, action2);
        if (choice.equals(action1)) {
            // Bruges i fieldAction til OwnableField
            ownableField.setMortgaged(true);
            // Tjekkes hver tur

            UI.viewAsMortgaged(player, i);
        }
    }

    /**
     * Tjekker om spilleren har fået en ekstra tur ved at slå 2 ens.
     * Hvis de har slået 2 ens 3 gange i træk ryger de i fængsel.
     *
     * @param player Den spiller der evt. får en ekstra tur
     */
    private void doExtraTurn(Player player) {
        extraTurn = false;
        if (turnCounter < 2) {
            turnCounter++;
            UI.showMessage("Du har slået 2 ens. Du får en tur til!");
            playTurn(player);
        }
        else {
            UI.showMessage("Du har slået 2 ens 3 gange i træk. Du fængsles");
            player.putInJail();
            UI.updatePlayer(player);
            turnCounter = 0;
        }
    }

    /**
     * Hvis man går bankerot, får man ikke en ekstra tur uanset om man fx har slået to ens med terningerne.<p>
     * Spillerens brik fjernes fra spillebrættet. Og alle spillerens grunde sættes på auktion.
     * Hvis ingen af de tilbageværende spillere deltager i auktionen bliver grundens farve og ejer sat til null.<p>
     * Der oprettes et midlertidigt array som indeholder spillerlisten minus spilleren som er gået bankerot. <p>
     * Den bankerotte spiller tilføjes til et nyt array, som holder styr på ranklisten af spillerne,
     * som bruges til slut når spillet udskriver hvem der har vundet.
     *
     * @param player Den spiller som er gået bankerot
     */
    private void updateGameWhenPlayerGoBankerupt(Player player) {
        extraTurn = false;
        afterAuction = false;

        UI.showMessage(player.name + " har mistet alle dine penge og har derfor tabt spillet");
        UI.removeCar(player);

        for (int i = 0; i < gameBoard.fields.length; i++) {
            Field f = gameBoard.fields[i];
            if (f instanceof OwnableField ownableField && player == ownableField.owner) {
                updateOwnerOfBankeruptPlayersFields(player, i, ownableField);
            }
        }
        tmpPlayerList = removeElementFromOldArray(tmpPlayerList, player.getIndex());
        playerRankList = makePlayerRankArray(playerRankList, player);
    }

    /**
     * Sætter alle felter, som den bankerotte spiller ejer på auktion. <br>
     * Byder ingen på hans felter eller er der kun 1 spiller tilbage i spillet, ryddes den bankerotte spillers grund
     *
     * @param player       Den spiller som er gået bankerot
     * @param i            index på det felt, hvis ejer og farve som skal opdateres
     * @param ownableField Det felt som ejes af den spiller der er gået bankerot
     */
    private void updateOwnerOfBankeruptPlayersFields(Player player, int i, OwnableField ownableField) {
        if (tmpPlayerList.length > 2)
            doAuction(player, ownableField);

        if (ownableField.owner == player) {
            ownableField.owner = null;
            UI.removeOwner(i);
        }
    }

    private void checkInstanceOf(Player player, int faceValue, Field landedOn) {
        if (landedOn instanceof IncomeTaxField incomeTaxField) {
            String action1 = "4000 kr.";
            String action2 = "10 %";
            String choice = UI.getUserButtonPressed("""
                    Du skal betale indkomstskat. Du har nu følgende valgmuligheder:
                    \t1. Betal 4000 kr.
                    \t2. Betal 10% af alle dine værdier""", action1, action2);

            if (choice.equals(action1))
                player.addAmountToBalance(-incomeTaxField.tax);
            else {
                player.addAmountToBalance(-(player.getNetWorth() / 100 * incomeTaxField.percent));
            }
        }
        else if (landedOn instanceof ChanceField) {
            ChanceCard chanceCard = drawChanceCard();
            UI.displayChanceCard(chanceCard);
            doCardAction(player, faceValue, chanceCard);
        }
        // if instanceof OwnableField
        else {
            updateOwnableFields(player, faceValue, landedOn);
        }
    }

    private int rollDice(Player player) {
        int faceValue2;
        int faceValue1;
        int faceValue;
        UI.getUserButtonPressed(player.name + " skal rulle med terningen!", "Rul");
        faceValue1 = die1.roll();
        faceValue2 = die2.roll();
        UI.setDice(faceValue1, 2, 8, faceValue2, 3, 8);
        faceValue = faceValue1 + faceValue2;

        if (faceValue1 == faceValue2) {
            extraTurn = true;
        }
        return faceValue;
    }

    private boolean rollDiceToDecideStartingOrder(HashMap<Integer, Player> dieValues) {
        boolean duplicates = false;
        for (Player player : playerList) {
            UI.getUserButtonPressed(player.name + " skal rulle med terningen for, " +
                    "at se hvem der skal starte!", "Rul");
            int rollResult1 = die1.roll();
            int rollResult2 = die2.roll();
            int rollResult = rollResult1 + rollResult2;
            UI.setDice(rollResult1, 2, 8, rollResult2, 3, 8);
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

    public void makeStartingOrderPlayerList() {
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
                UI.getUserButtonPressed("Der er ens antal øjne " + playerList[0].name + " skal rulle" +
                        " igen med terningen for hvem der skal starte!", "Rul");
            }
        }
        for (int i = 0; i < orderOfPlayers.length; i++) {
            playerList[i].setIndex(i);
        }
    }

    public void updateOwnableFields(Player player, int faceValue, Field landedOn) {
        if (landedOn instanceof OwnableField ownableField && !ownableField.isMortgaged()) {

            if (ownableField.owner == null) {
                // Køb felt og ændr farve

                String action1 = "Ja";
                String action2 = "Nej";
                String choice = UI.getUserButtonPressed("Du er landet på " + landedOn.fieldName +
                        ". Vil du købe denne ejendom?", action1, action2);

                if (choice.equals(action1)) {
                    player.addAmountToBalance(-ownableField.price);
                    // Ejendommen er mindre værd hvis den skal sælges igen
                    player.addToNetWorth(ownableField.price);
                    ownableField.owner = player;
                    UI.setOwner(player, player.getCurrentPos());

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
                // Har brug for en faceValue og står derfor ikke samme sted som shippingField
                if (ownableField instanceof BreweryField) {
                    Player owner = ownableField.owner;
                    int counter = 0;
                    for (BreweryField f : gameBoard.breweryFields)
                        if (f.owner == owner)
                            counter++;

                    ownableField.rent = faceValue * (100 * counter);
                }
            }
        }
        UI.updatePlayer(player);
    }

    private void doCardAction(Player player, int faceValue, ChanceCard chanceCard) {
        Field landedOn;
        int tmpPos = player.getCurrentPos();
        chanceCard.cardAction(player, gameBoard);

        if (chanceCard instanceof MovementCard) {
            UI.updatePlayer(player);

            // Sørger for at man laver den handling der svarer til det felt man lander på
            landedOn = gameBoard.fields[player.getCurrentPos()];
            landedOn.fieldAction(player);
            checkInstanceOf(player, faceValue, landedOn);
        }
        UI.getUserButtonPressed("Tryk OK for at fortsætte", "OK");
        // Sørger for at man ikke trækker et nyt chancekort, hvis man ikke rykker sig
        if (player.getCurrentPos() != tmpPos)
            updateOwnableFields(player, faceValue, gameBoard.fields[player.getCurrentPos()]);
    }

    /**
     * @param player       Der ikke har købt feltet eller som skal af med feltet
     * @param ownableField Det felt der bydes på
     */
    public void doAuction(Player player, OwnableField ownableField) {
        UI.showMessage("Alle andre spillere har nu mulighed for at byde på " + ownableField.fieldName +
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
        String action1 = "Ja";
        String action2 = "Nej";

        for (Player p : tmpPlayerList) {
            p.wantToTryBidding = true;
            if (p == player || p.inJail || p.getBalance() < ownableField.price) {

                p.wantToTryBidding = false;

                if (numOfPlayersBidding == 1) {
                    String choice = UI.getUserButtonPressed(p.name + " er den eneste der kan være med i auktionen. Vil " + p.name + " købe "
                            + ownableField.fieldName + " til mindsteprisen, som er " + ownableField.price + ".", action1, action2);
                    if (choice.equals(action2)) return 0;
                    else return 1;
                }
                else if (numOfPlayersBidding == 0) return 0;

                numOfPlayersBidding -= 1;
            }

            if (p.wantToTryBidding) {
                String choice = UI.getUserButtonPressed("Vil " + p.name + " byde på "
                        + ownableField.fieldName + "? Buddet starter på " + ownableField.price + ".", action1, action2);
                if (choice.equals(action2)) {
                    p.wantToTryBidding = false;
                    numOfPlayersBidding -= 1;
                }

            }
        }
        return numOfPlayersBidding;
    }

    /**
     * @param ownableField Det felt der sættes på auktion
     */
    public void bidOnAuction(OwnableField ownableField, int numOfPlayersBidding) {
        String action1 = "Ja";
        String action2 = "Nej";
        int prevBid = ownableField.price;
        int bid;
        Player prevPlayer = null;

        while (numOfPlayersBidding > 1) {
            for (Player p : tmpPlayerList) {
                if (numOfPlayersBidding == 1) break;

                if (p.wantToTryBidding) {

                    if (p.getBalance() < prevBid && prevPlayer != p) {
                        UI.showMessage(p.name + " har desværre ikke nok penge til at overbyde den forrige spiller, " +
                                "og udgår derfor fra denne auktion");
                        p.wantToTryBidding = false;
                        numOfPlayersBidding -= 1;
                        continue;
                    }
                    if (prevPlayer != null && prevPlayer != p) {
                        String choice = UI.getUserButtonPressed(prevPlayer.name + " bød " + prevBid + " kr. " +
                                "Vil " + p.name + " stadig byde på " + ownableField.fieldName + "?", action1, action2);
                        if (choice.equals(action2)) {
                            p.wantToTryBidding = false;
                            numOfPlayersBidding -= 1;
                            continue;
                        }

                        bid = UI.getUserInteger("Hvad vil " + p.name + " byde på " + ownableField.fieldName
                                + "? Buddet starter på " + (prevBid + 1) + " kr.", (prevBid + 1), p.getBalance());
                    }
                    else {
                        bid = UI.getUserInteger("Hvad vil " + p.name + " byde på " + ownableField.fieldName
                                + "? Buddet starter på " + prevBid + " kr.", prevBid, p.getBalance());
                    }
                    // Det er ikke muligt at byde lavere. Derfor er dette ikke inkluderet i if statementet
                    while (bid == prevBid && prevPlayer != null && prevPlayer != p) {
                        bid = UI.getUserInteger("Dette bud er ugyldigt. Giv et nyt bud" +
                                "\nHvad vil " + p.name + " byde på " + ownableField.fieldName + "? Buddet starter på "
                                + prevBid + " kr.", prevBid, p.getBalance());
                    }
                    prevPlayer = p;
                    prevBid = bid;
                }
            }
        }

        for (Player p : tmpPlayerList) {
            if (p.wantToTryBidding) {
                ownableField.owner = p;
                p.addAmountToBalance(-prevBid);
                // Ejendommen er mindre værd hvis den skal sælges igen
                p.addToNetWorth(ownableField.price / 2);
                UI.updatePlayerBalance(p);

                for (int i = 0; i < gameBoard.fields.length; i++) {
                    Field f = gameBoard.fields[i];
                    if (f == ownableField) {
                        UI.setOwner(p, i);
                    }
                }
                UI.showMessage("Tillykke! " + p.name + " har købt " + ownableField.fieldName + " til " + prevBid + " kr.");
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
        UI.updatePlayer(player);
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
        StringBuilder winnerMessage = new StringBuilder();

        if (tmpPlayerList.length == 1) {

            for (int i = playerRankList.length - 1, k = 1; i >= 0; i--, k++) {
                Player player = playerRankList[i];

                winnerMessage.append(player.name).append(" fik ").append(k).append(". pladsen.\n");

            }

            UI.showMessage(winnerMessage.append("\nSpillet er slut!\n").append(winner.name).
                    append(" er den eneste spiller tilbage og\n").append(winner.name).
                    append(" har derfor vundet med ").append(winner.getBalance()).append(" kr.\n").toString());
        }
        else {
            for (Player player : quickGamePlayerList) {
                winnerMessage.append(player.name).append(" har ").append(player.getBalance()).append(" kr.\n");
            }

            winnerMessage.append(winner.name).append(" har vundet! ");
            UI.showMessage(winnerMessage.toString());
        }

        UI.showMessage("Tryk \"OK\" for at lukke spillet");
        UI.close();
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