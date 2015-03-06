package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.ivo.automata.dictionary.OrderedListRecogniser;

public class OLRTest extends TestCase {

    public void testBenchmark() throws FileNotFoundException, UnsupportedEncodingException {
        final File inputFile = new File("data", "titles.txt");
        final InputStream inputStream = new FileInputStream(inputFile);
        final Reader reader = new InputStreamReader(inputStream);
        new OrderedListRecogniser(reader, "\n");

        // AutomataSection.traverseSection(System.out, orderedListRecogniser);
    }
}
