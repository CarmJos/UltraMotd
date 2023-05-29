package cc.carm.plugin.ultramotd.info;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.plugin.ultramotd.Main;
import cc.carm.plugin.ultramotd.conf.PluginMessages;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DisplayContent {

    protected final @NotNull String id;

    protected final @Nullable String line1;
    protected final @Nullable String line2;

    protected final @NotNull DisplayPlayers players;
    protected final @Nullable DisplayProtocol protocol;

    protected final @NotNull DisplayModType modType;
    protected final @Nullable Favicon favicon;

    protected final @NotNull Set<Pattern> hostnames;

    public DisplayContent(@NotNull String id,
                          @Nullable String line1, @Nullable String line2,
                          @NotNull DisplayPlayers players, @Nullable DisplayProtocol protocol,
                          @NotNull DisplayModType modType, @Nullable Favicon favicon,
                          @NotNull Set<Pattern> hostnames) {
        this.id = id;
        this.line1 = line1;
        this.line2 = line2;
        this.players = players;
        this.protocol = protocol;
        this.modType = modType;
        this.favicon = favicon;
        this.hostnames = hostnames;
    }

    public ServerPing applyTo(@NotNull ServerPing ping) {
        if (getLine1() != null || getLine2() != null) {
            ping.setDescriptionComponent(new TextComponent(PluginMessages.parse(getMOTDContent())));
        }

        if (getDisplayPlayers() != null) ping.setPlayers(getDisplayPlayers().generate());
        if (getDisplayProtocol() != null) ping.setVersion(getDisplayProtocol().apply(ping.getVersion()));
        if (getFavicon() != null) ping.setFavicon(getFavicon());

        ping.getModinfo().setType(getModType().name());
        return ping;
    }

    public @NotNull String getID() {
        return id;
    }

    public @Nullable String getLine1() {
        return line1;
    }

    public @Nullable String getLine2() {
        return line2;
    }

    public String getMOTDContent() {
        return ColorParser.parse(Optional.ofNullable(line1).orElse("") + "\n" + Optional.ofNullable(line2).orElse(""));
    }

    public DisplayPlayers getDisplayPlayers() {
        return players;
    }

    public DisplayProtocol getDisplayProtocol() {
        return protocol;
    }

    public @NotNull DisplayModType getModType() {
        return modType;
    }

    public @Nullable Favicon getFavicon() {
        return favicon;
    }

    public @NotNull Set<Pattern> getHostnames() {
        return hostnames;
    }

    public boolean matchHostname(String host) {
        return hostnames.stream().anyMatch(pattern -> pattern.matcher(host).matches());
    }

    public static DisplayContent parse(@NotNull String id, @NotNull Configuration conf) {
        String line1 = conf.getString("line1");
        String line2 = conf.getString("line2");

        DisplayPlayers players = Optional.ofNullable(conf.getSection("players")).map(DisplayPlayers::parse).orElse(DisplayPlayers.empty());
        DisplayProtocol protocol = Optional.ofNullable(conf.getSection("protocol")).map(DisplayProtocol::parse).orElse(null);
        DisplayModType modType = Optional.ofNullable(conf.getString("mod-type")).map(DisplayModType::parse).orElse(DisplayModType.VANILLA);
        Favicon icon = Optional.ofNullable(conf.getString("favicon")).map(Main.getDisplayManager()::loadFavicon).orElse(null);
        Set<Pattern> hostnames = new HashSet<>();
        try {
            hostnames = conf.getStringList("hostnames").stream().map(Pattern::compile).collect(Collectors.toSet());
        } catch (Exception ex) {
            Main.error("配置 " + id + " 的域名匹配正则表达式错误： " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }

        return new DisplayContent(id, line1, line2, players, protocol, modType, icon, hostnames);
    }

}
