package org.ivo.automata.text;

import java.util.HashMap;

import org.ivo.automata.State;

public class SRState extends State {
    
    private final HashMap<Integer, Boolean> primaries = new HashMap<Integer, Boolean>();
    
    private SRState suffixLink;
    
    public SRState getSuffixLink() {
        return this.suffixLink;
    }
    
    void setSuffixLink(final SRState suffixLink) {
        this.suffixLink = suffixLink;
    }
    
    public void setPrimary(final int ch, final boolean flag) {
        primaries.put(ch, flag);
    }
    
    public boolean isPrimary(final int ch) {
        final Boolean bool = primaries.get(ch);
        return bool != null && bool.booleanValue();
    }
    
    @Override
    public void clear() {
        super.clear();
        primaries.clear();
    }
}
