package rpg.skills;

import rpg.battle.BattleContext;

public interface Skill {
    String name();

    int cooldownTurns();

    // Executes the skill effect (damage/heal/buff etc.)
    void use(BattleContext ctx);
}
