package cc.carm.plugin.ultramotd.manager;

import cc.carm.plugin.ultramotd.conf.PluginConfig;
import cc.carm.plugin.ultramotd.info.DisplayContent;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ultramotd.Main;
import ultramotd.utils.ConfigFolderUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DisplayManager {

    protected @NotNull Map<String, DisplayContent> contents = new LinkedHashMap<>();
    protected @Nullable Cache<String, ServerPing> cache;

    protected @Nullable LocalDate skipUntil = null;

    public void reloadDisplays() {
        Main.log(" 开始加载内容配置...");
        long start = System.currentTimeMillis();

        File pluginFolder = Main.getInstance().getDataFolder();
        File folder;
        try {
            folder = ConfigFolderUtils.initialize(pluginFolder, "displays");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        File[] files = folder.listFiles();
        if (files == null) return;

        ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);

        HashMap<@NotNull String, @NotNull DisplayContent> data = new HashMap<>();
        for (File file : files) {
            String fileName = file.getName();
            if (!file.isFile() || fileName.startsWith(".") || !fileName.toLowerCase().endsWith(".yml")) continue;
            String identifier = fileName.substring(0, fileName.lastIndexOf("."));
            try {
                Configuration conf = provider.load(file);
                DisplayContent content = DisplayContent.parse(identifier, conf);
                Main.debug("成功加载了配置 " + identifier + " 。");
                data.put(identifier, content);
            } catch (IOException e) {
                Main.error("加载配置文件 displays/" + identifier + ".yml 时出现了错误：" + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

        this.contents = data;
        Main.log("	显示配置加载完成，共加载 " + data.size() + " 个配置，耗时 " + (System.currentTimeMillis() - start) + "ms 。");
    }

    public void reloadCacheService() {
        if (PluginConfig.CACHE_MILLIS.getNotNull() > 0) {
            this.cache = CacheBuilder.newBuilder()
                    .expireAfterWrite(PluginConfig.CACHE_MILLIS.getNotNull(), TimeUnit.MILLISECONDS)
                    .maximumSize(50).build();
        } else {
            if (cache == null) return;
            this.cache.cleanUp();
            this.cache = null;
        }
    }

    public @NotNull Map<String, DisplayContent> getContents() {
        return contents;
    }

    public List<DisplayContent> getMatchedContents(@Nullable String hostname) {
        if (hostname == null) {
            return getContents().values().stream().filter(c -> c.getHostnames().isEmpty()).collect(Collectors.toList());
        } else {
            return getContents().values().stream().filter(c -> c.matchHostname(hostname)).collect(Collectors.toList());
        }
    }

    public @Nullable DisplayContent getDisplay(@Nullable InetSocketAddress address) {
        return getDisplay(address == null ? null : address.getHostName());
    }

    public @Nullable DisplayContent getDisplay(String hostname) {
        List<DisplayContent> displays = getMatchedContents(hostname);
        if (hostname != null && displays.isEmpty()) {
            displays = getMatchedContents(null);
        }
        if (displays.isEmpty()) return null;

        Collections.shuffle(displays);
        return displays.get(0);
    }

    public void addDisplay(@NotNull DisplayContent content) {
        contents.put(content.getID(), content);
    }

    public void removeDisplay(@NotNull String id) {
        contents.remove(id);
    }

    public void clearCache() {
        if (cache == null) return;
        cache.cleanUp();
    }

    public boolean isMaintenance() {
        if (PluginConfig.MAINTENANCE.ENABLE.getNotNull()) return true;
        if (!PluginConfig.MAINTENANCE.SCHEDULE.ENABLE.getNotNull()) return false;

        if (skipUntil != null) {
            if (skipUntil.isAfter(LocalDate.now()) || skipUntil.isEqual(LocalDate.now())) return false;
            else skipUntil = null;
        }

        LocalTime start = PluginConfig.MAINTENANCE.SCHEDULE.START.getNotNull();
        LocalTime end = PluginConfig.MAINTENANCE.SCHEDULE.END.getNotNull();

        LocalTime now = LocalTime.now();
        if (start.isBefore(end)) {
            return now.isAfter(start) && now.isBefore(end);
        } else {
            return now.isAfter(start) || now.isBefore(end);
        }
    }

    public void setSkipUntil(@Nullable LocalDate skipUntil) {
        this.skipUntil = skipUntil;
    }

}
