package org.ivo.automata;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class AutomataSection extends AbstractDeterministicAutomaton {
    
    private static class Wrapper {
        public Wrapper(final State state, final int i) {
            this.state = state;
            this.trIndex = i;
        }
        
        private final State state;
        private int trIndex = -1;
    }
    
    public static void traverseSection(final PrintStream output, final IDeterministicAutomaton... automatons) {
        if (automatons.length == 0) {
            return;
        }
        final List<Deque<Wrapper>> stacks = new ArrayList<Deque<Wrapper>>(automatons.length);
        final List<Wrapper> wrappers = new ArrayList<Wrapper>(automatons.length);
        for (final IDeterministicAutomaton a : automatons) {
            final ArrayDeque<Wrapper> stack = new ArrayDeque<Wrapper>();
            stacks.add(stack);
            final Wrapper wrapper = new Wrapper(a.getStartState(), -1);
            wrappers.add(wrapper);
        }
        push(wrappers, stacks, output);
        
        final Deque<Wrapper> stack = stacks.get(0);
        while (!stack.isEmpty()) {
            peek(stacks, wrappers);
            if (haveCommonTransition(wrappers, automatons)) {
                push(wrappers, stacks, output);
            } else {
                final Wrapper peek = stack.peekLast();
                if (peek.state.getTransitionsCount() <= peek.trIndex) {
                    pop(stacks);
                }
            }
        }
    }
    
    private static boolean haveCommonTransition(final List<Wrapper> wrappers, final IDeterministicAutomaton[] automatons) {
        final Wrapper wrapper = wrappers.get(0);
        if (wrapper.state.getTransitionsCount() > wrapper.trIndex) {
            final int ch = wrapper.state.getTransitionAtIndex(wrapper.trIndex);
            for (int i = 0; i < wrappers.size(); i++) {
                final Wrapper current = wrappers.get(i);
                final IDeterministicAutomaton automaton = automatons[i];
                final State nextState = automaton.nextState(current.state, ch);
                if (nextState == null) {
                    wrappers.clear();
                    return false;
                }
                final Wrapper newWrapper = new Wrapper(nextState, -1);
                wrappers.set(i, newWrapper);
            }
            return true;
        }
        return false;
    }
    
    private static void peek(final List<Deque<Wrapper>> stacks, final List<Wrapper> wrappers) {
        wrappers.clear();
        for (int i = 0; i < stacks.size(); i++) {
            final Wrapper wrapper = stacks.get(i).peekLast();
            wrapper.trIndex++;
            wrappers.add(i, wrapper);
        }
    }
    
    private static void pop(final List<Deque<Wrapper>> stacks) {
        for (final Deque<Wrapper> stack : stacks) {
            stack.removeLast();
        }
    }
    
    private static void push(final List<Wrapper> wrappers, final List<Deque<Wrapper>> stacks, final PrintStream output) {
        boolean isFinal = true;
        for (int i = 0; i < wrappers.size(); i++) {
            final Wrapper wrapper = wrappers.get(i);
            stacks.get(i).addLast(wrapper);
            isFinal = isFinal && wrapper.state.isFinal();
        }
        if (isFinal) {
            printStack(stacks.get(0), output);
        }
    }
    
    private static void printStack(final Deque<Wrapper> stack, final PrintStream output) {
        if (stack.isEmpty()) {
            return;
        }
        final Iterator<Wrapper> iter = stack.iterator();
        final StringBuilder builder = new StringBuilder();
        while (iter.hasNext()) {
            final Wrapper wr = iter.next();
            if (wr.trIndex >= 0) {
                builder.append(wr.state.getTransitionAtIndex(wr.trIndex));
            }
        }
        if (builder.length() > 0) {
            output.println(builder.toString());
        }
    }
}
