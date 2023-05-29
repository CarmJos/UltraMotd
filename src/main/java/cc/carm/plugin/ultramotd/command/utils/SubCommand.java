package cc.carm.plugin.ultramotd.command.utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public abstract class SubCommand<C extends CommandHandler> implements NamedExecutor {

    private final @NotNull C parent;

    private final String identifier;
    private final String[] aliases;

    public SubCommand(@NotNull C parent, String identifier, String... aliases) {
        this.parent = parent;
        this.identifier = identifier;
        this.aliases = aliases;
    }

    public @NotNull C getParent() {
        return parent;
    }

    @Override
    public @NotNull String getIdentifier() {
        return this.identifier;
    }

    @NotNull
    @Override
    public String[] getAliases() {
        return aliases;
    }

    public abstract Void execute(Plugin plugin, CommandSender sender, String[] args) throws Exception;

    public List<String> tabComplete(Plugin plugin, CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

}
