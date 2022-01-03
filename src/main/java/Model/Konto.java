package Model;

/**
 * Model.Konto holder på en spillers pengebeholdning.
 * Spillerklassen skaber automatisk en tilhørende konto-klasse ved skabelse.
 */

public class Konto {
    private int balance;

    public Konto(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
