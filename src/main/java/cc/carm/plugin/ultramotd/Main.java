package cc.carm.plugin.ultramotd;

import cc.carm.lib.mineconfiguration.bungee.MineConfiguration;
import cc.carm.plugin.ultramotd.conf.PluginConfig;
import cc.carm.plugin.ultramotd.conf.PluginMessages;
import cc.carm.plugin.ultramotd.manager.DisplayManager;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {

    private static Main instance;

    protected MineConfiguration configuration;
    protected DisplayManager displayManager;

    @Override
    public void onEnable() {
        this.configuration = new MineConfiguration(this, PluginConfig.class, PluginMessages.class);
    }

    public MineConfiguration getConfiguration() {
        return configuration;
    }

    public static DisplayManager getDisplayManager() {
        return instance.displayManager;
    }


}
