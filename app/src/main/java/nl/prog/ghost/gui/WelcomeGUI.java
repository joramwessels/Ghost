package nl.prog.ghost.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import prog.nl.ghost.R;

/**
 * WelcomeGUI is the main activity. It asks for player names
 * to start a game, and provides a list of familiar names.
 * If two new names are entered, they are saved along with the other familiar names.
 * @author Joram Wessels; 10631542
 */
public class WelcomeGUI extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome_gui);

        // TODO: Ghost letter animation
        TextView G = (TextView) findViewById(R.id.G);
        TextView H = (TextView) findViewById(R.id.H);
        TextView O = (TextView) findViewById(R.id.O);
        TextView S = (TextView) findViewById(R.id.S);
        TextView T = (TextView) findViewById(R.id.T);

        // Collecting saved data
        SharedPreferences save = this.getSharedPreferences("nl.prog.ghost.save", Context.MODE_PRIVATE);
        Set<String> oldPlayers = new HashSet<String>(save.getStringSet("oldPlayers", new HashSet<String>()));
        String lang = save.getString("lang", "dutch");

        updatePlayerNames(oldPlayers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            Intent restart = new Intent(WelcomeGUI.this, WelcomeGUI.class);
            startActivity(restart);

        } else if (id == R.id.english) {
            SharedPreferences.Editor save = getSharedPreferences("nl.prog.ghost.save", Context.MODE_PRIVATE).edit();
            save.putString("lang", "english");
            save.commit();
            Intent restart = new Intent(WelcomeGUI.this, WelcomeGUI.class);
            startActivity(restart);

        } else if (id == R.id.restart) {
            Intent restart = new Intent(WelcomeGUI.this, WelcomeGUI.class);
            startActivity(restart);

        } else if (id == R.id.clean) {
            SharedPreferences.Editor save = getSharedPreferences("nl.prog.ghost.save", Context.MODE_PRIVATE).edit();
            save.clear();
            save.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Navigates to the GameGUI activity after bundling the required data.
     * @param p1
     * @param p2
     */
    public void toGame(String p1, String p2) {
        Intent nextAct = new Intent(WelcomeGUI.this, GameGUI.class);
        Bundle bundle = new Bundle();
        bundle.putString("p1",p1);
        bundle.putString("p2",p2);
        nextAct.putExtras(bundle);
        startActivity(nextAct);
    }

    /**
     * Updates the graphical display of the names of previous games.
     * @param oldPlayers
     */
    private void updatePlayerNames(Set<String> oldPlayers) {
        ListView list = (ListView) findViewById(R.id.oldNames);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, oldPlayers.toArray());
        list.setAdapter(adapter);
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView list = (ListView) findViewById(R.id.oldNames);
                String players = (String) list.getItemAtPosition(position);
                String[] names = players.split(" - ");
                String player1 = names[0];
                String player2 = names[1];
                toGame(player1, player2);
            }
        });
    }

    /**
     * Called when Play button is clicked.
     * Parses the input, saves new names and calls toGame()
     * @param view
     */
    public void onSubmit(View view) {
        EditText p1input = (EditText) findViewById(R.id.player1);
        EditText p2input = (EditText) findViewById(R.id.player2);

        // Parsing player names
        if (p1input.getText().toString() != null &&
            p1input.getText().toString().replaceAll("\\s","").length() > 0 &&
            p2input.getText().toString() != null &&
            p2input.getText().toString().replaceAll("\\s","").length() > 0) {

            // To disable parsing errors, all " - " are replaced
            String p1 = p1input.getText().toString().replaceAll("- ","-").replaceAll(" -","-");
            String p2 = p2input.getText().toString().replaceAll("- ", "-").replaceAll(" -","-");

            // Storing new names
            SharedPreferences save = getSharedPreferences("nl.prog.ghost.save", Context.MODE_PRIVATE);
            Set<String> oldPlayers = new HashSet<String>(save.getStringSet("oldPlayers", new HashSet<String>()));
            oldPlayers.add(p1 + " - " + p2);
            SharedPreferences.Editor editor = save.edit();
            editor.putStringSet("oldPlayers", oldPlayers);
            editor.commit();

            toGame(p1, p2);
        }
    }
}
