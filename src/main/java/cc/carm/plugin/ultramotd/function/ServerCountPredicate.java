package cc.carm.plugin.ultramotd.function;

import net.md_5.bungee.api.config.ServerInfo;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface ServerCountPredicate extends BiPredicate<ServerInfo, String> {

    static ServerCountPredicate byName() {
        return (serverInfo, input) -> serverInfo.getName().equals(input);
    }

    static ServerCountPredicate matchPattern() {
        return (serverInfo, input) -> serverInfo.getName().matches(input);
    }

}
