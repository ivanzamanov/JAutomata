package org.ivo.automata.collections;

import java.util.HashMap;

import org.ivo.automata.State;

public class NativeStatesTable extends StatesTable {
    
    private final HashMap<State, State> delegate;
    
    public NativeStatesTable(final float loadFactor) {
        super(0);
        delegate = new HashMap<State, State>(16, loadFactor);
    }
    
    @Override
    public void add(final State state) {
        delegate.put(state, state);
    }
    
    @Override
    public State addOrGet(final State state) {
        State result = delegate.get(state);
        if (result == null) {
            delegate.put(state, state);
            result = state;
        }
        return result;
    }
    
    @Override
    public boolean remove(final State state) {
        return delegate.remove(state) != null;
    }
}
