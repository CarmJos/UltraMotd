package cc.carm.plugin.ultramotd.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bungee.value.ConfiguredMessageList;
import net.md_5.bungee.api.chat.BaseComponent;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

        @HeaderComment("启用维护模式时，是否踢出已在线玩家。")
        public static final ConfiguredValue<Boolean> KICK_ONLINE = ConfiguredValue.of(Boolean.class, true);

        @HeaderComment("非白名单玩家进入服务器时的提示信息")
        public static final ConfiguredMessageList<BaseComponent[]> KICK_MESSAGE = PluginMessages.list()
                .defaults("服务器正在维护中，请稍后再试。").build();

        @HeaderComment({"维护模式白名单。白名单内的玩家可以在维护模式的情况下进入服务器。", "不建议离线验证服务器启用白名单。"})
        public static final ConfiguredList<String> ALLOWED_PLAYERS = ConfiguredList
                .builderOf(String.class).fromString()
                .defaults("CarmJos")
                .build();

        @HeaderComment("定时自动启动维护状态")
        public static final class SCHEDULE extends ConfigurationRoot {

            @HeaderComment("是否启用定时维护")
            public static final ConfiguredValue<Boolean> ENABLE = ConfiguredValue.of(Boolean.class, false);

            @HeaderComment("定时维护开始时间，格式为 00:00:00 (不足2位需要补0)")
            public static final ConfiguredValue<LocalTime> START = ConfiguredValue.builderOf(LocalTime.class).fromString()
                    .serializeValue(v -> v.format(DateTimeFormatter.ISO_LOCAL_TIME))
                    .parseValue((v, d) -> {
                        try {
                            return LocalTime.parse(v, DateTimeFormatter.ISO_LOCAL_TIME);
                        } catch (Exception e) {
                            return d;
                        }
                    }).defaults(LocalTime.of(1, 30)).build();

            @HeaderComment("定时维护结束时间，格式为 00:00:00 (不足2位需要补0)")
            public static final ConfiguredValue<LocalTime> END = ConfiguredValue.builderOf(LocalTime.class).fromString()
                    .serializeValue(v -> v.format(DateTimeFormatter.ISO_LOCAL_TIME))
                    .parseValue((v, d) -> {
                        try {
                            return LocalTime.parse(v, DateTimeFormatter.ISO_LOCAL_TIME);
                        } catch (Exception e) {
                            return d;
                        }
                    }).defaults(LocalTime.of(12, 0)).build();

        }

    }


}
