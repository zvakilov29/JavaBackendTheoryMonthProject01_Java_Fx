package rpg.ui;

import java.util.List;

public interface GameUI {
    void println(String s);

    String readNonBlank(String prompt);

    int chooseOption(String prompt, List<String> options);

    // NEW (optional UI enhancements)
    default void updatePlayerStatus(String text, double hpPercent) {}
    default void updateEnemyStatus(String text, double hpPercent) {}
}
