package rpg.ui;

import rpg.model.enums.PlayerClass;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI implements GameUI {
    private final Scanner sc = new Scanner(System.in);

    @Override
    public void println(String s) {
        System.out.println(s);
    }

    @Override
    public String readNonBlank(String prompt) {
        println(prompt);
        while (true) {
            String s = sc.nextLine().trim();
            if (!s.isBlank()) return s;
            println("Please enter a non-empty value:");
        }
    }

    @Override
    public int chooseOption(String prompt, List<String> options) {
        println(prompt);
        for (int i = 0; i < options.size(); i++) {
            println((i + 1) + ") " + options.get(i));
        }
        while (true) {
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v < 1 || v > options.size()) throw new IllegalArgumentException();
                return v - 1; // 0-based index
            } catch (Exception e) {
                println("Enter a number between 1 and " + options.size() + ":");
            }
        }
    }

    // keeping this just in case
    public PlayerClass choosePlayerClass() {
        int idx = chooseOption("Choose your class:", List.of("WARRIOR", "MAGE", "ROGUE"));
        return switch (idx) {
            case 0 -> PlayerClass.WARRIOR;
            case 1 -> PlayerClass.MAGE;
            default -> PlayerClass.ROGUE;
        };
    }
}
