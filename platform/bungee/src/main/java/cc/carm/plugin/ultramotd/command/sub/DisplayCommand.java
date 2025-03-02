package cc.carm.plugin.ultramotd.command.sub;

import cc.carm.plugin.ultramotd.Main;
import cc.carm.plugin.ultramotd.command.MainCommand;
import cc.carm.plugin.ultramotd.command.utils.SubCommand;
import ultramotd.conf.PluginMessages;
import ultramotd.info.DisplayContent;
import ultramotd.info.DisplayProtocol;
import ultramotd.manager.DisplayManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class DisplayCommand extends SubCommand<MainCommand> {

    public DisplayCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(Plugin plugin, CommandSender sender, String[] args) {
        DisplayManager manager = Main.getDisplayManager();

        List<DisplayContent> displays;
        if (args.length < 1) {
            displays = manager.getMatchedContents(null);
        } else {
            displays = manager.getMatchedContents(args[0]);
        }

        if (displays.isEmpty()) {
            PluginMessages.DISPLAY.NONE_MATCH.send(sender);
            return null;
        }

        PluginMessages.DISPLAY.INFO.send(sender, displays.size());
        int index = 1;
        for (DisplayContent display : displays) {
            PluginMessages.DISPLAY.VALUE.send(
                    sender,
                    index, display.getID(),
                    display.getDisplayPlayers().getDisplayPlayers(),
                    display.getDisplayPlayers().getMaxPlayers(),
                    Optional.ofNullable(display.getLine1()).orElse(""),
                    Optional.ofNullable(display.getLine2()).orElse(""),
                    Optional.ofNullable(display.getDisplayProtocol()).map(DisplayProtocol::getVersion).orElse(0),
                    Optional.ofNullable(display.getDisplayProtocol()).map(DisplayProtocol::getName).orElse(ProxyServer.getInstance().getName())
            );
            index++;
        }


        return null;
    }


}
