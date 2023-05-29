package cc.carm.plugin.ultramotd.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.mineconfiguration.bungee.builder.message.BungeeMessageListBuilder;
import cc.carm.lib.mineconfiguration.bungee.builder.message.BungeeMessageValueBuilder;
import cc.carm.lib.mineconfiguration.bungee.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bungee.value.ConfiguredMessageList;
import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;


@HeaderComment({
        "UltraMOTD 消息配置文件",
        "如特定的消息不需要任何提示，可直接留下单行空内容消息。",
        "支持 支持 &+颜色代码(原版颜色)、§(#XXXXXX)(RGB颜色) 与 &<#XXXXXX>(前后标注RGB颜色渐变)。",
        " "
})
public class PluginMessages extends ConfigurationRoot {

    public static BaseComponent[] parse(String message) {
        return MineDown.parse(ColorParser.parse(message));
    }

    public static @NotNull BungeeMessageListBuilder<BaseComponent[]> list() {
        return ConfiguredMessageList.create(getParser()).whenSend((sender, message) -> message.forEach(sender::sendMessage));
    }

    public static @NotNull BungeeMessageValueBuilder<BaseComponent[]> value() {
        return ConfiguredMessage.create(getParser()).whenSend(CommandSender::sendMessage);
    }

    public static @NotNull BiFunction<CommandSender, String, BaseComponent[]> getParser() {
        return (sender, message) -> parse(message);
    }


    public static final ConfiguredMessageList<BaseComponent[]> NO_PERMISSION = list()
            .defaults("&c&l抱歉！&f但您没有足够的权限这么做。")
            .build();

    public static final ConfiguredMessageList<BaseComponent[]> COMMAND_USAGE = list()
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
                    "&8#&f reload",
                    "&8-&7 重载插件配置文件。"
            )
            .build();


    public static final class MAINTENANCE extends ConfigurationRoot {

        public static final ConfiguredMessageList<BaseComponent[]> ENABLED = list()
                .defaults("&f成功 &a&l启用 &f维护模式，非白名单玩家将无法连接到服务器。")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> DISABLED = list()
                .defaults("&f成功 &c&l关闭 &f维护模式，所有玩家现均可以连接到服务器。")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> ALREADY_ALLOWED = list()
                .defaults("&f玩家 &e%(player) &f已经在白名单中了。")
                .params("player")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> ALLOWED = list()
                .defaults("&f成功将玩家 &e%(player) &f添加到维护白名单。")
                .params("player")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> ALREADY_DENIED = list()
                .defaults("&f玩家 &e%(player) &f并不在维护白名单中。")
                .params("player")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> DENIED = list()
                .defaults("&f成功将玩家 &e%(player) &f移出维护白名单。")
                .params("player")
                .build();


    }

    public static final class DISPLAY extends ConfigurationRoot {

        public static final ConfiguredMessageList<BaseComponent[]> NONE_MATCH = list()
                .defaults("&f未找到任何对应的服务器展示信息。")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> INFO = list()
                .defaults("&f共有 &e%(count) &f个服务器展示信息匹配，如下：")
                .params("count").build();

        public static final ConfiguredMessageList<BaseComponent[]> VALUE = list()
                .defaults("&8#&f%(index) &e%(id) &8- &f%(online)&7/%(max)",
                        "&8 | &r%(line1)",
                        "&8 | &r%(line2)",
                        "&8 - &7协议&8[&f%(protocol-version)&8] &r%(protocol-name)"
                ).params("index", "id", "online", "max", "line1", "line2", "protocol-version", "protocol-name")
                .build();

    }

    public static final class RELOAD extends ConfigurationRoot {

        public static final ConfiguredMessageList<BaseComponent[]> SUCCESS = list()
                .defaults("&a&l重载完成！&f耗时 &6%(time)ms &f，共加载了 &e%(count) &f条配置。")
                .params("time", "count")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> FAILED = list()
                .defaults("&c&l重载失败！&f请查看后台详细报错。")
                .build();
    }

}
