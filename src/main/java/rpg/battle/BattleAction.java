package rpg.battle;

public interface BattleAction {
    String label();
    void execute(BattleContext ctx);
}
