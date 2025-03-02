package cc.carm.plugin.ultramotd;

import java.io.File;

public class UltraMOTD {

    public static File getDataFolder() {
        return new File("plugins/UltraMOTD");
    }

    public static int getMaxPlayers() {
        return 100;
    }

    public static int getOnlinePlayers() {
        return 50;
    }


}
