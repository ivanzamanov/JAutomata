package org.ivo.automata.dictionary;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.ivo.automata.AbstractDeterministicAutomaton;
import org.ivo.automata.State;
import org.ivo.automata.collections.StatesTable;
import org.ivo.automata.util.Measure;

/**
 * Constructs a recognizer for a list of words, which must be passed in alphabetical order.
 * 
 * @author ivo
 */
public class OrderedListRecogniser extends AbstractDeterministicAutomaton {
    
    public static final String DEFAULT_SEPARATOR = "\n";
    
    public OrderedListRecogniser(final List<String> wordsList) {
        this(wordsListToStringReader(wordsList), DEFAULT_SEPARATOR);
    }
    
    public OrderedListRecogniser(final Reader inputStream, final String wordSeparator) {
        final Measure m = new Measure();
        m.start();
        final Scanner scanner = new Scanner(inputStream);
        try {
            scanner.useDelimiter(Pattern.quote(wordSeparator));
            final OLRState startState = newState();
            setStartState(startState);
            final StatesTable minimizedStates = new StatesTable(0.05f);
            final List<OLRState> chainStates = new ArrayList<OLRState>();
            chainStates.add(startState);
            String lastWord = "";
            while (scanner.hasNext()) {
                final String currentWord = scanner.next();
                if (currentWord.compareTo(lastWord) < 0) {
                    throw new IllegalArgumentException("List not ordered, " + lastWord + " > " + currentWord);
                }
                
                // Traverse as much as possible.
                int i = 1;
                while (i <= currentWord.length() && i <= lastWord.length()
                        && getChar(currentWord, i) == getChar(lastWord, i)) {
                    i++;
                }
                
                for (int j = lastWord.length(); j >= i; j--) {
                    final OLRState unminimizedState = chainStates.get(j);
                    final State minimizedState = findMinimized(unminimizedState, minimizedStates);
                    chainStates.remove(j);
                    final OLRState prevState = chainStates.get(j - 1);
                    boolean removed = false;
                    // if (j == i) {
                    removed = minimizedStates.remove(prevState);
                    // }
                    prevState.addTransition(minimizedState.getIndex(), getChar(lastWord, j));
                    if (removed) {
                        minimizedStates.add(prevState);
                    }
                }
                
                OLRState currentState = null;
                for (int j = i; j <= currentWord.length(); j++) {
                    currentState = newState();
                    chainStates.add(currentState);
                    chainStates.get(j - 1).addTransition(currentState.getIndex(), getChar(currentWord, j));
                }
                currentState.setFinal(true);
                lastWord = currentWord;
            }
            // Finally, minimize the last chain.
            for (int j = lastWord.length(); j >= 1; j--) {
                final State minimizedState = findMinimized(chainStates.get(j), minimizedStates);
                final OLRState prevState = chainStates.get(j - 1);
                boolean removed = false;
                // if (j == 1) {
                removed = minimizedStates.remove(prevState);
                // }
                prevState.addTransition(minimizedState.getIndex(), getChar(lastWord, j));
                if (removed) {
                    minimizedStates.add(prevState);
                }
                chainStates.remove(chainStates.size() - 1);
            }
        } finally {
            scanner.close();
        }
    }
    
    private char getChar(final String currentWord, final int i) {
        return currentWord.charAt(i - 1);
    }
    
    private State findMinimized(final OLRState state, final StatesTable minimizedStates) {
        final OLRState equivalent = (OLRState) minimizedStates.addOrGet(state);
        if (equivalent != state) {
            deleteState(state);
        }
        return equivalent;
    }
    
    private static StringReader wordsListToStringReader(final List<String> wordsList) {
        final StringBuilder words = new StringBuilder();
        for (final String word : wordsList) {
            words.append(word);
            words.append(DEFAULT_SEPARATOR);
        }
        final StringReader reader = new StringReader(words.toString());
        return reader;
    }
    
    @Override
    protected OLRState newState() {
        OLRState state = new OLRState();
        state = (OLRState) addState(state);
        return state;
    }
}
