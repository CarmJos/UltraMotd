package cc.carm.plugin.ultramotd.info;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import org.jetbrains.annotations.Nullable;

public class DisplayProtocol {

    protected final @Nullable String name;
    protected final @Nullable Integer version;

    public DisplayProtocol(@Nullable String name, @Nullable Integer version) {
        this.name = name;
        this.version = version;
    }

    public @Nullable String getName() {
        return name;
    }

    public @Nullable Integer getVersion() {
        return version;
    }

    public static DisplayProtocol parse(ConfigureSection section) {
        String name = section.getString("name");
        Integer version = section.getInt("version");
        return new DisplayProtocol(name, version);
    }

}
