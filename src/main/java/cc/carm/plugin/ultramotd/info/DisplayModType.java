package cc.carm.plugin.ultramotd.info;

import java.util.Arrays;

public enum DisplayModType {

    VANILLA, FML, BUKKIT;

    public static DisplayModType parse(String input) {
        return Arrays.stream(DisplayModType.values())
                .filter(v -> v.name().equalsIgnoreCase(input))
                .findFirst().orElse(null);
    }

}
