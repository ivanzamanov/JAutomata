package org.ivo.automata.compressed;

public class Transition implements Comparable<Transition> {
    public int source;
    public int target;
    public final int ch;

    public Transition(final int target, final int ch) {
        if (source < 0 || target < 0) {
            throw new IllegalArgumentException("Source or destination cannot be < 0. Got " + target);
        }
        this.target = target;
        this.ch = ch;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Transition) {
            final Transition tr = (Transition) obj;
            return tr.ch == this.ch && tr.source == source && tr.target == target;
        }
        return super.equals(obj);
    }

    @Override
    public int compareTo(final Transition o) {
        if (ch > o.ch) {
            return 1;
        } else if (ch < o.ch) {
            return -1;
        } else {
            return 0;
        }
    }
}
