package cc.carm.plugin.ultramotd.info;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.plugin.ultramotd.Main;
import cc.carm.plugin.ultramotd.function.PlayerMathFunction;
import cc.carm.plugin.ultramotd.function.ServerCountPredicate;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DisplayPlayers {

    protected final @Nullable List<String> samples;

    protected final @Nullable Integer max;
    protected final @NotNull List<String> countServers;

    protected final @Nullable PlayerMathFunction mathFunction;

    public DisplayPlayers(@Nullable List<String> samples, @Nullable Integer max,
                          @NotNull List<String> countServers, @Nullable PlayerMathFunction mathFunction) {
        this.samples = samples;
        this.max = max;
        this.countServers = countServers;
        this.mathFunction = mathFunction;
    }

    public ServerPing.Players generate() {
        int displayPlayers = getDisplayPlayers();
        int maxPlayers = getMaxPlayers();

        ServerPing.PlayerInfo[] playerInfos = null;

        List<String> lines = getSamples();
        if (lines != null && !lines.isEmpty()) {
            lines.replaceAll(s -> s
                    .replace("%(online)", String.valueOf(displayPlayers))
                    .replace("%(max)", String.valueOf(maxPlayers))
            );
            playerInfos = lines.stream()
                    .map(ColorParser::parse).map(s -> new ServerPing.PlayerInfo(s, UUID.randomUUID()))
                    .toArray(ServerPing.PlayerInfo[]::new);
        }

        return new ServerPing.Players(getDisplayPlayers(), maxPlayers, playerInfos);
    }

    public @Nullable List<String> getSamples() {
        return samples;
    }

    public int getMaxPlayers() {
        if (max != null) return max;
        return ProxyServer.getInstance().getConfig().getListeners().stream().mapToInt(ListenerInfo::getMaxPlayers).max().orElse(-1);
    }

    public @NotNull List<String> getCountServers() {
        return countServers;
    }

    public boolean matchCountServers(ServerInfo server) {
        ServerCountPredicate predicate = Main.getDisplayManager().getCountPredicate();
        return countServers.stream().anyMatch(p -> predicate.test(server, p));
    }

    public int countPlayers() {
        if (getCountServers().isEmpty()) return ProxyServer.getInstance().getOnlineCount();
        else return ProxyServer.getInstance().getServersCopy().values().stream()
                .filter(this::matchCountServers).mapToInt(s -> s.getPlayers().size()).sum();
    }

    public Map<UUID, String> getPlayers() {
        Map<UUID, String> players = new HashMap<>();
        if (getCountServers().isEmpty()) {
            ProxyServer.getInstance().getPlayers().forEach(p -> players.put(p.getUniqueId(), p.getName()));
        } else {
            ProxyServer.getInstance().getServersCopy().values().stream()
                    .filter(this::matchCountServers)
                    .flatMap(s -> s.getPlayers().stream())
                    .forEach(p -> players.put(p.getUniqueId(), p.getName()));
        }
        return players;
    }

    public int mathPlayers(int online) {
        if (mathFunction == null) return online;
        else return mathFunction.apply(online);
    }

    public int getDisplayPlayers() {
        return mathPlayers(countPlayers());
    }


}
