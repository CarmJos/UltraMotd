package cc.carm.plugin.ultramotd;

import cc.carm.plugin.ultramotd.manager.DisplayManager;
import org.jetbrains.annotations.NotNull;

public final class UltraMOTD {

    private UltraMOTD() {
    }

    public static @NotNull DisplayManager getDisplayManager() {
        return Main.getDisplayManager();
    }

}
