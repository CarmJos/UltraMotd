package cc.carm.plugin.ultramotd.manager;

import cc.carm.plugin.ultramotd.function.ServerCountPredicate;
import cc.carm.plugin.ultramotd.info.DisplayContents;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DisplayManager {

    protected final Map<String, DisplayContents> contents = new LinkedHashMap<>();
    protected final Cache<String, ServerPing> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(500, TimeUnit.MILLISECONDS)
            .maximumSize(50).build();

    protected @NotNull ServerCountPredicate countPredicate = ServerCountPredicate.byName();

    public void handle(@NotNull ProxyPingEvent event) {


    }

    public Map<String, DisplayContents> getContents() {
        return contents;
    }

    public List<DisplayContents> getMatchedContents(String hostname) {
        return getContents().values().stream()
                .filter(c -> c.matchHostname(hostname)).collect(Collectors.toList());
    }

    public @Nullable DisplayContents getDisplay(String hostname) {
        List<DisplayContents> displays = getMatchedContents(hostname);
        if (displays.isEmpty()) return null;
        Collections.shuffle(displays);
        return displays.get(0);
    }

    public @NotNull ServerCountPredicate getCountPredicate() {
        return countPredicate;
    }

    public void setCountPredicate(@NotNull ServerCountPredicate predicate) {
        this.countPredicate = predicate;
    }


}
