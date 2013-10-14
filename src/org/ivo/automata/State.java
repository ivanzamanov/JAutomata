package org.ivo.automata;

import java.util.Arrays;

public class State {
    
    private int index = -1;
    protected int[] trChar = new int[1];
    protected int[] trTarget = new int[1];
    private boolean isFinal = false;
    protected int transitionsCount = 0;
    
    public State() {
        
    }
    
    public final void setFinal(final boolean isFinal) {
        this.isFinal = isFinal;
    }
    
    public final boolean isFinal() {
        return this.isFinal;
    }
    
    public final int getIndex() {
        return index;
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    public int addTransition(final int target, final int ch) {
        if (transitionsCount == 0) {
            trChar[0] = ch;
            trTarget[0] = target;
            transitionsCount = 1;
            return 0;
        }
        if (transitionsCount == trChar.length) {
            expandToSize(Math.min((int) Math.ceil(trChar.length * 1.5), 256));
        }
        int index;
        try {
            index = Arrays.binarySearch(trChar, 0, transitionsCount, ch);
        } catch (final Exception e) {
            index = 0;
        }
        if (index < 0) {
            transitionsCount++;
            index = -index - 1;
            for (int i = transitionsCount - 1; i > index; i--) {
                trChar[i] = trChar[i - 1];
                trTarget[i] = trTarget[i - 1];
            }
        }
        trChar[index] = ch;
        trTarget[index] = target;
        return index;
    }
    
    private void expandToSize(final int size) {
        trChar = Arrays.copyOf(trChar, size);
        trTarget = Arrays.copyOf(trTarget, size);
    }
    
    public int getTransitionTarget(final int ch) {
        final int index = Arrays.binarySearch(trChar, 0, transitionsCount, ch);
        if (index < 0) {
            return -1;
        } else {
            return trTarget[index];
        }
    }
    
    public boolean hasTransition(final char ch) {
        return getTransitionIndex(ch) >= 0;
    }
    
    public void clear() {
        trChar = new int[1];
        trTarget = new int[1];
        transitionsCount = 0;
        index = -1;
        isFinal = false;
    }
    
    public int getTransitionsCount() {
        return transitionsCount;
    }
    
    public int getTransitionAtIndex(final int trIndex) {
        return trChar[trIndex];
    }
    
    public int getTransitionIndex(final int ch) {
        return Arrays.binarySearch(trChar, 0, transitionsCount, ch);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof State) {
            return getIndex() == ((State) obj).getIndex();
        }
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return getIndex();
    }
}
