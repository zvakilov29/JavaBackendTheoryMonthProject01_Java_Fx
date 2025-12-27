package rpg.model.enums;

import rpg.skills.Skill;
import rpg.skills.implementations.Backstab;
import rpg.skills.implementations.Fireball;
import rpg.skills.implementations.PowerStrike;

public enum PlayerClass {
    WARRIOR(100, 12, 6, new PowerStrike()),
    MAGE(80, 14, 3, new Fireball()),
    ROGUE(90, 11, 4, new Backstab());

    public final int baseMaxHp;
    public final int baseAttack;
    public final int baseDefense;

    public final Skill defaultSkill;

    PlayerClass(int baseMaxHp, int baseAttack, int baseDefense, Skill defaultSkill) {
        this.baseMaxHp = baseMaxHp;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.defaultSkill = defaultSkill;
    }
}
