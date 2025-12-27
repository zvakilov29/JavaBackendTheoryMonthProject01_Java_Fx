package rpg.fx;

import rpg.battle.BattleEngine;
import rpg.game.Difficulty;
import rpg.game.GameEngine;
import rpg.model.Player;
import rpg.model.enums.PlayerClass;
import rpg.persistence.SaveManager;
import rpg.ui.GameUI;

import java.io.IOException;
import java.util.List;

public class FxRunner {

    public static void run(GameUI ui) {
        SaveManager saveManager = new SaveManager("save.properties");

        Player player;

        // Save menu
        if (saveManager.exists()) {
            int choice = ui.chooseOption(
                    "Save file found. Choose:",
                    List.of("Continue", "New game (overwrite save)", "Delete save")
            );

            if (choice == 0) { // Continue
                try {
                    player = saveManager.load();
                    ui.println("Loaded save for: " + player.getName()
                            + " (" + player.getPlayerClass() + "), Lv " + player.getLevel());
                } catch (Exception e) {
                    ui.println("Failed to load save: " + e.getMessage());
                    ui.println("Starting a new game instead.");
                    player = createNewPlayer(ui);
                }
            } else if (choice == 1) { // New
                player = createNewPlayer(ui);
                trySave(ui, saveManager, player);
            } else { // Delete
                try {
                    saveManager.delete();
                    ui.println("Save deleted.");
                } catch (IOException e) {
                    ui.println("WARNING: Could not delete save: " + e.getMessage());
                }
                player = createNewPlayer(ui);
                trySave(ui, saveManager, player);
            }
        } else {
            player = createNewPlayer(ui);
            trySave(ui, saveManager, player);
        }

        // Difficulty
        int idx = ui.chooseOption("Choose difficulty:", List.of("EASY", "NORMAL", "HARD"));
        Difficulty difficulty = switch (idx) {
            case 0 -> Difficulty.EASY;
            case 1 -> Difficulty.NORMAL;
            default -> Difficulty.HARD;
        };

        BattleEngine battleEngine = new BattleEngine(ui);
        GameEngine gameEngine = new GameEngine(ui, battleEngine, saveManager);

        gameEngine.run(player, difficulty);

        ui.println("\n=== END ===");
    }

    private static Player createNewPlayer(GameUI ui) {
        String name = ui.readNonBlank("Enter your name:");
        int cls = ui.chooseOption("Choose your class:", List.of("WARRIOR", "MAGE", "ROGUE"));

        PlayerClass pc = switch (cls) {
            case 0 -> PlayerClass.WARRIOR;
            case 1 -> PlayerClass.MAGE;
            default -> PlayerClass.ROGUE;
        };

        return new Player(name, pc);
    }

    private static void trySave(GameUI ui, SaveManager saveManager, Player player) {
        try {
            saveManager.save(player);
            ui.println("(Saved new game)");
        } catch (IOException e) {
            ui.println("WARNING: Could not save: " + e.getMessage());
        }
    }
}
