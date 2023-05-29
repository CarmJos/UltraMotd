package cc.carm.plugin.ultramotd.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bungee.value.ConfiguredMessageList;
import net.md_5.bungee.api.chat.BaseComponent;

public class PluginConfig extends ConfigurationRoot {

    public static final ConfiguredValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    @HeaderComment({
            "统计数据设定",
            "该选项用于帮助开发者统计插件版本与使用情况，且绝不会影响性能与使用体验。",
            "当然，您也可以选择在这里关闭，或在plugins/bStats下的配置文件中关闭。"
    })
    public static final ConfiguredValue<Boolean> METRICS = ConfiguredValue.of(Boolean.class, true);

    @HeaderComment({
            "检查更新设定",
            "该选项用于插件判断是否要检查更新，若您不希望插件检查更新并提示您，可以选择关闭。",
            "检查更新为异步操作，绝不会影响性能与使用体验。"
    })
    public static final ConfiguredValue<Boolean> CHECK_UPDATE = ConfiguredValue.of(Boolean.class, true);


    @HeaderComment({"MOTD显示缓存时间（毫秒）", "若此配置项≤0，则不启用缓存。"})
    public static final ConfiguredValue<Long> CACHE_MILLIS = ConfiguredValue.of(Long.class, 500L);

    @HeaderComment("维护模式配置")
    public static final class MAINTENANCE extends ConfigurationRoot {

        @HeaderComment("是否启用维护模式")
        public static final ConfiguredValue<Boolean> ENABLE = ConfiguredValue.of(Boolean.class, false);

        @HeaderComment("维护模式下使用的显示配置ID")
        public static final ConfiguredValue<String> DISPLAY = ConfiguredValue.of(String.class, "maintenance");

        @HeaderComment("非白名单玩家进入服务器时的提示信息")
        public static final ConfiguredMessageList<BaseComponent[]> KICK_MESSAGE = PluginMessages.list()
                .defaults("服务器正在维护中，请稍后再试。").build();

        @HeaderComment({"维护模式白名单。白名单内的玩家可以在维护模式的情况下进入服务器。", "不建议离线验证服务器启用白名单。"})
        public static final ConfiguredList<String> ALLOWED_PLAYERS = ConfiguredList
                .builderOf(String.class).fromString()
                .defaults("CarmJos")
                .build();

    }


}
