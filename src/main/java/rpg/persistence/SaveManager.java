package rpg.persistence;

import rpg.items.ItemType;
import rpg.model.Player;
import rpg.model.enums.PlayerClass;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class SaveManager {
    private final Path path;

    public SaveManager(String filename) {
        this.path = Path.of(filename);
    }

    public boolean exists() {
        return Files.exists(path);
    }

    public void delete() throws IOException {
        Files.deleteIfExists(path);
    }

    public void save(Player p) throws IOException {
        Properties props = new Properties();

        props.setProperty("player.name", p.getName());
        props.setProperty("player.class", p.getPlayerClass().name());
        props.setProperty("player.level", Integer.toString(p.getLevel()));
        props.setProperty("player.xp", Integer.toString(p.getXp()));

        props.setProperty("player.mod.maxHp", Integer.toString(p.getMaxHpModifier()));
        props.setProperty("player.mod.atk", Integer.toString(p.getAttackModifier()));
        props.setProperty("player.mod.def", Integer.toString(p.getDefenseModifier()));

        // Inventory (start with only potion; I can expand later)
        props.setProperty("inv." + ItemType.HEALING_POTION.name(),
                Integer.toString(p.getInventory().getCount(ItemType.HEALING_POTION)));

        try (OutputStream out = Files.newOutputStream(path)) {
            props.store(out, "RPG Save File");
        }
    }

    public Player load() throws IOException {
        Properties props = new Properties();

        try (InputStream in = Files.newInputStream(path)) {
            props.load(in);
        }

        String name = require(props, "player.name");
        PlayerClass pc = PlayerClass.valueOf(require(props, "player.class"));

        int level = parseInt(props, "player.level", 1);
        int xp = parseInt(props, "player.xp", 0);

        int maxHpMod = parseInt(props, "player.mod.maxHp", 0);
        int atkMod = parseInt(props, "player.mod.atk", 0);
        int defMod = parseInt(props, "player.mod.def", 0);

        int potions = parseInt(props, "inv." + ItemType.HEALING_POTION.name(), 0);

        Player p = new Player(name, pc);
        // Restore modifiers
        p.addMaxHpModifier(maxHpMod);
        p.addAttackModifier(atkMod);
        p.addDefenseModifier(defMod);

        // Restore level/xp
        p.restoreProgress(level, xp);

        // Restore inventory
        p.getInventory().add(ItemType.HEALING_POTION, potions);

        // Optional: heal to full on load (feels nicer)
        p.healToFull();

        return p;
    }

    private static String require(Properties props, String key) {
        String v = props.getProperty(key);
        if (v == null || v.isBlank()) throw new IllegalStateException("Missing key in save: " + key);
        return v.trim();
    }

    private static int parseInt(Properties props, String key, int def) {
        String v = props.getProperty(key);
        if (v == null || v.isBlank()) return def;
        return Integer.parseInt(v.trim());
    }
}
