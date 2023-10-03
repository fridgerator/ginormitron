package com.fridgerator.ginormitron.customerdata.generator;

public class GeneratorCounter {
    static long generated = 0;

    public static void incrementGeneratedCount() {
        generated++;
    }

    public static long getGeneratedCount() {
        return generated;
    }
}
