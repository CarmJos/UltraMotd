package cc.carm.plugin.ultramotd.command.maintenance;

import cc.carm.plugin.ultramotd.Main;
import cc.carm.plugin.ultramotd.command.sub.MaintenanceCommands;
import cc.carm.plugin.ultramotd.command.utils.SubCommand;
import cc.carm.plugin.ultramotd.conf.PluginConfig;
import cc.carm.plugin.ultramotd.conf.PluginMessages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class DenyCommand extends SubCommand<MaintenanceCommands> {

    public DenyCommand(@NotNull MaintenanceCommands parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(Plugin plugin, CommandSender sender, String[] args) throws Exception {
        if (args.length != 1) return getParent().noArgs(sender);

        String username = args[0];
        if (PluginConfig.MAINTENANCE.ALLOWED_PLAYERS.stream().noneMatch(s -> s.equalsIgnoreCase(username))) {
            PluginMessages.MAINTENANCE.ALREADY_DENIED.send(sender, username);
            return null;
        }

        PluginConfig.MAINTENANCE.ALLOWED_PLAYERS.modify(list -> list.removeIf(s -> s.equalsIgnoreCase(username)));
        Main.getInstance().getConfigProvider().save();
        PluginMessages.MAINTENANCE.DENIED.send(sender, username);

        return null;
    }
}
