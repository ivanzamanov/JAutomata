package org.ivo.automata.letters;

import org.ivo.automata.Letter;

public class IntLetter implements Letter {

    private int value;

    public IntLetter(int value) {
        this.value = value;
    }

    @Override
    public int toInt() {
        return value;
    }

}
