package nl.prog.ghost.backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * The Game class provides the backend game functionality
 * for the game of Ghost. Provided with two player names and
 * a BufferedReader that references a lexicon file
 * @author Joram Wessels; 10631542
 */
public class Game {

    private String p1;
    private String p2;
    private String word;
    private boolean turn;   // false when p1, true when p2
    private boolean ended;
    private boolean winner; // false when p1, true when p2
    private Lexicon lex;
    private int livesP1 = 3;
    private int livesP2 = 3;
    private BufferedReader br;

    public Game(String player1, String player2, BufferedReader reader) {
        this.p1 = player1;
        this.p2 = player2;
        this.br = reader;
        lex = new Lexicon(br);
    }

/******************************* Main *******************************/
    /**
     * Main function to enable play in terminal.
     * @param args
     */
     public static void main(String[] args) {
         Scanner scanner = new Scanner(System.in);
         BufferedReader reader;
         System.out.println("Player1:");
         String player1 = scanner.next();
         System.out.println("Player2:");
         String player2 = scanner.next();
         System.out.println("Lexicon file:)");
         do {
             try {
                 String lang = scanner.next();
                 reader = new BufferedReader(new FileReader(lang));
                 break;
             } catch (IOException i) {
                 System.out.println("File not found. Lexicon file:");
             }
         } while (true);
         Game game = new Game(player1, player2, reader);
         String message;

        do {
            game.init();
            do {
                if (game.turn) System.out.println("Enter move for " + player2 + ":");
                else System.out.println("Enter move for " + player1 + ":");

                message = game.guess(scanner.next());
                System.out.println(message);
            } while (!game.ended());

            // Changing rounds
            System.out.println( game.p1 +": "+ game.livesP1 +"\n"+
                    game.p2 +": "+ game.livesP2 +"\n Next Round!\n");

        } while (game.getLivesP1() != 0 && game.getLivesP2() != 0);
        if (game.getLivesP2() == 0) {
            System.out.println(game.p1 + " has won the game!");
            game.winner = false;
        } else {
            System.out.println(game.p2 + " has won the game!");
            game.winner = true;
        }
    }

/***************************** Object **************************************/
    /**
     * Initializes Game.class for a new playthrough.
     * init() should only be called when a new round is required,
     * as it does not initialize a complete game.
     */
    public void init() {
        this.word = "";
        this.ended = false;
        lex.init();
    }

    /**
     * Performs a move for the player who's turn it is.
     * It takes care of:
     * 1) syntaxical validity,
     * 2) conformity of the rules,
     * 3) updating the word status,
     * 4) changing turns
     * 5) Ending rounds & updating lives
     * @param moveRaw
     * @return status message
     */
    public String guess(String moveRaw) {
        String move = moveRaw.toLowerCase();
        String message = checkInput(move);
        if (!message.equals("OK")) {
            return message;
        }
        message = validateMove(move);
        if (!message.equals("OK")) {
            return message;
        }
        String oldWord = word;
        word = word + move;
        turn = !turn;
        return move+" was added to "+oldWord+" to form "+word;
    }

    /**
     * Checks if the input is syntaxically valid.
     * Returns OK if so, returns problem message otherwise.
     * @param move
     * @return status message
     */
    private String checkInput(String move) {
        if (move != null && move.length() > 0) {
            move = move.toLowerCase();
            if (move.length() > 1) {
                return "You can only enter one character";
            }
            char ch = move.toCharArray()[0];
            if (ch < 97 || ch > 122) {
                return "You can only enter letters";
            } else {
                return "OK";
            }
        }
        return "Enter a move";
    }

    /**
     * Checks if the input is valid according to Game rules:
     * 1) The new fragment ought not be an existing word longer than 3 letters,
     * 2) The new fragment ought to be the beginning of an existing word.
     * @param move
     * @return status message
     */
    private String validateMove(String move) {
        String newWord = this.word + move;
        lex.filter(newWord);
        int hits = lex.count();

        // losing condition 2
        if (hits == 0) {
            String won = winner(!turn);
            return newWord + " cannot form an existing word. " + won + " has won this round!";
        // losing condition 1
        } else if (newWord.length() > 3 && lex.exists(newWord)) {
            String won = winner(!turn);
            return newWord + " is an existing word. " + won + " has won this round!";
        // continuing the game
        } else {
            return "OK";
        }
    }

    /**
     * Processes a winning move, ending the game,
     * setting a winner and sustracting lives accordingly.
     * Returns the name of the winner.
     * @param player
     * @return winner
     */
    private String winner(boolean player) {
        String won = p1;
        if (player) won = p2;
        ended = true;
        if (player) livesP1--;
        else livesP2--;
        return won;
    }


/******************** Getters & Setters *******************************/

    /**
     * @return p1 name
     */
    public String getP1() {
        return p1;
    }

    /**
     * @return p2 name
     */
    public String getP2() {
        return p2;
    }

    /**
     * @return lives
     */
    public int getLivesP1() {
        return livesP1;
    }

    /**
     * @return lives
     */
    public int getLivesP2() {
        return livesP2;
    }

    /**
     * @return word
     */
    public String getWord() {
        return word;
    }

    /**
     * False when p1, True when p2.
     * @return turn
     */
    public boolean turn() {
        return turn;
    }

    /**
     * False when p1, true when p2
     * @return winner
     */
    public boolean winner() {
        return this.winner;
    }

    /**
     * Indicates if a round has ended
     * @return hasEnded
     */
    public boolean ended() {
        return this.ended;
    }

    /**
     * @param p1
     */
    public void setP1(String p1) {
        this.p1 = p1;
    }

    /**
     * @param p2
     */
    public void setP2(String p2) {
        this.p2 = p2;
    }
}