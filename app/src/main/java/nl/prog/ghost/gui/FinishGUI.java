package nl.prog.ghost.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import java.util.List;
import java.util.Set;

import prog.nl.ghost.R;

/**
 * FinishGUI provides the last activity of the game,
 * which shows the winner and a list of high scores.
 * Also, it saves the new high score passed by GameGUI.
 * @author Joram Wessels; 10631542
 */
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

        // initializing graphical default
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.dutch) {
            SharedPreferences.Editor save = getSharedPreferences("nl.prog.ghost.save", Context.MODE_PRIVATE).edit();
            save.putString("lang", "dutch");
            save.commit();
            Intent restart = new Intent(FinishGUI.this, WelcomeGUI.class);
            startActivity(restart);

        } else if (id == R.id.english) {
            SharedPreferences.Editor save = getSharedPreferences("nl.prog.ghost.save", Context.MODE_PRIVATE).edit();
            save.putString("lang", "english");
            save.commit();
            Intent restart = new Intent(FinishGUI.this, WelcomeGUI.class);
            startActivity(restart);

        } else if (id == R.id.restart) {
            Intent restart = new Intent(FinishGUI.this, WelcomeGUI.class);
            startActivity(restart);

        } else if (id == R.id.clean) {
            SharedPreferences.Editor save = getSharedPreferences("nl.prog.ghost.save", Context.MODE_PRIVATE).edit();
            save.clear();
            save.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates shared preferences to include the
     * new high score and returns the new Set.
     * @param winName
     * @param turns
     * @return scoreList
     */
    private Set<String> updateHighScores(String winName, int turns) {
        SharedPreferences save = getSharedPreferences("nl.prog.ghost.save", Context.MODE_PRIVATE);
        Set<String> scoreList = new HashSet<String>(save.getStringSet("scores", new HashSet<String>()));

        String score = ""+turns;
        if (turns < 10) score = "0"+turns;
        scoreList.add(score + " - " + winName);
        SharedPreferences.Editor editScores = save.edit();
        editScores.putStringSet("scores", scoreList);
        editScores.commit();

        return scoreList;
    }

    /**
     * Updates the graphical display to show all ordered high scores.
     * @param scoreList
     */
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
}