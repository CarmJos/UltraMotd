package cc.carm.plugin.ultramotd.info;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.plugin.ultramotd.UltraMOTD;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DisplayPlayers {

    public static DisplayPlayers empty() {
        return new DisplayPlayers(null, null, null);
    }

    protected final @Nullable List<String> samples;

    protected final @Nullable Integer max;
    protected final @Nullable PlayerMathFunction mathFunction;

    public DisplayPlayers(@Nullable List<String> samples, @Nullable Integer max,
                          @Nullable PlayerMathFunction mathFunction) {
        this.samples = samples;
        this.max = max;
        this.mathFunction = mathFunction;
    }

    public @Nullable List<String> getSamples() {
        return samples;
    }

    public int getMaxPlayers() {
        return max != null ? max : UltraMOTD.getMaxPlayers();
    }

    public int countPlayers() {
        return UltraMOTD.getOnlinePlayers();
    }

    public int mathPlayers(int online) {
        if (mathFunction == null) return online;
        else return mathFunction.apply(online);
    }

    public int getDisplayPlayers() {
        return mathPlayers(countPlayers());
    }

    public static @NotNull DisplayPlayers parse(ConfigureSection section) {

        List<String> samples = section.getStringList("samples");
        Integer max = section.getInt("max");
        PlayerMathFunction mathFunction = PlayerMathFunction.parse(section.getString("math"));

        return new DisplayPlayers(samples, max, mathFunction);

    }

}
