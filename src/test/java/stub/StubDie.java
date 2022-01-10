package stub;

import Model.Die;

public class StubDie extends Die {
    int[] futureRolls;
    int rollIndex = 0;
    private boolean unlimited = false;

    public StubDie(int[] futureRolls) {
        super(0, 0);
        this.futureRolls = futureRolls;
    }

    public StubDie(int onlyValue) {
        super(0, 0);
        this.unlimited = true;
        this.rollIndex = onlyValue;
    }

    @Override
    public int roll() {
        return unlimited ? rollIndex : futureRolls[rollIndex++];
    }
}