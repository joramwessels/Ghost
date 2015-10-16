package nl.prog.ghost.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class WelcomeGUI extends Activity {

    String lang;

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

        SharedPreferences save = this.getSharedPreferences("nl.prog.ghost.save", Context.MODE_PRIVATE);
        lang = save.getString("lang", "dutch");
        Set<String> oldPlayers = save.getStringSet("oldPlayers", new HashSet<String>());
        System.out.println("get oldPlayers:");
        for (String entry : oldPlayers) {
            System.out.println(entry);
        }
        updatePlayerNames(oldPlayers);
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
            }});
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
            if (true/*!oldPlayers.contains(p1 + " - " + p2)*/) {
                SharedPreferences save = getSharedPreferences("nl.prog.ghost.save", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = save.edit();
                Set<String> oldPlayers = save.getStringSet("oldPlayers", new HashSet<String>());
                oldPlayers.add(p1 + " - " + p2);
                //HashSet<String> players = new HashSet<String>();
                //for (String entry : oldPlayers) {
                editor.putStringSet("oldPlayers", oldPlayers);
                editor.commit();

                System.out.println("put oldPlayers:");
                for (String entry : oldPlayers) {
                    System.out.println(entry);
                }


                SharedPreferences prefs = getSharedPreferences("nl.prog.ghost.save", Context.MODE_PRIVATE);
                Set<String> players = prefs.getStringSet("oldPlayers", new HashSet<String>());
                System.out.println("check:");
                for (String entry : players) {
                    System.out.println(entry);
                }
            }

            toGame(p1, p2);
        }
    }
}
