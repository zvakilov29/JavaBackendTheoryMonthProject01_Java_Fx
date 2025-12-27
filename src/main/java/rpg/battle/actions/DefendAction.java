package rpg.battle.actions;

import rpg.battle.BattleAction;
import rpg.battle.BattleContext;

public class DefendAction implements BattleAction {
    @Override
    public String label() {
        return "Defend (reduce next hit)";
    }

    @Override
    public void execute(BattleContext ctx) {
        ctx.setPlayerDefending(true);
        ctx.setPlayerDamageMultiplier(0.5); // take 50% damage on next enemy attack
        ctx.getUi().println("You brace for impact. Next enemy hit will be reduced.");
    }
}
