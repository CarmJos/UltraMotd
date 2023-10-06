package cc.carm.plugin.ultramotd.command.utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("UnusedReturnValue")
public abstract class CommandHandler extends Command implements NamedExecutor, TabExecutor {

    protected final @NotNull Plugin plugin;

    protected final @NotNull Map<String, SubCommand<?>> registeredCommands = new HashMap<>();
    protected final @NotNull Map<String, CommandHandler> registeredHandlers = new HashMap<>();

    protected final @NotNull Map<String, String> aliasesMap = new HashMap<>();

    public CommandHandler(@NotNull Plugin plugin) {
        this(plugin, plugin.getDescription().getName());
    }

    public CommandHandler(@NotNull Plugin plugin, @NotNull String cmd) {
        this(plugin, cmd, new String[0]);
    }

    public CommandHandler(@NotNull Plugin plugin, @NotNull String cmd, @NotNull String... aliases) {
        super(cmd, null, aliases);
        this.plugin = plugin;
    }

    public abstract Void noArgs(@NotNull CommandSender sender);

    public Void unknownCommand(@NotNull CommandSender sender, String[] args) {
        return noArgs(sender);
    }

    public abstract Void noPermission(@NotNull CommandSender sender);

    public Void onException(@NotNull CommandSender sender, SubCommand<?> cmd, Exception ex) {
        sender.sendMessage("Error occurred when executing " + cmd.getIdentifier() + ": " + ex.getLocalizedMessage());
        ex.printStackTrace();
        return null;
    }

    @Override
    public @NotNull String getIdentifier() {
        return this.getName();
    }

    public void registerSubCommand(SubCommand<?> command) {
        String name = command.getIdentifier().toLowerCase();
        this.registeredCommands.put(name, command);
        Arrays.stream(command.getAliases()).forEach(alias -> this.aliasesMap.put(alias.toLowerCase(), name));
    }

    public void registerHandler(CommandHandler handler) {
        String name = handler.getIdentifier().toLowerCase();
        this.registeredHandlers.put(name, handler);
        Arrays.stream(handler.getAliases()).forEach(alias -> this.aliasesMap.put(alias.toLowerCase(), name));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!this.hasPermission(sender)) {
            noPermission(sender);
            return;
        }

        if (args.length == 0) {
            this.noArgs(sender);
            return;
        }

        String input = args[0].toLowerCase();

        CommandHandler handler = getHandler(input);
        if (handler != null) {
            if (!handler.hasPermission(sender)) {
                this.noPermission(sender);
            } else {
                handler.execute(sender, this.shortenArgs(args));
            }
            return;
        }

        SubCommand<?> sub = getSubCommand(input);
        if (sub == null) {
            this.unknownCommand(sender, args);
        } else if (!sub.hasPermission(sender)) {
            this.noPermission(sender);
        } else {
            try {
                sub.execute(this.plugin, sender, this.shortenArgs(args));
            } catch (Exception ex) {
                this.onException(sender, sub, ex);
            }
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, String[] args) {
        if (args.length == 0) return Collections.emptyList();

        String input = args[0].toLowerCase();
        if (args.length == 1) {
            return getExecutors().stream()
                    .filter(e -> e.hasPermission(sender))
                    .map(NamedExecutor::getIdentifier)
                    .filter(s -> s.toLowerCase().startsWith(input.toLowerCase()))
                    .collect(Collectors.toList());
        } else {

            CommandHandler handler = getHandler(input);
            if (handler != null && handler.hasPermission(sender)) {
                return handler.onTabComplete(sender, this.shortenArgs(args));
            }

            SubCommand<?> sub = getSubCommand(input);
            if (sub != null && sub.hasPermission(sender)) {
                return sub.tabComplete(this.plugin, sender, this.shortenArgs(args));
            }

            return Collections.emptyList();
        }
    }

    public List<NamedExecutor> getExecutors() {
        Set<NamedExecutor> executors = new HashSet<>();
        executors.addAll(this.registeredHandlers.values());
        executors.addAll(this.registeredCommands.values());
        List<NamedExecutor> sortedExecutors = new ArrayList<>(executors);
        sortedExecutors.sort(Comparator.comparing(NamedExecutor::getIdentifier));
        return sortedExecutors;
    }

    protected @Nullable CommandHandler getHandler(@NotNull String name) {
        CommandHandler fromName = this.registeredHandlers.get(name);
        if (fromName != null) return fromName;

        String nameFromAlias = this.aliasesMap.get(name);
        if (nameFromAlias == null) return null;
        else return this.registeredHandlers.get(nameFromAlias);
    }

    protected @Nullable SubCommand<?> getSubCommand(@NotNull String name) {
        SubCommand<?> fromName = this.registeredCommands.get(name);
        if (fromName != null) return fromName;

        String nameFromAlias = this.aliasesMap.get(name);
        if (nameFromAlias == null) return null;
        else return this.registeredCommands.get(nameFromAlias);
    }

    protected String[] shortenArgs(String[] args) {
        if (args.length == 0) {
            return args;
        } else {
            List<String> argList = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
            return argList.toArray(new String[0]);
        }
    }

}
