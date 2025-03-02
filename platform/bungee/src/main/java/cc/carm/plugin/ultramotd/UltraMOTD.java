package cc.carm.plugin.ultramotd;

import ultramotd.manager.DisplayManager;
import org.jetbrains.annotations.NotNull;

public final class UltraMOTD {

    private UltraMOTD() {
    }

    public static @NotNull DisplayManager getDisplayManager() {
        return Main.getDisplayManager();
    }

}
