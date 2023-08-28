package cc.carm.plugin.ultramotd.manager;

import cc.carm.plugin.ultramotd.Main;
import cc.carm.plugin.ultramotd.conf.PluginConfig;
import cc.carm.plugin.ultramotd.info.DisplayContent;
import cc.carm.plugin.ultramotd.utils.ConfigFolderUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DisplayManager {

    protected Map<String, DisplayContent> contents = new LinkedHashMap<>();
    protected Cache<String, ServerPing> cache;

    public Favicon loadFavicon(String path) {
        File pluginFolder = Main.getInstance().getDataFolder();
        try {
            File folder = ConfigFolderUtils.initialize(pluginFolder, "favicons");
            BufferedImage img = ImageIO.read(new File(folder, path));
            return Favicon.create(img);
        } catch (IOException e) {
            Main.error("无法读取图标 [" + path + "] 请检查配置文件：" + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }


    public void loadDisplays() {
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

    public void loadCacheService() {
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

    public Cache<String, ServerPing> getCache() {
        return cache;
    }

    public Map<String, DisplayContent> getContents() {
        return contents;
    }

    public List<DisplayContent> getMatchedContents(@Nullable String hostname) {
        if (hostname == null) {
            return getContents().values().stream().filter(c -> c.getHostnames().isEmpty()).collect(Collectors.toList());
        } else {
            return getContents().values().stream().filter(c -> c.matchHostname(hostname)).collect(Collectors.toList());
        }
    }

    public @Nullable DisplayContent getDisplay(@NotNull InetSocketAddress address) {
        return getDisplay(address.getHostName());
    }

    public @Nullable DisplayContent getDisplay(String hostname) {
        List<DisplayContent> displays = getMatchedContents(hostname);
        if (displays.isEmpty()) displays = getMatchedContents(null);
        if (displays.isEmpty()) return null;

        Collections.shuffle(displays);
        return displays.get(0);
    }

}
