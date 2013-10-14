package org.ivo.automata;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Abstract implementation of a determined automaton.
 * 
 * @author ivo
 * @param <T>
 *            Class of the automaton's states.
 */
public abstract class AbstractDeterministicAutomaton implements IMutableDeterministicAutomaton {
    
    protected State[] states = new State[1];
    protected int state_index = 0;
    private int startState = 0;
    protected final LinkedList<Integer> deletedIndices = new LinkedList<Integer>();
    protected int deletedCount = 0;
    
    protected State addState(final State state) {
        int newStateIndex;
        if (!deletedIndices.isEmpty()) {
            newStateIndex = deletedIndices.pop();
            final State reusedState = states[newStateIndex];
            reusedState.setIndex(newStateIndex);
            return reusedState;
        } else {
            newStateIndex = state_index++;
            if (states.length <= newStateIndex) {
                states = Arrays.copyOf(states, (int) Math.ceil(newStateIndex * 1.3));
            }
            state.setIndex(newStateIndex);
            states[newStateIndex] = state;
            return state;
        }
    }
    
    protected State newState() {
        final State state = new State();
        return addState(state);
    }
    
    protected State buildState() {
        final State state = new State();
        return state;
    }
    
    @Override
    public boolean traverse(final Reader is) {
        State currentState = getStartState();
        try {
            while (currentState != null) {
                final int read = is.read();
                if (read == -1) {
                    break;
                }
                final char ch = (char) read;
                currentState = nextState(currentState, ch);
            }
        } catch (final IOException e) {
            // End of stream.
        }
        return currentState != null && currentState.isFinal();
    }
    
    @Override
    public State nextState(final State fromState, final int ch) {
        final int targetIndex = fromState.getTransitionTarget(ch);
        if (targetIndex >= 0 && states[targetIndex].getIndex() >= 0) {
            return states[targetIndex];
        } else {
            return null;
        }
    }
    
    @Override
    public State getStartState() {
        if (startState >= 0 && startState < states.length) {
            return states[startState];
        } else {
            return null;
        }
    }
    
    @Override
    public void deleteState(final State state) {
        deletedCount++;
        final int index = state.getIndex();
        state.clear();
        this.deletedIndices.push(index);
    }
    
    @Override
    public State[] getStates() {
        return states;
    }
    
    @Override
    public final void setStartState(final State state) {
        this.startState = state.getIndex();
    }
    
    @Override
    public int getNumStates() {
        return state_index - deletedIndices.size();
    }
}
