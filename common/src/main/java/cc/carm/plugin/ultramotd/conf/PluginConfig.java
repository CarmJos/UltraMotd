package cc.carm.plugin.ultramotd.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComments;
import cc.carm.lib.configuration.value.standard.ConfiguredList;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import cc.carm.plugin.ultramotd.conf.value.ConfiguredMessage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@ConfigPath(root = true)
public interface PluginConfig extends Configuration {

    ConfiguredValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    @HeaderComments({
            "统计数据设定",
            "该选项用于帮助开发者统计插件版本与使用情况，且绝不会影响性能与使用体验。",
            "当然，您也可以选择在这里关闭，或在plugins/bStats下的配置文件中关闭。"
    })
    ConfiguredValue<Boolean> METRICS = ConfiguredValue.of(Boolean.class, true);

    @HeaderComments({
            "检查更新设定",
            "该选项用于插件判断是否要检查更新，若您不希望插件检查更新并提示您，可以选择关闭。",
            "检查更新为异步操作，绝不会影响性能与使用体验。"
    })
    ConfiguredValue<Boolean> CHECK_UPDATE = ConfiguredValue.of(Boolean.class, true);


    @HeaderComments({"MOTD显示缓存时间（毫秒）", "若此配置项≤0，则不启用缓存。"})
    ConfiguredValue<Long> CACHE_MILLIS = ConfiguredValue.of(Long.class, 500L);

    @HeaderComments("维护模式配置")
    interface MAINTENANCE extends Configuration {

        @HeaderComments("是否启用维护模式")
        ConfiguredValue<Boolean> ENABLE = ConfiguredValue.of(Boolean.class, false);

        @HeaderComments("维护模式下使用的显示配置ID")
        ConfiguredValue<String> DISPLAY = ConfiguredValue.of(String.class, "maintenance");

        @HeaderComments("启用维护模式时，是否踢出已在线玩家。")
        ConfiguredValue<Boolean> KICK_ONLINE = ConfiguredValue.of(Boolean.class, true);

        @HeaderComments("非白名单玩家进入服务器时的提示信息")
        ConfiguredMessage KICK_MESSAGE = PluginMessages.create()
                .defaults("服务器正在维护中，请稍后再试。").build();

        @HeaderComments({"维护模式白名单。白名单内的玩家可以在维护模式的情况下进入服务器。", "不建议离线验证服务器启用白名单。"})
        ConfiguredList<String> ALLOWED_PLAYERS = ConfiguredList
                .builderOf(String.class).fromString()
                .defaults("CarmJos")
                .build();

        @HeaderComments("定时自动启动维护状态")
        interface SCHEDULE extends Configuration {

            @HeaderComments("是否启用定时维护")
            ConfiguredValue<Boolean> ENABLE = ConfiguredValue.of(Boolean.class, false);

            @HeaderComments("定时维护开始时间，格式为 00:00:00 (不足2位需要补0)")
            ConfiguredValue<LocalTime> START = ConfiguredValue.builderOf(LocalTime.class).fromString()
                    .serialize(v -> v.format(DateTimeFormatter.ISO_LOCAL_TIME))
                    .parse((v, d) -> LocalTime.parse(d, DateTimeFormatter.ISO_LOCAL_TIME))
                    .defaults(LocalTime.of(1, 30)).build();

            @HeaderComments("定时维护结束时间，格式为 00:00:00 (不足2位需要补0)")
            ConfiguredValue<LocalTime> END = ConfiguredValue.builderOf(LocalTime.class).fromString()
                    .serialize(v -> v.format(DateTimeFormatter.ISO_LOCAL_TIME))
                    .parse((v, d) -> LocalTime.parse(d, DateTimeFormatter.ISO_LOCAL_TIME))
                    .defaults(LocalTime.of(12, 0)).build();

        }

    }


}
