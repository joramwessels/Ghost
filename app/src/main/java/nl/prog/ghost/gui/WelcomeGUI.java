package nl.prog.ghost.gui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import nl.prog.ghost.backend.Game;
import prog.nl.ghost.R;

public class WelcomeGUI extends Activity {

    String lang;
    Set<String> oldPlayers = new HashSet<>();
    SharedPreferences save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome_gui);

        TextView G = (TextView) findViewById(R.id.G);
        TextView H = (TextView) findViewById(R.id.H);
        TextView O = (TextView) findViewById(R.id.O);
        TextView S = (TextView) findViewById(R.id.S);
        TextView T = (TextView) findViewById(R.id.T);

        save = this.getSharedPreferences("prefs", 0);
        lang = save.getString("lang", "dutch");

        // Recovering old player names
        oldPlayers = save.getStringSet("oldPlayers", oldPlayers);
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
        }});
    }

    /**
     * Navigates to the next activity after bundling the required data.
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

            String p1 = p1input.getText().toString().replaceAll("- ","-").replaceAll(" -","-");
            String p2 = p2input.getText().toString().replaceAll("- ", "-").replaceAll(" -","-");

            // Storing new names
            if (!oldPlayers.contains(p1 + " - " + p2)) {
                SharedPreferences.Editor editor = save.edit();
                oldPlayers.add(p1 + " - " + p2);
                editor.putStringSet("oldPlayers",oldPlayers);
                editor.commit();
            }

            toGame(p1, p2);
        }
    }
}
