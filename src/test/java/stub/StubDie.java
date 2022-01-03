package stub;

import Model.Die;

public class StubDie extends Die {
    int[] futureRolls;
    int rollIndex = 0;

    public StubDie(int[] futureRolls) {
        super(0, 0);
        this.futureRolls = futureRolls;
    }

    @Override
    public int roll() {
        return futureRolls[rollIndex++];
    }
}