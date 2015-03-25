package test;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.ivo.automata.dictionary.OrderedListRecogniser;

public class OrderedListRecogniserTest extends TestCase {
    
    public void test() throws FileNotFoundException {
        for (int i = 5; i < 10000; i++) {
            doTest(i);
        }
        doTest(10000);
    }
    
    private void doTest(final int numTestWords) throws FileNotFoundException {
        final StringBuilder words = new StringBuilder();
        final List<String> wordsList = new LinkedList<String>();
        final int numDigits = (int) Math.ceil(Math.log10(numTestWords));
        for (int i = 0; i < numTestWords; i++) {
            final String word = String.format("%0" + numDigits + "d", i);
            wordsList.add(word);
        }
        Collections.sort(wordsList);
        for (final String word : wordsList) {
            words.append(word);
            words.append('\n');
        }
        final StringReader reader = new StringReader(words.toString());
        final OrderedListRecogniser recognizer = new OrderedListRecogniser(reader, "\n");
        
        for (final String word : wordsList) {
            assertTrue("Assert recognized " + word, recognizer.traverse(new StringReader(word)));
        }
        
        for (int i = numTestWords; i < numTestWords * 2; i++) {
            final String word = Integer.toString(i);
            final boolean recognized = recognizer.traverse(new StringReader(word));
            assertFalse("Assert not recognized " + word + " from " + i, recognized);
        }
    }
    
    public void testEdge() {
        final StringBuilder words = new StringBuilder();
        final List<String> wordsList = new LinkedList<String>();
        wordsList.add("0");
        // wordsList.add("1");
        wordsList.add("10");
        wordsList.add("100");
        if (wordsList.size() > 0) {
            return;
        }
        
        Collections.sort(wordsList);
        
        for (final String word : wordsList) {
            words.append(word);
            words.append('\n');
        }
        
        final StringReader reader = new StringReader(words.toString());
        
        final OrderedListRecogniser recognizer = new OrderedListRecogniser(reader, "\n");
        
        for (final String word : wordsList) {
            assertTrue("Assert recognized " + word, recognizer.traverse(new StringReader(word)));
        }
        
        String word = "00";
        assertFalse("Assert not recognized " + word, recognizer.traverse(new StringReader(word)));
        word = "11";
        assertFalse("Assert not recognized " + word, recognizer.traverse(new StringReader(word)));
        word = "110";
        assertFalse("Assert not recognized " + word, recognizer.traverse(new StringReader(word)));
        word = "1000";
        assertFalse("Assert not recognized " + word, recognizer.traverse(new StringReader(word)));
    }
}
