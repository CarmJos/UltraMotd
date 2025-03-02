package cc.carm.plugin.ultramotd.event;

import ultramotd.info.DisplayContent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.plugin.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Optional;

public class MOTDResponseEvent extends Event {

    private final @NotNull PendingConnection connection;
    private @Nullable DisplayContent content;

    public MOTDResponseEvent(@NotNull PendingConnection connection, @Nullable DisplayContent content) {
        this.connection = connection;
        this.content = content;
    }

    public @NotNull PendingConnection getConnection() {
        return connection;
    }

    public @Nullable String getConnectHostname() {
        return Optional.ofNullable(getConnection().getVirtualHost())
                .map(InetSocketAddress::getHostName).orElse(null);
    }

    public @Nullable DisplayContent getContent() {
        return content;
    }

    public void setContent(@Nullable DisplayContent content) {
        this.content = content;
    }

}