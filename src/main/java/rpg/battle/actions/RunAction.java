package rpg.battle.actions;

import rpg.battle.BattleAction;
import rpg.battle.BattleContext;
import rpg.battle.BattleOutcome;
import rpg.util.Rng;

public class RunAction implements BattleAction {
    private final int escapeChancePercent;

    public RunAction(int escapeChancePercent) {
        if (escapeChancePercent < 0 || escapeChancePercent > 100) {
            throw new IllegalArgumentException("escapeChancePercent must be 0..100");
        }
        this.escapeChancePercent = escapeChancePercent;
    }

    @Override
    public String label() {
        return "Run (" + escapeChancePercent + "% chance)";
    }

    @Override
    public void execute(BattleContext ctx) {
        if (Rng.chance(escapeChancePercent)) {
            ctx.getUi().println("You escaped successfully!");
            ctx.end(BattleOutcome.ESCAPED);
        } else {
            ctx.getUi().println("You tried to run, but failed!");
        }
    }
}
