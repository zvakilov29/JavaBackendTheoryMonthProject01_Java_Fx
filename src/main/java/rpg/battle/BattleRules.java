package rpg.battle;

import rpg.util.Rng;
import rpg.model.Character;

public final class BattleRules {
    private BattleRules() {}

    public static int rollDamage(int attack, int defense) {
        int base = Math.max(1, attack - defense);     // avoid infinite fights
        int percent = Rng.between(80, 120);           // 80%...120% variance
        int dmg = (base * percent) / 100;

        if (dmg == 0) dmg = 1; // safety for low base damage
        return dmg;
    }

    public static int performAttack(Character attacker, Character defender) {
        int dmg = rollDamage(attacker.getAttack(), defender.getDefense());
        defender.takeDamage(dmg);
        return dmg;
    }
}
