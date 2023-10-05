package cc.carm.plugin.ultramotd.listener;

import cc.carm.plugin.ultramotd.Main;
import cc.carm.plugin.ultramotd.conf.PluginConfig;
import cc.carm.plugin.ultramotd.event.MOTDResponseEvent;
import cc.carm.plugin.ultramotd.info.DisplayContent;
import cc.carm.plugin.ultramotd.manager.DisplayManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyListener implements Listener {

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        DisplayManager manager = Main.getDisplayManager();

        DisplayContent content;
        if (manager.isMaintenance()) {
            content = manager.getContents().get(PluginConfig.MAINTENANCE.DISPLAY.getNotNull());
        } else {
            content = manager.getDisplay(event.getConnection().getVirtualHost());
        }

        MOTDResponseEvent responseEvent = new MOTDResponseEvent(event.getConnection(), content);
        Main.getInstance().getProxy().getPluginManager().callEvent(responseEvent);

        DisplayContent response = responseEvent.getContent();
        if (response == null) return;

        event.setResponse(manager.applyContent(response, event.getResponse()));
    }

    @EventHandler
    public void onJoin(PreLoginEvent event) {
        DisplayManager manager = Main.getDisplayManager();
        if (!manager.isMaintenance()) return;

        String username = event.getConnection().getName();
        if (PluginConfig.MAINTENANCE.ALLOWED_PLAYERS.stream().anyMatch(s -> s.equalsIgnoreCase(username))) return;

        BaseComponent[] messages = PluginConfig.MAINTENANCE.KICK_MESSAGE.parseToLine(null);
        if (messages != null) event.setCancelReason(messages);
        event.setCancelled(true);
    }


}
