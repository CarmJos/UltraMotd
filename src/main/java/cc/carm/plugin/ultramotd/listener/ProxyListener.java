package cc.carm.plugin.ultramotd.listener;

import cc.carm.plugin.ultramotd.Main;
import cc.carm.plugin.ultramotd.conf.PluginConfig;
import cc.carm.plugin.ultramotd.info.DisplayContent;
import cc.carm.plugin.ultramotd.manager.DisplayManager;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ProxyListener implements Listener {

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        DisplayManager manager = Main.getDisplayManager();


        DisplayContent content;
        if (PluginConfig.MAINTENANCE.ENABLE.getNotNull()) {
            content = manager.getContents().get(PluginConfig.MAINTENANCE.DISPLAY.getNotNull());
        } else {
            content = manager.getDisplay(event.getConnection().getVirtualHost());
        }

        if (content == null) return;

        ServerPing ping;

        ServerPing provided = Optional.ofNullable(event.getResponse()).orElse(new ServerPing());
        if (manager.getCache() == null) {
            ping = content.applyTo(provided);
        } else {
            try {
                ping = manager.getCache().get(content.getID(), () -> content.applyTo(provided));
            } catch (ExecutionException e) {
                ping = content.applyTo(provided);
            }
        }

        event.setResponse(ping);
    }

    @EventHandler
    public void onJoin(PreLoginEvent event) {
        if (!PluginConfig.MAINTENANCE.ENABLE.getNotNull()) return;

        String username = event.getConnection().getName();
        if (PluginConfig.MAINTENANCE.ALLOWED_PLAYERS.stream().anyMatch(s -> s.equalsIgnoreCase(username))) return;

        BaseComponent[] messages = PluginConfig.MAINTENANCE.KICK_MESSAGE.parseToLine(null);
        if (messages != null) event.setCancelReason(messages);
        event.setCancelled(true);

    }

}
