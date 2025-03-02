package cc.carm.plugin.ultramotd.command.maintenance;

import cc.carm.plugin.ultramotd.Main;
import cc.carm.plugin.ultramotd.command.sub.MaintenanceCommands;
import cc.carm.plugin.ultramotd.command.utils.SubCommand;
import ultramotd.conf.PluginConfig;
import ultramotd.conf.PluginMessages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ToggleCommand extends SubCommand<MaintenanceCommands> {

    public ToggleCommand(@NotNull MaintenanceCommands parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(Plugin plugin, CommandSender sender, String[] args) throws Exception {
        boolean enabled = PluginConfig.MAINTENANCE.ENABLE.getNotNull();
        if (enabled) {
            PluginConfig.MAINTENANCE.ENABLE.set(false);
            PluginMessages.MAINTENANCE.DISABLED.send(sender);
        } else {
            PluginConfig.MAINTENANCE.ENABLE.set(true);
            PluginMessages.MAINTENANCE.ENABLED.send(sender);
            if (PluginConfig.MAINTENANCE.KICK_ONLINE.getNotNull()) {
                Main.getInstance().getProxy().getPlayers().stream()
                        .filter(p -> PluginConfig.MAINTENANCE.ALLOWED_PLAYERS.stream().noneMatch(s -> p.getName().equalsIgnoreCase(s)))
                        .forEach(player -> player.disconnect(PluginConfig.MAINTENANCE.KICK_MESSAGE.parseToLine(player)));
            }
        }

        Main.getInstance().getConfigProvider().save();
        return null;
    }
}
