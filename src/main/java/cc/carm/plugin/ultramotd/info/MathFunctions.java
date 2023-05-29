package cc.carm.plugin.ultramotd.info;

import cc.carm.plugin.ultramotd.function.PlayerMathFunction;

import java.util.Arrays;

public enum MathFunctions {
    IDENTITY(PlayerMathFunction.identity()),
    MULTIPLIER(PlayerMathFunction.multiple()),
    DYNAMIC(PlayerMathFunction.dynamic()),
    ZERO(PlayerMathFunction.zero());

    private final PlayerMathFunction function;

    MathFunctions(PlayerMathFunction function) {
        this.function = function;
    }

    public PlayerMathFunction getFunction() {
        return function;
    }

    public int calculate(int online) {
        return function.apply(online);
    }

    public static MathFunctions parse(String input) {
        return Arrays.stream(MathFunctions.values())
                .filter(f -> f.name().equalsIgnoreCase(input))
                .findFirst().orElse(null);
    }
}
