package cc.carm.plugin.ultramotd.info;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisplayProtocol {

    protected final @Nullable String name;
    protected final @Nullable Integer version;

    public DisplayProtocol(@Nullable String name, @Nullable Integer version) {
        this.name = name;
        this.version = version;
    }

    public @NotNull ServerPing.Protocol apply(ServerPing.Protocol protocol) {
        if (getName() != null) protocol.setName(getName());
        if (getVersion() != null && getVersion() > 0) protocol.setProtocol(getVersion());
        return protocol;
    }

    public @Nullable String getName() {
        return name;
    }

    public @Nullable Integer getVersion() {
        return version;
    }

    public static DisplayProtocol parse(Configuration section) {
        String name = section.getString("name");
        Integer version = section.getInt("version");
        return new DisplayProtocol(name, version);
    }

}
