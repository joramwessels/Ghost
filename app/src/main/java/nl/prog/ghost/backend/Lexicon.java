package nl.prog.ghost.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;

/**
 * The lexicon model class enables the game class to validate moves
 * according to their presence in the provided lexicon txt file.
 * Created by Joram Wessels on 28-Sep-15.
 */
public class Lexicon {

    private String path;
    private HashSet<String> set;
    private HashSet<String> filtered;

    public Lexicon(String path) {
        this.path = path;
    }

    /**
     * Initializes a lexicon for a new game.
     */
    public void init() {
        set = new HashSet<String>(10000, 1);
        try {
            //InputStream stream = ClassLoader.getSystemResourceAsStream(path);
            //BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            System.out.println("exists " + new File(path).exists());
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                set.add(line);
            }
            System.out.println(set.size());
        } catch (IOException i) {
            i.printStackTrace();
        }
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