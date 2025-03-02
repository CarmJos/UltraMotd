package cc.carm.plugin.ultramotd.command.utils;

import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.UnaryOperator;

public interface NamedExecutor {

    @NotNull String getIdentifier();

    @NotNull String[] getAliases();

    default boolean hasPermission(@NotNull CommandSender sender) {
        return true;
    }

    default Void sendMessage(@NotNull CommandSender sender, @NotNull String... messages) {
        return sendMessage(sender, UnaryOperator.identity(), messages);
    }

    default Void sendMessage(@NotNull CommandSender sender,
                             @Nullable UnaryOperator<String> parser,
                             @NotNull String... messages) {
        if (messages.length == 0) return null;
        UnaryOperator<String> finalParser = Optional.ofNullable(parser).orElse(UnaryOperator.identity());
        Arrays.stream(messages).map(finalParser).forEach(sender::sendMessage);
        return null;
    }

}
