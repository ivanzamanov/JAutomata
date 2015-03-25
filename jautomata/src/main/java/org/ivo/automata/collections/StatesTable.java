package org.ivo.automata.collections;

import org.ivo.automata.State;

public class StatesTable {
    
    private static class Entry {
        private State state;
        private Entry next = null;
        private final int hash;
        
        public Entry(final State state, final int hash) {
            this.state = state;
            this.hash = hash;
        }
    }
    
    private final float loadFactor;
    private Entry[] table;
    private int threshold;
    private int size = 0;
    
    public StatesTable(final float loadFactor) {
        this.loadFactor = loadFactor;
        threshold = (int) (16 * loadFactor);
        this.table = new Entry[16];
    }
    
    public void add(final State state) {
        final int hash = state.hashCode();
        final int tableIndex = getTableIndex(hash, table);
        Entry entry = table[tableIndex];
        if (entry == null) {
            entry = new Entry(state, hash);
            table[tableIndex] = entry;
            return;
        }
        int count = 1;
        if (isEqual(state, hash, entry)) {
            entry.state = state;
            return;
        }
        while (entry.next != null) {
            if (isEqual(state, hash, entry)) {
                entry.state = state;
                return;
            }
            entry = entry.next;
            count++;
        }
        entry.next = new Entry(state, hash);
        size++;
        if (count > threshold) {
            rehash();
        }
    }
    
    private int getTableIndex(final int hash, final Entry[] table) {
        final int tableIndex;
        tableIndex = hash & table.length - 1;
        return tableIndex;
    }
    
    public int size() {
        return size;
    }
    
    private void rehash() {
        final int newTableLength = table.length * 2;
        final Entry[] newTable = new Entry[newTableLength];
        threshold = (int) (newTableLength * loadFactor);
        for (int i = 0; i < table.length; i++) {
            Entry entry = table[i];
            while (entry != null) {
                addToTable(newTable, entry);
                entry = entry.next;
            }
            table[i] = null;
        }
        table = newTable;
    }
    
    private void addToTable(final Entry[] table, final Entry entry) {
        final int tableIndex = getTableIndex(entry.hash, table);
        entry.next = table[tableIndex];
        table[tableIndex] = entry;
    }
    
    public boolean remove(final State state) {
        final int hash = state.hashCode();
        final int tableIndex = getTableIndex(hash, table);
        Entry entry = table[tableIndex];
        if (entry == null) {
            return false;
        } else {
            if (isEqual(state, hash, entry)) {
                table[tableIndex] = entry.next;
                size--;
                return true;
            }
            Entry previous = entry;
            entry = entry.next;
            while (entry != null) {
                if (isEqual(state, hash, entry)) {
                    previous.next = entry.next;
                    size--;
                    return true;
                }
                previous = entry;
                entry = entry.next;
            }
            return false;
        }
    }
    
    public State addOrGet(final State state) {
        final int hash = state.hashCode();
        final int tableIndex = getTableIndex(hash, table);
        Entry entry = table[tableIndex];
        if (entry == null) {
            entry = new Entry(state, hash);
            table[tableIndex] = entry;
            size++;
            return state;
        } else {
            if (isEqual(state, hash, entry)) {
                return entry.state;
            }
            int count = 1;
            while (entry.next != null) {
                if (isEqual(state, hash, entry.next)) {
                    return entry.next.state;
                }
                entry = entry.next;
                count++;
            }
            entry.next = new Entry(state, hash);
            size++;
            if (count > threshold) {
                rehash();
            }
            return state;
        }
    }
    
    public State get(final State state) {
        final int hash = state.hashCode();
        final int tableIndex = getTableIndex(hash, table);
        Entry entry = table[tableIndex];
        if (entry == null) {
            return null;
        } else {
            while (entry != null) {
                if (isEqual(state, hash, entry)) {
                    return entry.state;
                }
                entry = entry.next;
            }
            return null;
        }
    }
    
    private boolean isEqual(final State state, final int hash, final Entry entry) {
        return entry.hash == hash && entry.state.equals(state);
        // return entry.state.equals(state);
    }
    
    public void printDistribution() {
        for (int i = 0; i < table.length; i++) {
            Entry entry = table[i];
            int size = 0;
            while (entry != null) {
                size++;
                entry = entry.next;
            }
            System.out.println(size);
        }
    }
}
