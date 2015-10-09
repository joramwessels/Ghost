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

import org.w3c.dom.Text;

import nl.prog.ghost.backend.Game;
import prog.nl.ghost.R;

public class GameGUI extends Activity {

    int RED = 0xFFB00000;
    int BLACK = 0xFF101010;
    int WHITE = 0xFFD6D6D6;

    Game game;
    String p1;
    String p2;
    TextView p1view;
    TextView p2view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_gui);

        // Creating Game instance
        Bundle bundle = getIntent().getExtras();
        p1 = bundle.getString("p1");
        p2 = bundle.getString("p2");
        game = new Game(p1, p2);
        game.init();
        if (game.lex != null) {
            displayMessage("" + game.lex.count(), true);
        }

        TextView p1view = (TextView) findViewById(R.id.nameP1);
        TextView p2view = (TextView) findViewById(R.id.nameP2);
        p1view.setText(p1);
        p2view.setText(p2);
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

        if (message.contains("was added to")) {
            displayMessage(message, false);
            updateWord();
            switchTurn();
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
        //view.setVisibility(View.INVISIBLE);
    }

    /**
     * Changes the graphical interface to indicate whose turn it is.
     */
    private void switchTurn() {
        if (game.turn()) {
            p1view.setTextColor(BLACK);
            p2view.setTextColor(WHITE);
        } else {
            p2view.setTextColor(BLACK);
            p1view.setTextColor(WHITE);
        }
    }

    /**
     * Updates the graphical display of the current word status.
     */
    private void updateWord() {
        TextView view = (TextView) findViewById(R.id.word);
        view.setText(game.getWord());
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
        if (game.getLivesP1() == 0 || game.getLivesP1() == 0) {
            Intent toFinish = new Intent(GameGUI.this, FinishGUI.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("winner", game.winner());
            bundle.putString("p1",p1);
            bundle.putString("p2",p2);
            toFinish.putExtras(bundle);
            startActivity(toFinish);
        } else {
            game.init();
        }
    }
}
