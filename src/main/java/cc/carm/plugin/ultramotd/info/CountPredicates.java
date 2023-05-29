package cc.carm.plugin.ultramotd.info;

import cc.carm.plugin.ultramotd.function.ServerCountPredicate;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Arrays;

public enum CountPredicates {
    NAME(ServerCountPredicate.byName()),
    PATTERN(ServerCountPredicate.matchPattern());

    private final ServerCountPredicate function;

    CountPredicates(ServerCountPredicate function) {
        this.function = function;
    }

    public ServerCountPredicate getFunction() {
        return function;
    }

    public boolean test(ServerInfo info, String input) {
        return function.test(info, input);
    }

    public static CountPredicates parse(String input) {
        return Arrays.stream(CountPredicates.values())
                .filter(predicate -> predicate.name().equalsIgnoreCase(input))
                .findFirst().orElse(null);
    }
}
