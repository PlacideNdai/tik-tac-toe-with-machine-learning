import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Control extends View implements ActionListener {

    // default variables

    boolean whosTurn = false;
    String gameState = "---------";
    String playerChar = "O";
    String computerChar = "X";
    boolean gameWon = false; // false should be the computer turn

    Model model = new Model();

    public static void main(String[] args) {
        new Control();
    }

    Control() {
        for (int a = 0; a < buttons.length; a++) {
            buttons[a].addActionListener(this);
        }

        Restart.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();

        if (btn.getText().equals("")) {
            // game going
            btn.setText(whosTurn ? playerChar : computerChar);
            whosTurn = !whosTurn;

            // Getting index of the button so that it can be used to update the button
            int btnIndex = -1;
            for (int a = 0; a < buttons.length; a++) {
                if (buttons[a] == btn) {
                    btnIndex = a;
                    break;
                }
            }

            // updating a state string
            if (btnIndex != -1) {
                gameState = gameState.substring(0, btnIndex) + btn.getText() + gameState.substring(btnIndex + 1);
            }

            // Who won? logic below will determind who won.
            // according to class instruction, DRAW = COMPUTER WON.

            if (model.gameOverWith(gameState).equals("X")) {
                // System.out.println("Computer won");

                model.gameResultsShow(buttons, model.gameOverWith(gameState));
                gameReset();
            } else if (model.gameOverWith(gameState).equals("O")) {
                // System.out.println("Player won");

                model.gameResultsShow(buttons, model.gameOverWith(gameState));
                gameReset();
            } else if (model.gameOverWith(gameState).equals("")) {
                // game going on

            } else {
                // System.out.println("Computer won by Draw");
                // model.gameResultsShow(buttons, "X");
                model.gameResultsShow(buttons, "X");

                gameReset();
            }

        } else if (btn.getText().equals("Start New Game")) {
            gameReset();
        }
    }

    void gameReset() {
        // board cleaning
        model.resetGame(buttons);
        // clean string
        gameState = "---------";

        // computer turn
        whosTurn = false;

        // game state over to false
        gameWon = false;
    }
}
