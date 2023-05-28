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


}
