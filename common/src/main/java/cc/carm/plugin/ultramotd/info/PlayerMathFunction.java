package cc.carm.plugin.ultramotd.info;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface PlayerMathFunction extends UnaryOperator<Integer> {

    static PlayerMathFunction identity() {
        return t -> t;
    }

    static PlayerMathFunction multiple(int times) {
        return input -> input * times;
    }

    static PlayerMathFunction dynamic() {
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

    static PlayerMathFunction parse(String input) {
        if (input == null || input.isEmpty()) return identity();

        String[] split = input.split(":");
        String type = split[0];
        if (type.equalsIgnoreCase("multiple")) {
            int times = 2;
            try {
                times = Integer.parseInt(split[1]);
            } catch (Exception e) {
            }
            return multiple(times);
        } else if (type.equalsIgnoreCase("dynamic")) {
            return dynamic();
        } else if (type.equalsIgnoreCase("zero")) {
            return zero();
        } else return identity();
    }
}
