package rpg.skills.implementations;

import rpg.battle.BattleContext;
import rpg.battle.BattleRules;
import rpg.model.Player;
import rpg.skills.Skill;
import rpg.util.Rng;

public class Backstab implements Skill {

    @Override
    public String name() {
        return "Backstab";
    }

    @Override
    public int cooldownTurns() {
        return 2;
    }

    @Override
    public void use(BattleContext ctx) {
        Player p = ctx.getPlayer();

        int dmg = BattleRules.rollDamage(p.getAttack() + 1, ctx.getEnemy().getDefense());

        // 35% chance to crit (double damage)
        if (Rng.chance(35)) {
            dmg *= 2;
            ctx.getUi().println("CRITICAL!");
        }

        ctx.getEnemy().takeDamage(dmg);
        ctx.getUi().println("You used " + name() + " for " + dmg + " damage.");
    }
}
