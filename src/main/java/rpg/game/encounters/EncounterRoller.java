package rpg.game.encounters;

import rpg.util.Rng;

import java.util.List;

public class EncounterRoller {
    private final List<Encounter> encounters;

    public EncounterRoller(List<Encounter> encounters) {
        this.encounters = encounters;
    }

    public Encounter roll() {
        // Simple: 40% encounter, 60% nothing
        if (!Rng.chance(40)) return null;

        int idx = Rng.between(0, encounters.size() - 1);
        return encounters.get(idx);
    }
}
