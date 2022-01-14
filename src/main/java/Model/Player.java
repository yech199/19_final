package Model;

/**
 * Model.Player bruges til at holde styr på hver enkelt spiller.
 * Brug .addScore for at tilføje eller fjerne penge.
 */
public class Player {
    //------------------------------------------------------------------------------------------------------------------
    // Hvis previousPos er public kan den ændres af andre klasser, hvilket ikke er meningen. Derfor skal både den og
    // currentPos være private, da previousPos bliver ændret til currentPos.
    //------------------------------------------------------------------------------------------------------------------
    private int currentPos;
    private int previousPos;

    public final String name;
    private int index;
    private int balance;
    public int jailTryRollCounter;
    public boolean inJail = false;
    public boolean getOutOfJailFree;
    public boolean wantToTryBidding = true;
    public boolean haveMortgagedField = false;
    private int netWorth;

    /**
     * Alle constructerer fører tilbage til denne constructor.
     * Dermed har alle constructorer alt der skal bruges for at spilleren kan bruges alle steder
     */
    public Player(String name, int index, int balance) {
        this.name = name;
        this.balance = balance;
        this.index = index;
        this.jailTryRollCounter = 0;
        addToNetWorth(balance);
    }

    /**
     * Spiller der har custom-made navn og pengebeholdning (bruges til test)
     */
    public Player(String name, int balance) {
        this(name, 0, balance);
    }

    /**
     * Spiller der har custom-made index og navn
     *
     * @param index Player index i playerList i gameControlleren
     */
    public Player(int index, String name) {
        this(name, index, GlobalValues.START_MONEY);
    }

    public Player(String name) {
        this(-1, name);
    }

    /**
     * Metode der tilføjer et positivt eller negativt beløb til spillerens pengebeholdning. Fx når spilleren køber en forlystelse
     *
     * @param amount to add to the players balance
     */
    public void addAmountToBalance(int amount) {
        this.balance += amount;
        addToNetWorth(amount);
    }

    //------------------------------------------------------------------------------------------------------------------
    // get() og set() metoder til den enkelte spillers balance
    // Bruger variablen balance fra klassen Konto
    //------------------------------------------------------------------------------------------------------------------

    public int getBalance() {
        return this.balance;
    }

    public void setBalance(int newBalance) {
        this.balance = newBalance;
    }

    //------------------------------------------------------------------------------------------------------------------
    // get() og set() metoder til currentPos
    //------------------------------------------------------------------------------------------------------------------
    public int getCurrentPos() {
        return currentPos;
    }

    /**
     * Sætter spillerens nuværende position, og opdaterer den forrige position til den nuværende position.
     * Man behøver aldrig at sætte previousPos, da den sættes her.
     *
     * @param currentPos der hvor spilleren rykker hen, fx 4 felter frem fra previousPos
     */
    public void setCurrentPos(int currentPos) {
        this.previousPos = this.currentPos;
        this.currentPos = currentPos;
    }

    /**
     * @return Det felt, hvor spilleren står, lige før han rykker sin brik frem eller tilbage på brættet
     */
    public int getPreviousPos() {
        return previousPos;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    /**
     * Sætter spilleren i fængsel og opdaterer deres position i Model.
     * Opdaterer ikke deres position i GUI
     */
    public void putInJail(){
        inJail = true;
        setCurrentPos(GlobalValues.JAIL_INDEX);
    }

    public void addToNetWorth (int amount){
        netWorth += amount;
    }

    public int getNetWorth (){
        return netWorth;
    }
}