package rpg.model;

public abstract class Character implements Damageable {

    // Identity
    private final String name;

    // Base stats (never change)
    private final int baseMaxHp;
    private final int baseAttack;
    private final int baseDefense;

    // Modifiers (can be positive or negative: buffs/debuffs, upgrades/penalties)
    private int maxHpModifier = 0;
    private int attackModifier = 0;
    private int defenseModifier = 0;

    // Current state
    private int hp;

    protected Character(String name, int baseMaxHp, int baseAttack, int baseDefense) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
        if (baseMaxHp <= 0) throw new IllegalArgumentException("baseMaxHp must be > 0");
        if (baseAttack < 0) throw new IllegalArgumentException("baseAttack cannot be negative");
        if (baseDefense < 0) throw new IllegalArgumentException("baseDefense cannot be negative");

        this.name = name.trim();
        this.baseMaxHp = baseMaxHp;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;

        // Start full HP based on current effective max HP
        this.hp = getMaxHp();
    }

    // --------------------
    // Effective stats (base + modifiers)
    // --------------------
    public String getName() {
        return name;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public int getMaxHp() {
        return baseMaxHp + maxHpModifier;
    }

    public int getAttack() {
        return baseAttack + attackModifier;
    }

    public int getDefense() {
        return baseDefense + defenseModifier;
    }

    public boolean isAlive() {
        return !isDefeated();
    }

    public int getMaxHpModifier() { return maxHpModifier; }
    public int getAttackModifier() { return attackModifier; }
    public int getDefenseModifier() { return defenseModifier; }

    // --------------------
    // Combat state changes
    // --------------------
    @Override
    public void takeDamage(int dmg) {
        if (dmg < 0) throw new IllegalArgumentException("Damage cannot be negative");
        hp = Math.max(0, hp - dmg);
    }

    public void heal(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Heal amount cannot be negative");
        hp = Math.min(getMaxHp(), hp + amount);
    }

    public void healToFull() {
        hp = getMaxHp();
    }

    // --------------------
    // Modifiers (buffs/debuffs, upgrades/penalties)
    // --------------------
    public void addMaxHpModifier(int delta) {
        if (delta == 0) return;

        int old = maxHpModifier;
        maxHpModifier += delta;

        // Ensure effective max HP remains valid
        if (getMaxHp() <= 0) {
            maxHpModifier = old;
            throw new IllegalArgumentException("Resulting maxHp must stay > 0");
        }

        clampHpToValidRange();
    }

    public void addAttackModifier(int delta) {
        if (delta == 0) return;

        int old = attackModifier;
        attackModifier += delta;

        // Ensure effective attack remains valid (no negative attack)
        if (getAttack() < 0) {
            attackModifier = old;
            throw new IllegalArgumentException("Resulting attack cannot be negative");
        }
    }

    public void addDefenseModifier(int delta) {
        if (delta == 0) return;

        int old = defenseModifier;
        defenseModifier += delta;

        // Ensure effective defense remains valid (no negative defense)
        if (getDefense() < 0) {
            defenseModifier = old;
            throw new IllegalArgumentException("Resulting defense cannot be negative");
        }
    }

    // --------------------
    // Internal helper
    // --------------------
    private void clampHpToValidRange() {
        // Ensures: 0 <= hp <= maxHp
        hp = Math.max(0, Math.min(hp, getMaxHp()));
    }
}
