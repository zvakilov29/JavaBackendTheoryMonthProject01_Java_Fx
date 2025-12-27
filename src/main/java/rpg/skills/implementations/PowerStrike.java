package rpg.skills.implementations;

import rpg.battle.BattleContext;
import rpg.battle.BattleRules;
import rpg.model.Player;
import rpg.skills.Skill;

public class PowerStrike implements Skill {

    @Override
    public String name() {
        return "Power Strike";
    }

    @Override
    public int cooldownTurns() {
        return 3;
    }

    @Override
    public void use(BattleContext ctx) {
        Player p = ctx.getPlayer();
        int raw = BattleRules.rollDamage(p.getAttack() + 5, ctx.getEnemy().getDefense());
        ctx.getEnemy().takeDamage(raw);
        ctx.getUi().println("You used " + name() + "! Massive hit for " + raw + " damage.");
    }
}
