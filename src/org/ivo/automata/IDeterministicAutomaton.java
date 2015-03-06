package org.ivo.automata;

public interface IDeterministicAutomaton {
    State getStartState();

    boolean traverse(final SequenceReader reader);

    State nextState(final State fromState, final int withChar);

    int getNumStates();

    State[] getStates();
}
