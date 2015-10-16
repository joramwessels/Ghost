package nl.prog.ghost.gui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import prog.nl.ghost.R;

public class FinishGUI extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_gui);

        // accepting bundle
        Bundle bundle = getIntent().getExtras();
        boolean winner = bundle.getBoolean("winner");
        String p1 = bundle.getString("p1");
        String p2 = bundle.getString("p2");
        int turns = bundle.getInt("turns");

        // show winning info
        TextView winView = (TextView) findViewById(R.id.winView);
        String winName = p1;
        if (!winner) winName = p2;
        winView.setText(winName + " has won the game!");
        Set<String> scoreList = updateHighScores(winName, turns);
        showHighScores(scoreList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_gui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Set<String> updateHighScores(String winName, int turns) {
        SharedPreferences save = getSharedPreferences("nl.prog.ghost.save", Context.MODE_PRIVATE);
        Set<String> scoreList = save.getStringSet("scores", new HashSet<String>());

        System.out.println("get scoreList:");
        for (String entry : scoreList) {
            System.out.println(entry);
        }

        scoreList.add(turns + " - " + winName);
        SharedPreferences.Editor editScores = save.edit();
        editScores.putStringSet("scores", scoreList);
        editScores.commit();

        System.out.println("put scoreList:");
        for (String entry : scoreList) {
            System.out.println(entry);
        }

        return scoreList;
    }

    private void showHighScores(Set<String> scoreList) {
        ListView view = (ListView) findViewById(R.id.highScores);
        List<String> orderedList = new ArrayList<String>();
        for (String entry : scoreList) {
            orderedList.add(entry);
        }
        Collections.sort(orderedList, null);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, orderedList);
        view.setAdapter(adapter);
    }

    /**
     * Sorts high score entries according to their score.
     * @param set
     * @return ordered
     */
    private String[] sort(Set<String> set) {
        int size = set.size();
        int best = 10000;
        int newSize = size;
        String bestWord = null;
        String word;
        String[] ordered = new String[size];
        for (int i=0; i>size; i++) {
            Iterator<String> it = set.iterator();
            for (int j=0; j < newSize; j++) {
                word = it.next();
                int next = Integer.parseInt(word.split(" - ")[0]);
                if (next < best) {
                    best = next;
                    bestWord = word;
                }
            }
            set.remove(bestWord);
            ordered[i] = bestWord;
            newSize--;
        }
        return ordered;
    }
}
