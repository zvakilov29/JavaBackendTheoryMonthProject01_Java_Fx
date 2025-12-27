package rpg.items;

public enum ItemType {
    HEALING_POTION("Healing Potion", 20);

    private final String displayName;
    private final int healAmount;

    ItemType(String displayName, int healAmount) {
        this.displayName = displayName;
        this.healAmount = healAmount;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getHealAmount() {
        return healAmount;
    }
}