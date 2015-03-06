package org.ivo.automata.readers;

import java.io.IOException;
import java.io.Reader;

import org.ivo.automata.Letter;
import org.ivo.automata.SequenceReader;
import org.ivo.automata.letters.IntLetter;

public class ReaderWrapper implements SequenceReader {

    private Reader delegate;

    public ReaderWrapper(Reader reader) {
        this.delegate = reader;
    }

    @Override
    public Letter read() {
        try {
            return new IntLetter(delegate.read());
        } catch (IOException e) {
            return new IntLetter(-1);
        }
    }

}
