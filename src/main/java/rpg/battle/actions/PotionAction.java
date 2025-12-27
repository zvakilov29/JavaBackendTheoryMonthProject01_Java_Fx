package rpg.battle.actions;

import rpg.battle.BattleAction;
import rpg.battle.BattleContext;
import rpg.items.ItemType;
import rpg.model.Player;
import rpg.ui.GameUI;

public class PotionAction implements BattleAction {
    private final ItemType potionType;

    public PotionAction(ItemType potionType) {
        this.potionType = potionType;
    }

    @Override
    public String label() {
        return "Use " + potionType.getDisplayName() + " (+" + potionType.getHealAmount() + " HP)";
    }

    @Override
    public void execute(BattleContext ctx) {
        var player = ctx.getPlayer();
        var ui = ctx.getUi();

        if (!player.getInventory().remove(potionType, 1)) {
            ui.println("You have no " + potionType.getDisplayName() + "!");
            return;
        }

        int before = player.getHp();
        player.heal(potionType.getHealAmount());
        int healed = player.getHp() - before;

        ui.println("You used " + potionType.getDisplayName() + " and healed " + healed + " HP.");
        ui.println(potionType.getDisplayName() + " left: " + player.getInventory().getCount(potionType));
    }
}
