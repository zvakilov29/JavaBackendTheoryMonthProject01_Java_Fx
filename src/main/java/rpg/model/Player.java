package rpg.model;

import rpg.items.Inventory;
import rpg.items.ItemType;
import rpg.model.enums.PlayerClass;
import rpg.skills.Skill;

import java.util.EnumMap;
import java.util.Map;

public class Player extends Character {
    private final PlayerClass playerClass;
    private final Inventory inventory = new Inventory();


    private int level = 1;
    private int xp = 0;

    private final Skill skill;
    private int skillCooldownRemaining = 0;

    public Player(String name, PlayerClass playerClass) {
        super(name, playerClass.baseMaxHp, playerClass.baseAttack, playerClass.baseDefense);
        this.playerClass = playerClass;
        inventory.add(ItemType.HEALING_POTION, 3);
        this.skill = playerClass.defaultSkill;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public Inventory getInventory() {
        return inventory;
    }

    // Skills-related logic
    public Skill getSkill() {
        return skill;
    }

    public int getSkillCooldownRemaining() {
        return skillCooldownRemaining;
    }

    public boolean canUseSkill() {
        return skillCooldownRemaining == 0;
    }

    // Adds XP and performs level-ups if needed.
    public void gainXp(int amount) {
        if (amount < 0) throw new IllegalArgumentException("XP amount cannot be negative");
        xp += amount;

        // Level up while XP is enough (in case you gain a lot at once)
        while (xp >= xpToNextLevel()) {
            xp -= xpToNextLevel();
            levelUp();
        }
    }

    /**
     * Increasing difficulty: quadratic XP growth.
     * Example: base + growth * level^2
     */
    public int xpToNextLevel() {
        int base = 50;
        int growth = 15;
        return base + growth * level * level;
    }

    private void levelUp() {
        level++;

        // Simple stat upgrades each level
        addMaxHpModifier(5);
        addAttackModifier(2);
        addDefenseModifier(1);

        // Nice feel: heal to full on level up
        healToFull();
    }

    // Skills-related logic
    public void startSkillCooldown() {
        skillCooldownRemaining = skill.cooldownTurns();
    }

    public void tickCooldowns() {
        if (skillCooldownRemaining > 0) skillCooldownRemaining--;
    }

    public void restoreProgress(int level, int xp) {
        if (level < 1) throw new IllegalArgumentException("level must be >= 1");
        if (xp < 0) throw new IllegalArgumentException("xp must be >= 0");
        this.level = level;
        this.xp = xp;
    }
}
