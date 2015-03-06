package test;

import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.ivo.automata.compressed.CompressedAutomaton;
import org.ivo.automata.dictionary.OrderedListRecogniser;
import org.ivo.automata.readers.ReaderWrapper;

public class CompressedAutomatonTest extends TestCase {

    public void test() {
        final List<String> wordsList = new LinkedList<String>();
        final int numTestWords = 10000;
        final int numDigits = (int) Math.log10(numTestWords);
        for (int i = 0; i < numTestWords; i++) {
            final String word = String.format("%0" + numDigits + "d", i);
            // final String word = Integer.toString(i);
            wordsList.add(word);
        }
        Collections.sort(wordsList);

        final OrderedListRecogniser uncompressed = new OrderedListRecogniser(wordsList);
        final CompressedAutomaton recognizer = new CompressedAutomaton(uncompressed);

        for (final String word : wordsList) {
            assertTrue("Assert recognized " + word, recognizer.traverse(new ReaderWrapper(new StringReader(word))));
        }

        for (int i = numTestWords; i < numTestWords * 2; i++) {
            final String word = Integer.toString(i);
            assertFalse("Assert not recognized " + word, recognizer.traverse(new ReaderWrapper(new StringReader(word))));
        }
    }
}
