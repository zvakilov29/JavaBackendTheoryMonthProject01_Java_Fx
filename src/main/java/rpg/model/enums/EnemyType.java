package rpg.model.enums;

public enum EnemyType {
    GOBLIN("Goblin", 40, 8, 2, 20),
    SKELETON("Skeleton", 45, 7, 3, 25),
    WOLF("Wolf", 35, 9, 1, 18);

    public final String displayName;
    public final int baseMaxHp;
    public final int baseAttack;
    public final int baseDefense;
    public final int xpReward;

    EnemyType(String displayName, int baseMaxHp, int baseAttack, int baseDefense, int xpReward) {
        this.displayName = displayName;
        this.baseMaxHp = baseMaxHp;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.xpReward = xpReward;
    }
}
