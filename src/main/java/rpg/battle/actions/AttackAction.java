package rpg.battle.actions;

import rpg.battle.BattleAction;
import rpg.battle.BattleContext;
import rpg.battle.BattleRules;

public class AttackAction implements BattleAction {
    @Override
    public String label() {
        return "Attack";
    }

    @Override
    public void execute(BattleContext ctx) {
        int dmg = BattleRules.performAttack(ctx.getPlayer(), ctx.getEnemy());
        ctx.getUi().println("You hit " + ctx.getEnemy().getName() + " for " + dmg + " damage.");
    }
}
