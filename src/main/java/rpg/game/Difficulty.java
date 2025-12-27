package rpg.game;

public enum Difficulty {
    EASY(0.90, 0.90, 0.90, 50),
    NORMAL(1.00, 1.00, 1.00, 35),
    HARD(1.15, 1.15, 1.10, 25);

    public final double hpMult;
    public final double atkMult;
    public final double defMult;
    public final int escapeChancePercent;

    Difficulty(double hpMult, double atkMult, double defMult, int escapeChancePercent) {
        this.hpMult = hpMult;
        this.atkMult = atkMult;
        this.defMult = defMult;
        this.escapeChancePercent = escapeChancePercent;
    }
}
