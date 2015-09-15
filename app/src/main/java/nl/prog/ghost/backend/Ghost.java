package nl.prog.ghost.backend;

/**
 * @author Joram Wessels; 10631542
 */
public class Ghost {

    private String p1;
    private String p2;
    private String word;
    private boolean turn;   // 0 when p1, 1 when p2

    public Ghost(String player1, String player2) {
        this.p1 = player1;
        this.p2 = player2;
    }

    /**
     * Performs a move for the player who's turn it is.
     * @param move
     * @return status message
     */
    public String move(String move) {
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
     * Checks if the input is valid according to Ghost rules.
     * @param move
     * @return status message
     */
    private String validateMove(String move) {
        return "OK";
    }

    public String getP1() {
        return p1;
    }

    public String getP2() {
        return p2;
    }

    public String getWord() {
        return word;
    }

    /**
     * False when p1, True when p2.
     * @return turn
     */
    public boolean isTurn() {
        return turn;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }
}
