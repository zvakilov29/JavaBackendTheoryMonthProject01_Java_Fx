package rpg.model;

import rpg.model.enums.EnemyType;

public class Enemy extends Character {
    private final EnemyType type;

    public Enemy(EnemyType type, int maxHp, int attack, int defense) {
        super(type.name(), maxHp, attack, defense);
        this.type = type;
    }

    public EnemyType getType() {
        return type;
    }
}
