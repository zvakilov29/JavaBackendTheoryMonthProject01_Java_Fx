package rpg.battle;

import rpg.model.Enemy;
import rpg.model.Player;
import rpg.ui.GameUI;

public class BattleContext {
    private final Player player;
    private final Enemy enemy;
    private final GameUI ui;

    // Battle state flags
    private boolean playerDefending = false;
    private double playerDamageMultiplier = 1.0; // used to reduce damage taken

    private BattleOutcome outcome = null;


    public BattleContext(Player player, Enemy enemy, GameUI ui) {
        this.player = player;
        this.enemy = enemy;
        this.ui = ui;
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public GameUI getUi() {
        return ui;
    }

    // --------------------
    // Defend mechanic
    // --------------------
    public void setPlayerDefending(boolean defending) {
        this.playerDefending = defending;
    }

    public boolean isPlayerDefending() {
        return playerDefending;
    }

    public void setPlayerDamageMultiplier(double multiplier) {
        if (multiplier <= 0) throw new IllegalArgumentException("Multiplier must be > 0");
        this.playerDamageMultiplier = multiplier;
    }

    public double getPlayerDamageMultiplier() {
        return playerDamageMultiplier;
    }

    public void resetPlayerDefense() {
        playerDefending = false;
        playerDamageMultiplier = 1.0;
    }

    // --------------------
    // Battle outcome logic
    // --------------------
    public boolean isEnded() {
        return outcome != null;
    }

    public BattleOutcome getOutcome() {
        return outcome;
    }

    public void end(BattleOutcome outcome) {
        if (outcome == null) throw new IllegalArgumentException("outcome cannot be null");
        this.outcome = outcome;
    }
}
