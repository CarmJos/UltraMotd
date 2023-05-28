package cc.carm.plugin.ultramotd.info;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.plugin.ultramotd.conf.PluginMessages;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class DisplayContents {

    protected final @Nullable String line1;
    protected final @Nullable String line2;

    protected final @Nullable DisplayPlayers players;
    protected final @Nullable DisplayProtocol protocol;

    protected final @NotNull DisplayModType modType;
    protected final @Nullable Favicon favicon;

    protected final @NotNull List<Pattern> hostnames;

    public DisplayContents(@Nullable String line1, @Nullable String line2,
                           @Nullable DisplayPlayers players, @Nullable DisplayProtocol protocol,
                           @NotNull DisplayModType modType, @Nullable Favicon favicon,
                           @NotNull List<Pattern> hostnames) {
        this.line1 = line1;
        this.line2 = line2;
        this.players = players;
        this.protocol = protocol;
        this.modType = modType;
        this.favicon = favicon;
        this.hostnames = hostnames;
    }

    public void applyTo(@NotNull ServerPing ping) {
        if (getLine1() != null || getLine2() != null) {
            ping.setDescriptionComponent(new TextComponent(PluginMessages.parse(getMOTDContent())));
        }

        if (getDisplayPlayers() != null) ping.setPlayers(getDisplayPlayers().generate());
        if (getDisplayProtocol() != null) ping.setVersion(getDisplayProtocol().apply(ping.getVersion()));
        if (getFavicon() != null) ping.setFavicon(getFavicon());

        ping.getModinfo().setType(getModType().name());
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

    public @NotNull List<Pattern> getHostnames() {
        return hostnames;
    }

    public boolean matchHostname(String host) {
        return hostnames.stream().anyMatch(pattern -> pattern.matcher(host).matches());
    }

}
