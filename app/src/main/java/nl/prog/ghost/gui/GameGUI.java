package nl.prog.ghost.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nl.prog.ghost.backend.Game;
import prog.nl.ghost.R;

public class GameGUI extends Activity {

    private static final int RED = 0xFFB00000;
    private static final int BLACK = 0xFF101010;
    private static final int WHITE = 0xFFD6D6D6;

    Game game;
    String p1;
    String p2;
    TextView p1view;
    TextView p2view;
    int turnCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_gui);

        // Creating Game instance
        try {
            InputStream iS = getApplicationContext().getAssets().open("dutch.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(iS));

            Bundle bundle = getIntent().getExtras();
            p1 = bundle.getString("p1");
            p2 = bundle.getString("p2");
            game = new Game(p1, p2, reader);
            long start = System.currentTimeMillis();
            game.init();
            System.out.println("Game initialized in " + (System.currentTimeMillis() - start) + "ms");


        } catch (IOException i) {
            i.printStackTrace();
            System.exit(1);
        }

        p1view = (TextView) findViewById(R.id.nameP1);
        p2view = (TextView) findViewById(R.id.nameP2);
        p1view.setText(p1);
        p2view.setText(p2);
        if (game.turn()) p2view.setTextColor(WHITE);
        else p1view.setTextColor(WHITE);
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
        if (id == R.id.restart) {
            Intent restart = new Intent(GameGUI.this, WelcomeGUI.class);
            startActivity(restart);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when clicked on Enter button.
     * Queries the Game instance for a response message
     * and changes the graphical dislay accordingly.
     * @param view
     */
    public void move(View view) {
        EditText input = (EditText) findViewById(R.id.letterAdd);
        String move = input.getText().toString();
        displayMessage(move, false);
        String message = game.guess(move);
        System.out.println(message);
        System.out.println("word: " + game.getWord());

        if (message.contains("was added to") || message.contains("won this round!")) {
            displayMessage(message, false);
            updateWord();
            updateTurn();
            turnCount += 2;
        } else {
            displayMessage(message, true);
        }

        if (game.ended()) {
            updateLives();
        }
    }

    /**
     * Displays a message on the graphical interface.
     * Error decides whether the message is red or black.
     * @param message
     * @param error
     */
    private void displayMessage(String message, boolean error) {
        TextView view = (TextView) findViewById(R.id.message);
        view.setText(message);
        if (error) {
            view.setTextColor(RED);
        } else {
            view.setTextColor(BLACK);
        }
        view.setVisibility(View.VISIBLE);
    }

    /**
     * Changes the graphical interface to indicate whose turn it is.
     */
    private void updateTurn() {
        if (game.turn()) {
            p1view.setTextColor(BLACK);
            p2view.setTextColor(WHITE);
        } else {
            p2view.setTextColor(BLACK);
            p1view.setTextColor(WHITE);
        }
    }

    /**
     * Updates the graphical display of the current word status,
     * and cleans the content of the letter adding EditText.
     */
    private void updateWord() {
        TextView view = (TextView) findViewById(R.id.word);
        view.setText(game.getWord().toUpperCase());
        EditText text = (EditText) findViewById(R.id.letterAdd);
        text.setText("");
    }

    /**
     * Updates the graphical interface to show the new amount of lives,
     * and navigates to the finish activity when a player has lost.
     */
    private void updateLives() {
        RatingBar barP1 = (RatingBar) findViewById(R.id.ratingP1);
        RatingBar barP2 = (RatingBar) findViewById(R.id.ratingP2);
        barP1.setRating(game.getLivesP1());
        barP2.setRating(game.getLivesP2());
        if (game.getLivesP1() == 0 || game.getLivesP2() == 0) {
            toFinish();
        } else {
            long start = System.currentTimeMillis();
            game.init();
            System.out.println("Game initialized in " + (System.currentTimeMillis() - start) + "ms");
        }
    }

    private void toFinish() {
        Intent toFinish = new Intent(GameGUI.this, FinishGUI.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("winner", game.winner());
        bundle.putString("p1",p1);
        bundle.putString("p2",p2);
        bundle.putInt("turns", turnCount/2+1);
        toFinish.putExtras(bundle);
        startActivity(toFinish);
    }
}
