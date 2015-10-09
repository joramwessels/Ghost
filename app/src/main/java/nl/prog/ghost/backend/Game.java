package nl.prog.ghost.backend;

import java.util.Scanner;

/**
 * @author Joram Wessels; 10631542
 */
public class Game {

    private String p1;
    private String p2;
    private String word;
    private String language = "dutch";
    private boolean turn;   // false when p1, true when p2
    private boolean ended;
    private boolean winner; // false when p1, true when p2
    public Lexicon lex;
    private int livesP1 = 3;
    private int livesP2 = 3;

    /**
     * Main function to play in terminal.
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Player1:");
        String player1 = scanner.next();
        System.out.println("Player2:");
        String player2 = scanner.next();
        Game game = new Game(player1, player2);
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

    public Game(String player1, String player2) {
        this.p1 = player1;
        this.p2 = player2;
    }

    /**
     * Initializes Game.class for a new playthrough
     */
    public void init() {
        this.word = "";
        this.turn = false;
        this.ended = false;
        lex = new Lexicon("src/main/java/nl/prog/ghost/lexicon/" +language+".txt");
        lex.init();
    }

    /**
     * Performs a move for the player who's turn it is.
     * @param move
     * @return status message
     */
    public String guess(String move) {
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
            String won = p2;
            if (turn) won = p1;
            ended = true;
            if (turn) livesP2--;
            else livesP1--;
            return newWord + " cannot form an existing word. " + won + " has won this round!";
        // losing condition 1
        } else if (newWord.length() > 3 && lex.exists(newWord)) {
            String won = p2;
            if (turn) won = p1;
            ended = true;
            if (turn) livesP2--;
            else livesP1--;
            return newWord + " is an existing word. " + won + " has won this round!";
        // continuing the game
        } else {
            return "OK";
        }
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
     * @return
     */
    public String getLanguage() {
        return this.language;
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

    /**
     * Defaults to dutch
     * @param lang
     */
    public void setLanguage(String lang) {
        this.language = lang;
    }
}