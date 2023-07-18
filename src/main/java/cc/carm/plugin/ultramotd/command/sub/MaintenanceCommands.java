package cc.carm.plugin.ultramotd.command.sub;

import cc.carm.plugin.ultramotd.command.MainCommand;
import cc.carm.plugin.ultramotd.command.maintenance.AllowCommand;
import cc.carm.plugin.ultramotd.command.maintenance.DenyCommand;
import cc.carm.plugin.ultramotd.command.maintenance.ToggleCommand;
import cc.carm.plugin.ultramotd.command.utils.CommandHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class MaintenanceCommands extends CommandHandler {

    protected final MainCommand parent;

    public MaintenanceCommands(@NotNull Plugin plugin, MainCommand parent, String identifier, String... aliases) {
        super(plugin, identifier, aliases);
        this.parent = parent;
        registerSubCommand(new ToggleCommand(this, "toggle", "t"));
        registerSubCommand(new AllowCommand(this, "allow", "a"));
        registerSubCommand(new DenyCommand(this, "deny", "d"));
    }

    @Override
    public Void noArgs(CommandSender sender) {
        return parent.noArgs(sender);
    }

    @Override
    public Void noPermission(CommandSender sender) {
        return parent.noPermission(sender);
    }
}
