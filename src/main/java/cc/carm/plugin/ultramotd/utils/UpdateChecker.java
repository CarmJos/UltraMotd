package cc.carm.plugin.ultramotd.utils;

import cc.carm.lib.githubreleases4j.GithubReleases4J;
import cc.carm.plugin.ultramotd.Main;
import net.md_5.bungee.api.ProxyServer;

public class UpdateChecker {

    private UpdateChecker() {
        throw new IllegalStateException("Utility class");
    }

    public static void checkUpdate(Main plugin) {
        ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {
            Integer behindVersions = GithubReleases4J.getVersionBehind(
                    "CarmJos", "UltraMOTD",
                    plugin.getDescription().getVersion()
            );

            String downloadURL = GithubReleases4J.getReleasesURL("CarmJos", "UltraMOTD");

            if (behindVersions == null) {
                plugin.getLogger().severe("检查更新失败，请您定期查看插件是否更新，避免安全问题。");
                plugin.getLogger().severe("下载地址 " + downloadURL);
            } else if (behindVersions == 0) {
                plugin.getLogger().info("检查完成，当前已是最新版本。");
            } else if (behindVersions > 0) {
                plugin.getLogger().info("发现新版本! 目前已落后 " + behindVersions + " 个版本。");
                plugin.getLogger().info("最新版下载地址 " + downloadURL);
            } else {
                plugin.getLogger().warning("检查更新失败! 当前版本未知，请您使用原生版本以避免安全问题。");
                plugin.getLogger().warning("最新版下载地址 " + downloadURL);
            }
        });
    }


}
