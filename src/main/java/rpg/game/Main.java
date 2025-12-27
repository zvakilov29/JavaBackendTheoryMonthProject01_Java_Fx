package rpg.game;

import rpg.battle.BattleEngine;
import rpg.model.Player;
import rpg.persistence.SaveManager;
import rpg.ui.ConsoleUI;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        SaveManager saveManager = new SaveManager("save.properties");

        Player player;

        // -----------------------------
        // Save menu (Continue / New / Delete)
        // -----------------------------
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
            } else if (choice == 1) { // New game
                player = createNewPlayer(ui);
                try {
                    saveManager.save(player); // create initial save
                    ui.println("(Saved new game)");
                } catch (IOException e) {
                    ui.println("WARNING: Could not save: " + e.getMessage());
                }
            } else { // Delete save
                try {
                    saveManager.delete();
                    ui.println("Save deleted.");
                } catch (IOException e) {
                    ui.println("WARNING: Could not delete save: " + e.getMessage());
                }

                player = createNewPlayer(ui);
                try {
                    saveManager.save(player); // create initial save
                    ui.println("(Saved new game)");
                } catch (IOException e) {
                    ui.println("WARNING: Could not save: " + e.getMessage());
                }
            }
        } else {
            player = createNewPlayer(ui);
            try {
                saveManager.save(player); // create initial save
                ui.println("(Saved new game)");
            } catch (IOException e) {
                ui.println("WARNING: Could not save: " + e.getMessage());
            }
        }

        Difficulty difficulty = chooseDifficulty(ui);

        BattleEngine battleEngine = new BattleEngine(ui);
        GameEngine engine = new GameEngine(ui, battleEngine, saveManager);

        engine.run(player, difficulty);
    }

    private static Player createNewPlayer(ConsoleUI ui) {
        String name = ui.readNonBlank("Enter your name:");
        var playerClass = ui.choosePlayerClass();
        return new Player(name, playerClass);
    }

    private static Difficulty chooseDifficulty(ConsoleUI ui) {
        int idx = ui.chooseOption("Choose difficulty:", List.of("EASY", "NORMAL", "HARD"));
        return switch (idx) {
            case 0 -> Difficulty.EASY;
            case 1 -> Difficulty.NORMAL;
            default -> Difficulty.HARD;
        };
    }
}
