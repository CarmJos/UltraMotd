package cc.carm.plugin.ultramotd.listener;

import cc.carm.plugin.ultramotd.Main;
import cc.carm.plugin.ultramotd.conf.PluginConfig;
import cc.carm.plugin.ultramotd.event.MOTDResponseEvent;
import cc.carm.plugin.ultramotd.info.DisplayContent;
import cc.carm.plugin.ultramotd.manager.DisplayManager;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.time.LocalTime;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ProxyListener implements Listener {

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        DisplayManager manager = Main.getDisplayManager();

        DisplayContent content;
        if (isMaintenance()) {
            content = manager.getContents().get(PluginConfig.MAINTENANCE.DISPLAY.getNotNull());
        } else {
            content = manager.getDisplay(event.getConnection().getVirtualHost());
        }

        MOTDResponseEvent responseEvent = new MOTDResponseEvent(event.getConnection(), content);
        Main.getInstance().getProxy().getPluginManager().callEvent(responseEvent);

        DisplayContent response = responseEvent.getContent();
        if (response == null) return;

        ServerPing ping;

        ServerPing provided = Optional.ofNullable(event.getResponse()).orElse(new ServerPing());
        if (manager.getCache() == null) {
            ping = response.applyTo(provided);
        } else {
            try {
                ping = manager.getCache().get(response.getID(), () -> response.applyTo(provided));
            } catch (ExecutionException e) {
                ping = response.applyTo(provided);
            }
        }

        event.setResponse(ping);
    }

    @EventHandler
    public void onJoin(PreLoginEvent event) {
        if (!isMaintenance()) return;

        String username = event.getConnection().getName();
        if (PluginConfig.MAINTENANCE.ALLOWED_PLAYERS.stream().anyMatch(s -> s.equalsIgnoreCase(username))) return;

        BaseComponent[] messages = PluginConfig.MAINTENANCE.KICK_MESSAGE.parseToLine(null);
        if (messages != null) event.setCancelReason(messages);
        event.setCancelled(true);
    }

    public boolean isMaintenance() {
        if (PluginConfig.MAINTENANCE.ENABLE.getNotNull()) return true;
        if (!PluginConfig.MAINTENANCE.SCHEDULE.ENABLE.getNotNull()) return false;

        LocalTime start = PluginConfig.MAINTENANCE.SCHEDULE.START.getNotNull();
        LocalTime end = PluginConfig.MAINTENANCE.SCHEDULE.END.getNotNull();

        LocalTime now = LocalTime.now();
        if (start.isBefore(end)) {
            return now.isAfter(start) && now.isBefore(end);
        } else {
            return now.isAfter(start) || now.isBefore(end);
        }
    }

}
