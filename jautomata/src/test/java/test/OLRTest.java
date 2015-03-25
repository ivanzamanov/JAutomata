package test;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.ivo.automata.dictionary.OrderedListRecogniser;

public class OLRTest extends TestCase {

	public void testBenchmark() throws FileNotFoundException,
			UnsupportedEncodingException {
		new OrderedListRecogniser(new StringReader("word1 word2 word3 word4"),
				" ");

		// AutomataSection.traverseSection(System.out, orderedListRecogniser);
	}
}
