package rpg.game.encounters;

import rpg.items.ItemType;
import rpg.model.Player;
import rpg.ui.GameUI;

import java.util.List;

public class MerchantEncounter implements Encounter {

    @Override
    public String name() {
        return "Merchant";
    }

    @Override
    public void run(Player player, GameUI ui) {
        ui.println("\n=== MERCHANT ===");
        ui.println("A merchant offers you potions.");

        int choice = ui.chooseOption(
                "What do you want to do?",
                List.of("Buy 1 Healing Potion", "Leave")
        );

        if (choice == 0) {
            player.getInventory().add(ItemType.HEALING_POTION, 1);
            ui.println("You bought 1 Healing Potion.");
            ui.println("You now have: " +
                    player.getInventory().getCount(ItemType.HEALING_POTION));
        } else {
            ui.println("You leave the merchant.");
        }
    }
}
