package rpg.fx;

public class FeedItem {
    private final String text;
    private final FeedKind kind;

    public FeedItem(String text, FeedKind kind) {
        this.text = text;
        this.kind = kind;
    }

    public String getText() {
        return text;
    }

    public FeedKind getKind() {
        return kind;
    }

    /**
     * Heuristic classifier so you DON'T have to rewrite your whole engine.
     * Later we can upgrade to explicit event types (better), but this is quick and impactful.
     */
    public static FeedItem fromLine(String line) {
        if (line == null) line = "";
        String s = line.trim();
        String low = s.toLowerCase();

        FeedKind kind = FeedKind.INFO;

        // System / headers
        if (s.startsWith("===") || s.startsWith("---") || low.contains("game start") || low.contains("game over")) {
            kind = FeedKind.SYSTEM;
        }

        // Damage-ish
        if (low.contains("hits you for") || low.contains("damage") || low.contains("you hit") || low.contains("crit")) {
            kind = FeedKind.DMG;
        }

        // Healing-ish
        if (low.contains("potion") || low.contains("heal") || low.contains("healed") || low.contains("restored")) {
            kind = FeedKind.HEAL;
        }

        // XP / level
        if (low.contains("xp") || low.contains("level up") || low.contains("reward")) {
            kind = FeedKind.XP;
        }

        // If it’s a battle header, keep SYSTEM even if it mentions something else
        if (s.startsWith("=== BATTLE START ===") || s.startsWith("=== BATTLE END ===")) {
            kind = FeedKind.SYSTEM;
        }

        return new FeedItem(s, kind);
    }
}
