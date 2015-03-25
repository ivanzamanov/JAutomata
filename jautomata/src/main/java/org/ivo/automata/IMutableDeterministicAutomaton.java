package org.ivo.automata;


public interface IMutableDeterministicAutomaton extends IDeterministicAutomaton {
    void deleteState(final State state);
    
    void setStartState(final State state);
}
