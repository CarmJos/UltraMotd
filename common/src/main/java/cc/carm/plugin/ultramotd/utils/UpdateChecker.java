package cc.carm.plugin.ultramotd.utils;

import cc.carm.lib.githubreleases4j.GithubReleases4J;

import java.util.logging.Logger;

public class UpdateChecker {

    private UpdateChecker() {
        throw new IllegalStateException("Utility class");
    }

    public static void checkUpdate(Logger logger, String version) {
        Integer behindVersions = GithubReleases4J.getVersionBehind("CarmJos", "UltraMOTD", version);
        String downloadURL = GithubReleases4J.getReleasesURL("CarmJos", "UltraMOTD");

        if (behindVersions == null) {
            logger.severe("检查更新失败，请您定期查看插件是否更新，避免安全问题。");
            logger.severe("下载地址 " + downloadURL);
        } else if (behindVersions == 0) {
            logger.info("检查完成，当前已是最新版本。");
        } else if (behindVersions > 0) {
            logger.info("发现新版本! 目前已落后 " + behindVersions + " 个版本。");
            logger.info("最新版下载地址 " + downloadURL);
        } else {
            logger.warning("检查更新失败! 当前版本未知，请您使用原生版本以避免安全问题。");
            logger.warning("最新版下载地址 " + downloadURL);
        }
    }


}
