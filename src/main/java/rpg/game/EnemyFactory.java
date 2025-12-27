package rpg.game;

import rpg.model.Enemy;
import rpg.model.enums.EnemyType;
import rpg.util.Rng;

public class EnemyFactory {

    public Enemy createEnemy(Difficulty difficulty, int playerLevel) {
        EnemyType[] all = EnemyType.values();
        EnemyType type = all[Rng.between(0, all.length - 1)];

        // Simple scaling: difficulty multiplier + small growth per player level
        int hp = (int) Math.round(type.baseMaxHp * difficulty.hpMult + (playerLevel - 1) * 5);
        int atk = (int) Math.round(type.baseAttack * difficulty.atkMult + (playerLevel - 1) * 1);
        int def = (int) Math.round(type.baseDefense * difficulty.defMult + (playerLevel - 1) * 1);

        return new Enemy(type, hp, atk, def);
    }
}
