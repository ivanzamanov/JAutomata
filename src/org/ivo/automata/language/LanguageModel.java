package org.ivo.automata.language;

import org.ivo.automata.AbstractDeterministicAutomaton;
import org.ivo.automata.SequenceReader;
import org.ivo.automata.State;

public class LanguageModel extends AbstractDeterministicAutomaton {
    
    private static final int $ = 0;
    private int n;
    
    public LanguageModel(int n) {
        this.n = n;
    }
    
    public void update(SequenceReader reader) {
        int letter = adjust(reader.read().toInt());
        int[] ngram = new int[n];
        int sequenceLength = 0;
        while(letter >= 0) {
            int count = 0;
            while(count < 3 && letter >= 0) {
                letter = reader.read().toInt();
                ngram[count] = letter;
            }
            if(count == 3)
                addNGram(ngram);
        }
    }

    private void addNGram(int[] ngram) {
        State state = getStartState();
        int i = 0;
        int nextState = state.getTransitionTarget(ngram[i]);
        while(i < ngram.length && nextState >= 0) {
            state = getStates()[nextState];
            nextState = state.getTransitionTarget(ngram[i]);
        }
    }

    private int adjust(int int1) {
        return int1 + 1;
    }

}
