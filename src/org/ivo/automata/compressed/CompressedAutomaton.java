package org.ivo.automata.compressed;

import java.util.Arrays;

import org.ivo.automata.AbstractDeterministicAutomaton;
import org.ivo.automata.IDeterministicAutomaton;
import org.ivo.automata.State;
import org.ivo.automata.util.Measure;

public class CompressedAutomaton extends AbstractDeterministicAutomaton {
    
    private static final int LOOK_BACK = 3;
    protected Transition[] table = new Transition[1];
    
    public CompressedAutomaton(final IDeterministicAutomaton source) {
        final Measure m = new Measure();
        m.start();
        int transitionsCount = 0;
        try {
            this.states = new State[source.getNumStates()];
            final State[] sourceStates = source.getStates();
            for (int i = 0; i < states.length; i++) {
                final CState newState = new CState(this, i);
                states[i] = newState;
                final State oldState = sourceStates[i];
                newState.setFinal(oldState.isFinal());
                if (oldState.getTransitionsCount() == 0) {
                    continue;
                }
                transitionsCount += oldState.getTransitionsCount();
                final int lastChar = oldState.getTransitionAtIndex(oldState.getTransitionsCount() - 1);
                final int tablePos = findPosition(oldState);
                if (tablePos + lastChar >= table.length) {
                    final int newTableSize = (int) Math.max(table.length * 1.75, table.length + lastChar);
                    table = Arrays.copyOf(table, newTableSize);
                }
                insertStates(tablePos, oldState, newState);
                newState.setTablePos(tablePos);
            }
        } finally {
            m.end();
            m.print(this.getClass().getSimpleName() + " states=" + getNumStates() + " transCount=" + transitionsCount
                    + " tableSize=" + table.length);
        }
    }
    
    private void insertStates(final int tablePos, final State oldState, final CState newState) {
        for (int i = 0; i < oldState.getTransitionsCount(); i++) {
            final int ch = oldState.getTransitionAtIndex(i);
            final Transition transition = new Transition(oldState.getTransitionTarget(ch), ch);
            transition.source = newState.getIndex();
            table[tablePos + ch] = transition;
        }
    }
    
    protected int findPosition(final State oldState) {
        int pos = Math.max(0, table.length / LOOK_BACK);
        // int pos = 0;
        while (!isFree(pos, oldState)) {
            pos++;
        }
        return pos;
    }
    
    private boolean isFree(final int pos, final State oldState) {
        for (int i = 0; i < oldState.getTransitionsCount(); i++) {
            final int ch = oldState.getTransitionAtIndex(i);
            final int j = pos + ch;
            if (j < table.length && table[j] != null) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected State newState() {
        return null;
    }
    
    @Override
    protected State addState(final State state) {
        return state;
    }
    
    @Override
    public int getNumStates() {
        return states.length;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append('[');
        builder.append(this.getClass().getName());
        builder.append(", states=");
        builder.append(states.length);
        builder.append(", table=");
        builder.append(table.length);
        builder.append(']');
        return builder.toString();
    }
}
