package rpg.game.encounters;

import rpg.model.Player;
import rpg.ui.GameUI;

import java.util.List;

public class ShrineEncounter implements Encounter {

    @Override
    public String name() {
        return "Shrine";
    }

    @Override
    public void run(Player player, GameUI ui) {
        ui.println("\n=== SHRINE ===");
        ui.println("You find a mysterious shrine. Choose a blessing:");

        int choice = ui.chooseOption(
                "Choose one blessing:",
                List.of("+10 Max HP", "+2 Attack", "+2 Defense")
        );

        switch (choice) {
            case 0 -> {
                player.addMaxHpModifier(10);
                ui.println("Blessing received: +10 Max HP!");
            }
            case 1 -> {
                player.addAttackModifier(2);
                ui.println("Blessing received: +2 Attack!");
            }
            default -> {
                player.addDefenseModifier(2);
                ui.println("Blessing received: +2 Defense!");
            }
        }

        ui.println("Updated stats -> HP: " + player.getHp() + "/" + player.getMaxHp()
                + " | ATK: " + player.getAttack()
                + " | DEF: " + player.getDefense());
    }
}
