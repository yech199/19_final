package Model;

/**
 * Terning initieres med Model.Die(int, int) for at parametre for terningen.
 * Der bruges .roll til at vælge et tilfældigt tal mellem disse parametre.
 */

public class Die {
    private final int MINfaceValue; //starter med værdien 0
    private final int MAXfaceValue;
    private int faceValue;

    public Die(int MINfaceValue, int MAXfaceValue) {
        this.MINfaceValue = MINfaceValue;
        this.MAXfaceValue = MAXfaceValue;
    }

    public int roll() { //funktionen roll ruller en terning med x-antal sider.
        // Metode der kan køre når man har lavet et Dice objekt
        faceValue = (int) (Math.floor(Math.random() * (this.MAXfaceValue - this.MINfaceValue + 1) + this.MINfaceValue));
        return faceValue; //Maksimum værdi på terningen = 6. Minimum værdi på terningen = 1
        // Maksimum værdi på terningen = 6.
        // Minimum værdi på terningen = 1
    }
}
