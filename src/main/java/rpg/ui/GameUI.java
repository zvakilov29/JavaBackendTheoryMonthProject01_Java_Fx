package rpg.ui;

import java.util.List;

public interface GameUI {
    void println(String s);

    String readNonBlank(String prompt);

    int chooseOption(String prompt, List<String> options);

    // Optional UI enhancements (HUD). ConsoleUI can ignore these.
    default void updatePlayerStatus(String text, double hpPercent) { }
    default void updateEnemyStatus(String text, double hpPercent) { }
    default void updatePlayerProgress(String text, double xpPercent, int potions) { }
}
