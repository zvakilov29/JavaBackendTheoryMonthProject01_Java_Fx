package rpg.util;

import java.util.concurrent.ThreadLocalRandom;

public final class Rng {
    private Rng() {}

    public static int between(int minInclusive, int maxInclusive) {
        return ThreadLocalRandom.current().nextInt(minInclusive, maxInclusive + 1);
    }

    public static boolean chance(int percent) {
        if (percent < 0 || percent > 100) throw new IllegalArgumentException("percent must be 0..100");
        return between(1, 100) <= percent;
    }
}
