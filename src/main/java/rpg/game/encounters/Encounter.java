package rpg.game.encounters;

import rpg.model.Player;
import rpg.ui.GameUI;

public interface Encounter {
    String name();
    void run(Player player, GameUI ui);
}
