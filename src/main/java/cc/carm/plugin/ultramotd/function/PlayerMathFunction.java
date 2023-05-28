package cc.carm.plugin.ultramotd.function;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface PlayerMathFunction extends UnaryOperator<Integer> {

    static PlayerMathFunction multiple() {
        return input -> input * 2;
    }

    static PlayerMathFunction dynamicAdd() {
        return input -> {
            if (input < 5) {
                return input * 2;
            } else if (input <= 10) {
                return (int) (input * 1.6);
            } else if (input <= 20) {
                return (int) (input * 1.4);
            } else if (input <= 50) {
                return (int) (input * 1.2);
            } else return (int) (input * 1.1);
        };
    }

    static PlayerMathFunction zero() {
        return input -> 0;
    }

}
