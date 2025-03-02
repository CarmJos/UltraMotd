package cc.carm.plugin.ultramotd.command.maintenance;

import cc.carm.plugin.ultramotd.Main;
import cc.carm.plugin.ultramotd.command.sub.MaintenanceCommands;
import cc.carm.plugin.ultramotd.command.utils.SubCommand;
import ultramotd.conf.PluginMessages;
import ultramotd.manager.DisplayManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SkipCommand extends SubCommand<MaintenanceCommands> {

    public SkipCommand(@NotNull MaintenanceCommands parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(Plugin plugin, CommandSender sender, String[] args) throws Exception {

        int days = 0;
        if (args.length > 0) {
            try {
                days = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }

        DisplayManager manager = Main.getDisplayManager();

        if (days < 0) {
            manager.setSkipUntil(null);
            PluginMessages.MAINTENANCE.SCHEDULE_ENABLE.send(sender);
        } else {
            LocalDate skipUntil = LocalDate.now().plusDays(days);
            manager.setSkipUntil(skipUntil);
            PluginMessages.MAINTENANCE.SCHEDULE_SKIP.send(sender, skipUntil.format(DateTimeFormatter.ISO_DATE), days + 1);
        }
        return null;
    }
}
