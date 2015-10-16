package nl.prog.ghost.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

/**
 * The lexicon model class enables the game class to validate moves
 * according to their presence in the provided reader of the lexicon txt file.
 * @auathor Joram Wessels; 10631542
 */
public class Lexicon {

    private BufferedReader br;
    private HashSet<String> set;
    private HashSet<String> filtered;

    public Lexicon(BufferedReader reader) {
        this.br = reader;
        set = new HashSet<String>(10000, 1);
        try {
            String line;
            while ((line = br.readLine()) != null) {
                set.add(line);
            }
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * Initializes a lexicon for a new round.
     */
    public void init() {
        this.filtered = null;
    }

    /**
     * Filters the lexicon on the given condition.
     * @param filter
     */
    public void filter(String filter) {
        filtered = new HashSet<String>(1000, 1);
        Iterator<String> it = set.iterator();
        int size = set.size();
        for (int i=0; i < size; i++) {
            String next = it.next();
            if (next.startsWith(filter)) {
                filtered.add(next);
            }
        }
    }

    /**
     * Returns the size of the filtered HashSet.
     * @return size
     */
    public int count() {
        if (filtered != null) return filtered.size();
        return set.size();
    }

    /**
     * Returns last element left, or null if there are more
     * or less than 1 element left.
     * @return last string
     */
    public String result() {
        if (filtered.size() == 1) {
            Iterator<String> it = filtered.iterator();
            return it.next();
        } else {
            return null;
        }
    }

    /**
     * Checks the presence of the given word in the lexicon
     * @param word
     * @return exists
     */
    public boolean exists(String word) {
        if (filtered != null) return filtered.contains(word);
        return set.contains(word);
    }
}