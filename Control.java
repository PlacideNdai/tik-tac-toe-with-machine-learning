import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Control extends View implements ActionListener {

    private String stringGame = "---------";
    private char player = 'O';
    private char computer = 'X';
    private Model model = new Model();
    private double beta;
    private String lastComputerState = null;
    private int lastComputerMove = -1;
    private double computerSCore = 0.0;
    private double playerSCore = 0.0;

    public static void main(String[] args) {
        new Control();
    }

    Control() {
        // traning the mode;
        model.trainAgainstItself(50000);

        for (int a = 0; a < buttons.length; a++) {
            buttons[a].addActionListener(this);
        }
        Restart.addActionListener(this);
        computerMove();
    }

    // button action listener
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int a = 0; a < buttons.length; a++) {
            if (e.getSource() == buttons[a] && stringGame.charAt(a) == '-') {
                buttons[a].setText(String.valueOf(player));
                updateGameString(a, player);

                if (model.isWinner(player, stringGame)) {
                    System.out.println("Player Wins");
                    playerSCore += 1;

                    scoreTracker.setText(String.valueOf(playerSCore) + " / " + String.valueOf(computerSCore));
                    gameReset();
                    return;
                } else if (model.isDraw(stringGame)) {
                    System.out.println("Draw");
                    playerSCore += 0.5;
                    computerSCore += 0.5;
                    scoreTracker.setText(String.valueOf(playerSCore) + " / " + String.valueOf(computerSCore));

                    gameReset();
                    return;
                }

                computerMove();
                return;
            }
        }

        // restart button
        if (e.getSource() == Restart) {
            gameReset();
        }
    }

    // computer move function
    void computerMove() {
        // getting the beta value.
        try {
            double betaValueGetter = Double.parseDouble(betaValue.getText());

            // validate the beta value. It should be between 0 and 1.
            if (betaValueGetter < 0 || betaValueGetter > 1) {
                beta = 0.5;
                betaValue.setText("0.5");
            } else {
                beta = betaValueGetter;
            }

        } catch (Exception e) {
            beta = 0.5;
            betaValue.setText("0.5");
        }

        int move = model.computerMove(stringGame);
        if (move != -1) {
            // String previousString = stringGame;
            lastComputerState = stringGame;
            lastComputerMove = move;


            buttons[move].setText(String.valueOf(computer));
            updateGameString(move, computer);

            if (model.isWinner(computer, stringGame)) {
                double reward = model.evaluation(stringGame, computer, player);
                // model.learn(previousString, move, stringGame, reward, beta);
                model.learn(lastComputerState, lastComputerMove, stringGame, reward, beta);
                System.out.println("Computer Wins");

                scoreTracker.setText(String.valueOf(playerSCore) + " / " + String.valueOf(computerSCore));
                computerSCore += 1;

                gameReset();
                return;
            } else if (model.isDraw(stringGame)) {
                double reward = model.evaluation(stringGame, computer, player);
                // model.learn(previousString, move, stringGame, reward, beta);
                model.learn(lastComputerState, lastComputerMove, stringGame, reward, beta);
                System.out.println("Draw");
                computerSCore += 0.5;
                playerSCore += 0.5;

                scoreTracker.setText(String.valueOf(playerSCore) + " / " + String.valueOf(computerSCore));
                gameReset();
                return;
            }

            double reward = model.evaluation(stringGame, computer, player);
            // model.learn(previousString, move, stringGame, reward, beta);
            model.learn(lastComputerState, lastComputerMove, stringGame, reward, beta);
        }
    }

    // reset the game mothod
    void gameReset() {
        model.boardReset(buttons);
        stringGame = "---------";
        computerMove();
    }

    // update the game string method
    void updateGameString(int index, char player) {
        StringBuilder stringBuilder = new StringBuilder(stringGame);
        stringBuilder.setCharAt(index, player);
        stringGame = stringBuilder.toString();
    }
}
