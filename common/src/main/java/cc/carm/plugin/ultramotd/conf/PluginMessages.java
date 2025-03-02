package cc.carm.plugin.ultramotd.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComments;
import cc.carm.plugin.ultramotd.conf.value.ConfiguredMessage;
import org.jetbrains.annotations.NotNull;


@HeaderComments({
        "UltraMOTD 消息配置文件",
        "如特定的消息不需要任何提示，可直接留下单行空内容消息。",
        "支持 支持 &+颜色代码(原版颜色)、§(#XXXXXX)(RGB颜色) 与 &<#XXXXXX>(前后标注RGB颜色渐变)。",
        " "
})
@ConfigPath(root = true)
public interface PluginMessages extends Configuration {

    static @NotNull ConfiguredMessage.Builder create() {
        return ConfiguredMessage.create();
    }

    ConfiguredMessage NO_PERMISSION = create()
            .defaults("&c&l抱歉！&f但您没有足够的权限这么做。")
            .build();

    ConfiguredMessage COMMAND_USAGE = create()
            .defaults(
                    "&6&lUltra&e&lMOTD &l指令帮助",
                    "&8#&f display &6[链接域名]",
                    "&8-&7 查看对应的服务器展示信息。",
                    "&8#&f maintenance toggle",
                    "&8-&7 开启/关闭维护模式。",
                    "&8#&f maintenance allow &e<玩家名>",
                    "&8-&7 允许玩家在维护期间进入服务器。",
                    "&8#&f maintenance deny &e<玩家名>",
                    "&8-&7 不再允许玩家在维护期间进入服务器。",
                    "&8#&f maintenance skip &e[天数]",
                    "&8-&7 跳过对应天数的定时维护（0代表当天，小于0则重置)",
                    "&8#&f reload",
                    "&8-&7 重载插件配置文件。"
            )
            .build();


    interface MAINTENANCE extends Configuration {

        ConfiguredMessage ENABLED = create()
                .defaults("&f成功 &a&l启用 &f维护模式，非白名单玩家将无法连接到服务器。")
                .build();

        ConfiguredMessage DISABLED = create()
                .defaults("&f成功 &c&l关闭 &f维护模式，所有玩家现均可以连接到服务器。")
                .build();

        ConfiguredMessage ALREADY_ALLOWED = create()
                .defaults("&f玩家 &e%(player) &f已经在白名单中了。")
                .params("player")
                .build();

        ConfiguredMessage ALLOWED = create()
                .defaults("&f成功将玩家 &e%(player) &f添加到维护白名单。")
                .params("player")
                .build();

        ConfiguredMessage ALREADY_DENIED = create()
                .defaults("&f玩家 &e%(player) &f并不在维护白名单中。")
                .params("player")
                .build();

        ConfiguredMessage DENIED = create()
                .defaults("&f成功将玩家 &e%(player) &f移出维护白名单。")
                .params("player")
                .build();

        ConfiguredMessage SCHEDULE_SKIP = create()
                .defaults("&f已设置在 &e%(date) &f前都不再进入定时维护状态，共跳过 &e%(days) &f天。")
                .params("date", "days")
                .build();

        ConfiguredMessage SCHEDULE_ENABLE = create()
                .defaults("&f已清除定时维护的关闭期限，将自动启用维护模式。")
                .build();

    }

    interface DISPLAY extends Configuration {

        ConfiguredMessage NONE_MATCH = create()
                .defaults("&f未找到任何对应的服务器展示信息。")
                .build();

        ConfiguredMessage INFO = create()
                .defaults("&f共有 &e%(count) &f个服务器展示信息匹配，如下：")
                .params("count").build();

        ConfiguredMessage VALUE = create()
                .defaults("&8#&f%(index) &e%(id) &8- &f%(online)&7/%(max)",
                        "&8 | &r%(line1)",
                        "&8 | &r%(line2)",
                        "&8 - &7协议&8[&f%(protocol-version)&8] &r%(protocol-name)"
                ).params("index", "id", "online", "max", "line1", "line2", "protocol-version", "protocol-name")
                .build();

    }

    interface RELOAD extends Configuration {

        ConfiguredMessage SUCCESS = create()
                .defaults("&a&l重载完成！&f耗时 &6%(time)ms &f，共加载了 &e%(count) &f条配置。")
                .params("time", "count")
                .build();

        ConfiguredMessage FAILED = create()
                .defaults("&c&l重载失败！&f请查看后台详细报错。")
                .build();
    }

}
