package cc.carm.plugin.ultramotd.command;

import cc.carm.plugin.ultramotd.command.sub.MaintenanceCommands;
import cc.carm.plugin.ultramotd.command.sub.DisplayCommand;
import cc.carm.plugin.ultramotd.command.sub.ReloadCommand;
import cc.carm.plugin.ultramotd.command.utils.CommandHandler;
import ultramotd.conf.PluginMessages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends CommandHandler {

    public MainCommand(@NotNull Plugin plugin) {
        super(plugin);
        registerSubCommand(new DisplayCommand(this, "display", "show"));
        registerHandler(new MaintenanceCommands(plugin, this, "maintenance", "mt"));
        registerSubCommand(new ReloadCommand(this, "reload", "rl"));
    }

    @Override
    public Void noArgs(@NotNull CommandSender sender) {
        PluginMessages.COMMAND_USAGE.send(sender);
        return null;
    }

    @Override
    public Void noPermission(@NotNull CommandSender sender) {
        PluginMessages.NO_PERMISSION.send(sender);
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("ultramotd.admin");
    }

}
