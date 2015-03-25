package org.ivo.automata.text;

import java.io.IOException;
import java.io.Reader;

import org.ivo.automata.AbstractDeterministicAutomaton;

public class SubwordRecogniser extends AbstractDeterministicAutomaton {
    
    private final SRState source;
    
    public SubwordRecogniser(final Reader textReader) throws IOException {
        SRState currentSink = newState();
        source = currentSink;
        setStartState(source);
        int read = 0;
        while (read >= 0) {
            read = textReader.read();
            final char ch = (char) read;
            currentSink = update(currentSink, ch);
        }
    }
    
    private SRState update(final SRState currentSink, final char ch) {
        final SRState newSink = newState();
        // This is a primary transition until proven otherwise.
        currentSink.addTransition(newSink.getIndex(), ch);
        currentSink.setPrimary(ch, true);
        SRState currentState = currentSink;
        SRState suffixState = null;
        while (currentState != source && suffixState == null) {
            currentState = currentState.getSuffixLink();
            if (!currentState.hasTransition(ch)) {
                // No transition means this is a transition from one subword of a representative to
                // another.
                currentState.addTransition(newSink.getIndex(), ch);
                currentState.setPrimary(ch, false);
            } else if (currentState.isPrimary(ch)) {
                // We already have this transition, i.e. there is already a transition between these
                // two representatives.
                suffixState = (SRState) nextState(currentState, ch);
            } else {
                // Means we should branch here, i.e. a new representative is found.
                final SRState childState = (SRState) nextState(currentState, ch);
                suffixState = split(currentState, childState, ch);
            }
        }
        if (suffixState == null) {
            suffixState = source;
        }
        newSink.setSuffixLink(suffixState);
        return newSink;
    }
    
    private SRState split(final SRState parentState, final SRState childState, final char ch) {
        // The state for the newly found representative.
        final SRState newChildState = newState();
        parentState.addTransition(newChildState.getIndex(), ch);
        parentState.setPrimary(ch, true);
        for (int i = 0; i < childState.getTransitionsCount(); i++) {
            final int currentCh = childState.getTransitionAtIndex(i);
            newChildState.addTransition(nextState(childState, currentCh).getIndex(), currentCh);
            newChildState.setPrimary(currentCh, false);
        }
        
        newChildState.setSuffixLink(childState.getSuffixLink());
        childState.setSuffixLink(newChildState);
        SRState currentState = parentState;
        // All suffixes of the new representative must have a transition to here.
        while (currentState != getStartState()) {
            currentState = currentState.getSuffixLink();
            if (currentState.hasTransition(ch) && !currentState.isPrimary(ch)) {
                currentState.addTransition(newChildState.getIndex(), ch);
                currentState.setPrimary(ch, false);
            } else {
                break;
            }
        }
        return newChildState;
    }
    
    @Override
    protected SRState newState() {
        final SRState result = new SRState();
        result.setFinal(true);
        addState(result);
        return result;
    }
}
