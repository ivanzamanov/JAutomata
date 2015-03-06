package org.ivo.automata.util;

public class Measure {

    private long startTime;
    private long endTime;

    public Measure() {

    }

    public void start() {
        startTime = System.nanoTime();
    }

    public void end() {
        endTime = System.nanoTime();
    }

    public void print(final String message) {
        final long nanos = endTime - startTime;
        System.out.println(message + " = " + nanos / Math.pow(10, 9) + " s");
    }
}
