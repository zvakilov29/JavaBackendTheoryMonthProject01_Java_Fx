package rpg.skills.implementations;

import rpg.battle.BattleContext;
import rpg.battle.BattleRules;
import rpg.model.Player;
import rpg.skills.Skill;

public class Fireball implements Skill {

    @Override
    public String name() {
        return "Fireball";
    }

    @Override
    public int cooldownTurns() {
        return 3;
    }

    @Override
    public void use(BattleContext ctx) {
        Player p = ctx.getPlayer();

        // Fireball ignores part of defense (simple version)
        int effectiveDefense = Math.max(0, ctx.getEnemy().getDefense() - 3);

        int dmg = BattleRules.rollDamage(p.getAttack() + 2, effectiveDefense);
        ctx.getEnemy().takeDamage(dmg);

        ctx.getUi().println("You cast " + name() + " for " + dmg + " damage (partly ignores defense).");
    }
}
