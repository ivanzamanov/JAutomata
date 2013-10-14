package org.ivo.automata.compressed;

import org.ivo.automata.State;

public class CState extends State {
    private final CompressedAutomaton owner;
    private int tablePos = 0;
    
    public CState(final CompressedAutomaton owner, final int i) {
        this.owner = owner;
        setIndex(i);
    }
    
    public int getTablePos() {
        return tablePos;
    }
    
    public void setTablePos(final int tablePos) {
        this.tablePos = tablePos;
    }
    
    @Override
    public int addTransition(final int target, final int ch) {
        return -1;
    }
    
    @Override
    public boolean hasTransition(final char ch) {
        final Transition tr = owner.table[getTablePos() + ch];
        return tr.source == getIndex();
    }
    
    @Override
    public int getTransitionTarget(final int ch) {
        final Transition tr = owner.table[getTablePos() + ch];
        if (tr.source == getIndex()) {
            return tr.target;
        } else {
            return -1;
        }
    }
}
