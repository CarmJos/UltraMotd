package cc.carm.plugin.ultramotd.command.maintenance;

import cc.carm.plugin.ultramotd.Main;
import cc.carm.plugin.ultramotd.command.utils.SubCommand;
import cc.carm.plugin.ultramotd.conf.PluginConfig;
import cc.carm.plugin.ultramotd.conf.PluginMessages;
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
        }

        Main.getInstance().getConfigProvider().save();
        return null;
    }
}
