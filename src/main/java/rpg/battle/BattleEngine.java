package rpg.battle;

import rpg.battle.actions.AttackAction;
import rpg.battle.actions.DefendAction;
import rpg.battle.actions.PotionAction;
import rpg.battle.actions.RunAction;
import rpg.battle.actions.SpecialAction;
import rpg.items.ItemType;
import rpg.model.Enemy;
import rpg.model.Player;
import rpg.ui.GameUI;

import java.util.List;

public class BattleEngine {
    private final GameUI ui;

    public BattleEngine(GameUI ui) {
        this.ui = ui;
    }

    public BattleOutcome fight(Player player, Enemy enemy) {
        BattleContext ctx = new BattleContext(player, enemy, ui);

        List<BattleAction> actions = List.of(
                new AttackAction(),
                new DefendAction(),
                new PotionAction(ItemType.HEALING_POTION),
                new SpecialAction(),
                new RunAction(35)
        );

        ui.println("\n=== BATTLE START ===");
        ui.println(player.getName() + " (" + player.getPlayerClass() + ") vs " +
                enemy.getName() + " (" + enemy.getType() + ")");

        while (!ctx.isEnded()) {
            ui.println("\n--- STATUS ---");
            ui.println(player.getName() + " (Lv " + player.getLevel() + ") HP: " +
                    player.getHp() + "/" + player.getMaxHp());
            ui.println(enemy.getName() + " HP: " + enemy.getHp() + "/" + enemy.getMaxHp());

            // Refresh JavaFX HUD (HP + XP + potions)
            refreshHud(player, enemy);

            // --------------------
            // Player turn
            // --------------------
            List<String> labels = actions.stream()
                    .map(BattleAction::label)
                    .toList();

            int choice = ui.chooseOption("Your turn:", labels);
            actions.get(choice).execute(ctx);

            // Refresh after action (potion use, etc.)
            refreshHud(player, enemy);

            // If enemy died due to the player's action, end as WIN
            if (enemy.isDefeated()) {
                ctx.end(BattleOutcome.WIN);
                break;
            }

            // --------------------
            // Enemy turn
            // --------------------
            ui.println("\nEnemy turn:");

            int raw = BattleRules.rollDamage(enemy.getAttack(), player.getDefense());

            int finalDmg;
            if (ctx.isPlayerDefending()) {
                finalDmg = (int) Math.round(raw * ctx.getPlayerDamageMultiplier());
                if (finalDmg < 1) finalDmg = 1;
                ui.println("You defended! Damage reduced.");
                ctx.resetPlayerDefense();
            } else {
                finalDmg = raw;
            }

            player.takeDamage(finalDmg);
            ui.println(enemy.getName() + " hits you for " + finalDmg + " damage.");

            // Refresh after enemy hit
            refreshHud(player, enemy);

            // If player died after enemy hit, end as LOSE
            if (player.isDefeated()) {
                ctx.end(BattleOutcome.LOSE);
                break;
            }

            player.tickCooldowns();
        }

        // --------------------
        // Battle result
        // --------------------
        ui.println("\n=== BATTLE END ===");
        BattleOutcome outcome = ctx.getOutcome();

        if (outcome == BattleOutcome.WIN) {
            int reward = enemy.getType().xpReward;
            ui.println("You won! XP reward: " + reward);

            int oldLevel = player.getLevel();
            player.gainXp(reward);

            // Refresh to reflect new XP / new level
            refreshHud(player, enemy);

            ui.println("Your XP: " + player.getXp() + "/" + player.xpToNextLevel());
            if (player.getLevel() > oldLevel) {
                ui.println("LEVEL UP! You are now level " + player.getLevel() + "!");
                ui.println("New stats -> HP: " + player.getHp() + "/" + player.getMaxHp()
                        + " | ATK: " + player.getAttack()
                        + " | DEF: " + player.getDefense());
            }
        } else if (outcome == BattleOutcome.ESCAPED) {
            ui.println("You escaped. No XP gained.");
        } else {
            ui.println("You lost...");
        }

        return outcome;
    }

    private void refreshHud(Player player, Enemy enemy) {
        ui.updatePlayerStatus(
                player.getName() + " (Lv " + player.getLevel() + ") HP: " + player.getHp() + "/" + player.getMaxHp(),
                (double) player.getHp() / player.getMaxHp()
        );

        ui.updateEnemyStatus(
                enemy.getName() + " HP: " + enemy.getHp() + "/" + enemy.getMaxHp(),
                (double) enemy.getHp() / enemy.getMaxHp()
        );

        int xpNeed = player.xpToNextLevel();
        double xpPercent = xpNeed <= 0 ? 0 : (double) player.getXp() / xpNeed;

        int potions = player.getInventory().getCount(ItemType.HEALING_POTION);

        ui.updatePlayerProgress(
                "XP: " + player.getXp() + "/" + xpNeed,
                xpPercent,
                potions
        );
    }
}
