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
    private int balance;
    public boolean inJail;
    public boolean getOutOfJailFree;

    public Player(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    /**
     * Constructor der laver en player med et navn og en startbalance på 0
     *
     * @param name of the Player being made
     */
    public Player(String name) {
        this(name, GlobalValues.START_MONEY);
    }

    /**
     * Metode der tilføjer et positivt eller negativt beløb til spillerens pengebeholdning. Fx når spilleren køber en forlystelse
     *
     * @param amount to add to the players balance
     */
    public void addAmountToBalance(int amount) {
        this.balance += amount;
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
}