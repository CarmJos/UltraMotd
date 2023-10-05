package cc.carm.plugin.ultramotd.command.sub;

import cc.carm.plugin.ultramotd.Main;
import cc.carm.plugin.ultramotd.command.MainCommand;
import cc.carm.plugin.ultramotd.command.utils.SubCommand;
import cc.carm.plugin.ultramotd.conf.PluginMessages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends SubCommand<MainCommand> {

    public ReloadCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(Plugin plugin, CommandSender sender, String[] args) {
        long s1 = System.currentTimeMillis();

        try {
            Main.getInstance().getConfigProvider().reload();
            Main.getInstance().getMessageProvider().reload();
            Main.getDisplayManager().reloadDisplays();
            Main.getDisplayManager().reloadCacheService();
            PluginMessages.RELOAD.SUCCESS.send(sender, (System.currentTimeMillis() - s1), Main.getDisplayManager().getContents().size());
        } catch (Exception ex) {
            PluginMessages.RELOAD.FAILED.send(sender);
            ex.printStackTrace();
        }

        return null;
    }


}
