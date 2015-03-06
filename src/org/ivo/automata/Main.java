package org.ivo.automata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.ivo.automata.dictionary.OrderedListRecogniser;

public class Main {

    /**
     * @param args
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    public static void main(final String[] args) {
        try {
            final File inputFile = new File("data", "titles.txt");
            final InputStream inputStream = new FileInputStream(inputFile);
            final Reader reader = new InputStreamReader(inputStream, "cp1251");
            new OrderedListRecogniser(reader, "\n");
        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }
}
