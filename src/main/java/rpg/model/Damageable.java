package rpg.model;

public interface Damageable {
    int getHp();
    int getMaxHp();
    void takeDamage(int dmg);

    default boolean isDefeated() {
        return getHp() <= 0;
    }
}
