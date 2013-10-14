package org.ivo.automata.dictionary;

import org.ivo.automata.State;

public class OLRState extends State {
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof OLRState) {
            final OLRState state2 = (OLRState) obj;
            if (this.getIndex() == state2.getIndex() || this.isFinal() != state2.isFinal()
                    || this.getTransitionsCount() != state2.getTransitionsCount()) {
                return false;
            }
            for (int i = 0; i < getTransitionsCount(); i++) {
                if (trChar[i] != state2.trChar[i] || trTarget[i] != state2.trTarget[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return super.equals(obj);
        }
    }
    
    @Override
    public int hashCode() {
        return fnvHash();
    }
    
    @SuppressWarnings("unused")
    private int defaultHash() {
        int mul = 0;
        if (isFinal()) {
            mul = 1;
        }
        
        int sum = 0;
        for (int i = 0; i < getTransitionsCount(); i++) {
            sum += trChar[i] * trTarget[i];
        }
        return (Integer.MAX_VALUE >> 1) * mul + sum;
    }
    
    private int fnvHash() {
        long h = 2166136261l;
        for (int i = 0; i < getTransitionsCount(); i++) {
            h = h * 16777619 ^ trChar[i] ^ trTarget[i];
        }
        return (int) h & Integer.MAX_VALUE;
    }
    
    @SuppressWarnings("unused")
    private int otatHash() {
        int h = 0;
        int i = 0;
        for (i = 0; i < getTransitionsCount(); i++) {
            h += trChar[i];
            h += h << 10;
            h ^= h >> 6;
            
            h += trTarget[i];
            h += h << 10;
            h ^= h >> 6;
        }
        
        h += h << 3;
        h ^= h >> 11;
        h += h << 15;
        return h;
    }
}
