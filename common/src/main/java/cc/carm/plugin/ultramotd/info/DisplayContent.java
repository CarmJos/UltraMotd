package cc.carm.plugin.ultramotd.info;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.plugin.ultramotd.UltraMOTD;
import cc.carm.plugin.ultramotd.utils.ConfigFolderUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    protected final @Nullable BufferedImage favicon;

    protected final @NotNull Set<Pattern> hostnames;

    public DisplayContent(@NotNull String id,
                          @Nullable String line1, @Nullable String line2,
                          @NotNull DisplayPlayers players, @Nullable DisplayProtocol protocol,
                          @NotNull DisplayModType modType, @Nullable BufferedImage favicon,
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

    public @Nullable BufferedImage getFavicon() {
        return favicon;
    }

    public @NotNull Set<Pattern> getHostnames() {
        return hostnames;
    }

    public boolean matchHostname(String host) {
        return hostnames.stream().anyMatch(pattern -> pattern.matcher(host).matches());
    }

    public static DisplayContent parse(@NotNull String id, @NotNull ConfigureSection conf) {
        String line1 = conf.getString("line1");
        String line2 = conf.getString("line2");

        DisplayPlayers players = Optional.ofNullable(conf.getSection("players")).map(DisplayPlayers::parse).orElse(DisplayPlayers.empty());
        DisplayProtocol protocol = Optional.ofNullable(conf.getSection("protocol")).map(DisplayProtocol::parse).orElse(null);
        DisplayModType modType = Optional.ofNullable(conf.getString("mod-type")).map(DisplayModType::parse).orElse(DisplayModType.VANILLA);
        BufferedImage image = null;
        String iconPath = conf.getString("favicon");
        if (iconPath != null && !iconPath.isEmpty()) {
            try {
                File folder = ConfigFolderUtils.initialize(UltraMOTD.getDataFolder(), "favicons");
                image = ImageIO.read(new File(folder, iconPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Set<Pattern> hostnames = new HashSet<>();
        try {
            hostnames = conf.getStringList("hostnames").stream().map(Pattern::compile).collect(Collectors.toSet());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new DisplayContent(id, line1, line2, players, protocol, modType, image, hostnames);
    }

}
