package cc.carm.plugin.ultramotd;

import cc.carm.lib.configuration.EasyConfiguration;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.plugin.ultramotd.command.MainCommand;
import cc.carm.plugin.ultramotd.conf.PluginConfig;
import cc.carm.plugin.ultramotd.conf.PluginMessages;
import cc.carm.plugin.ultramotd.listener.ProxyListener;
import cc.carm.plugin.ultramotd.manager.DisplayManager;
import cc.carm.plugin.ultramotd.utils.UpdateChecker;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

import java.io.File;
import java.util.Arrays;

public class Main extends Plugin {

    private static Main instance;

    protected ConfigurationProvider<?> configProvider;
    protected ConfigurationProvider<?> messageProvider;

    protected DisplayManager displayManager;

    @Override
    public void onLoad() {
        Main.instance = this;

        log("加载配置文件...");
        this.configProvider = EasyConfiguration.from(new File(getDataFolder(), "config.yml"));
        this.messageProvider = EasyConfiguration.from(new File(getDataFolder(), "messages.yml"));
        this.configProvider.initialize(PluginConfig.class);
        this.messageProvider.initialize(PluginMessages.class);

    }

    @Override
    public void onEnable() {

        log("加载内容管理器...");
        this.displayManager = new DisplayManager();
        this.displayManager.loadDisplays();
        this.displayManager.loadCacheService();

        log("注册监听器...");
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxyListener());

        log("注册指令...");
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MainCommand(this));

        if (PluginConfig.METRICS.getNotNull()) {
            log("启用统计数据...");
            new Metrics(this, 18596);
        }

        if (PluginConfig.CHECK_UPDATE.getNotNull()) {
            log("开始检查更新...");
            UpdateChecker.checkUpdate(this);
        } else {
            log("已禁用检查更新，跳过。");
        }

    }

    public boolean isDebugging() {
        return PluginConfig.DEBUG.getNotNull();
    }

    public ConfigurationProvider<?> getMessageProvider() {
        return messageProvider;
    }

    public ConfigurationProvider<?> getConfigProvider() {
        return configProvider;
    }

    public static Main getInstance() {
        return instance;
    }

    public static void log(String... messages) {
        Arrays.stream(messages).forEach(message -> instance.getLogger().info(message));
    }

    public static void error(String... messages) {
        Arrays.stream(messages).forEach(message -> instance.getLogger().severe(message));
    }

    public static void debug(String... messages) {
        if (instance.isDebugging()) {
            Arrays.stream(messages).forEach(message -> instance.getLogger().info("[DEBUG] " + message));
        }
    }

    public static DisplayManager getDisplayManager() {
        return instance.displayManager;
    }


}
