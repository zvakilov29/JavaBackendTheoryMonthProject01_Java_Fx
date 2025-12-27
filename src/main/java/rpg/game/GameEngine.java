package rpg.game;

import rpg.battle.BattleEngine;
import rpg.battle.BattleOutcome;
import rpg.game.encounters.Encounter;
import rpg.game.encounters.EncounterRoller;
import rpg.game.encounters.MerchantEncounter;
import rpg.game.encounters.ShrineEncounter;
import rpg.model.Enemy;
import rpg.model.Player;
import rpg.persistence.SaveManager;
import rpg.ui.GameUI;

import java.io.IOException;
import java.util.List;

public class GameEngine {

    private final GameUI ui;
    private final BattleEngine battleEngine;
    private final EnemyFactory enemyFactory = new EnemyFactory();
    private final EncounterRoller encounterRoller =
            new EncounterRoller(List.of(new MerchantEncounter(), new ShrineEncounter()));

    private final SaveManager saveManager;

    public GameEngine(GameUI ui, BattleEngine battleEngine, SaveManager saveManager) {
        this.ui = ui;
        this.battleEngine = battleEngine;
        this.saveManager = saveManager;
    }

    public void run(Player player, Difficulty difficulty) {
        ui.println("\n=== GAME START ===");
        ui.println("Difficulty: " + difficulty);

        int fightsWon = 0;

        while (true) {
            Enemy enemy = enemyFactory.createEnemy(difficulty, player.getLevel());

            ui.println("\n--- A wild enemy appears! ---");
            ui.println(enemy.getName() + " (" + enemy.getType() + ")");

            BattleOutcome outcome = battleEngine.fight(player, enemy);

            if (outcome == BattleOutcome.WIN) {
                fightsWon++;
                ui.println("Fights won: " + fightsWon);

                Encounter enc = encounterRoller.roll();
                if (enc != null) {
                    ui.println("\nAn encounter begins: " + enc.name());
                    enc.run(player, ui);
                } else {
                    ui.println("\nNo encounter this time. You move on...");
                }

                autoSave(player);

            } else if (outcome == BattleOutcome.ESCAPED) {
                ui.println("You escaped. The run ends here (for now).");
                autoSave(player); // optional but nice
                break;

            } else {
                ui.println("Game Over. Total fights won: " + fightsWon);
                break;
            }

            // Optional stop condition for a “demo”
            if (fightsWon >= 5) {
                ui.println("You cleared 5 fights! Demo complete.");
                autoSave(player);
                break;
            }
        }
    }

    private void autoSave(Player player) {
        try {
            saveManager.save(player);
            ui.println("(Auto-saved)");
        } catch (IOException e) {
            ui.println("WARNING: Failed to save game: " + e.getMessage());
        }
    }
}
