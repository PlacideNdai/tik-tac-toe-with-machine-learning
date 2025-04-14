import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Model {

    String gameOverWith(String line) {
        // holizontal win conditions
        if (line.charAt(0) == line.charAt(1) && line.charAt(1) == line.charAt(2) && line.charAt(0) != '-') {
            return String.valueOf(line.charAt(0));
        } else if (line.charAt(3) == line.charAt(4) && line.charAt(4) == line.charAt(5) && line.charAt(3) != '-') {
            return String.valueOf(line.charAt(3));
        } else if (line.charAt(6) == line.charAt(7) && line.charAt(7) == line.charAt(8) && line.charAt(6) != '-') {
            return String.valueOf(line.charAt(6));
        }

        // vertical
        if (line.charAt(0) == line.charAt(3) && line.charAt(3) == line.charAt(6) && line.charAt(0) != '-') {
            return String.valueOf(line.charAt(0));
        } else if (line.charAt(1) == line.charAt(4) && line.charAt(4) == line.charAt(7) && line.charAt(1) != '-') {
            return String.valueOf(line.charAt(1));
        } else if (line.charAt(2) == line.charAt(5) && line.charAt(5) == line.charAt(8) && line.charAt(2) != '-') {
            return String.valueOf(line.charAt(2));
        }

        // diagonal
        if (line.charAt(0) == line.charAt(4) && line.charAt(4) == line.charAt(8) && line.charAt(0) != '-') {
            return String.valueOf(line.charAt(0));
        } else if (line.charAt(2) == line.charAt(4) && line.charAt(4) == line.charAt(6) && line.charAt(2) != '-') {
            return String.valueOf(line.charAt(2));
        }

        if (!line.contains("-")) {
            return "Draw";
        }

        return "";
    }

    void gameResultsShow(JButton[] buttons, String winner) {
        // disabling all the buttons
        for (JButton button : buttons) {
            button.setEnabled(false);
        }

        // pop after the game to show who won.
        JOptionPane.showMessageDialog(new Frame(), winner + " Won", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        // resetGame();
    }


    // cleaning the game for a new session, same scores for now.
    void resetGame(JButton[] buttons, String gameState, boolean gameWon, boolean whosTurn) {
        // cleaning the board
        for (int a = 0; a < buttons.length; a++) {
            buttons[a].setText("");
        }

        // clean string
        gameState = "---------";

        // computer turn
        whosTurn = false;

        // game state over to false
        gameWon = false;
    }
}
