import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Control extends View implements ActionListener {

    private String stringGame = "---------";
    private char player = 'O';
    private char computer = 'X';
    private Model model = new Model();
    private double beta = 0.5;

    public static void main(String[] args) {
        new Control();
    }

    Control() {
        // traning the mode;
        model.trainAgainstItself(10000);

        for (int a = 0; a < buttons.length; a++) {
            buttons[a].addActionListener(this);
        }
        Restart.addActionListener(this);
        computerMove();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int a = 0; a < buttons.length; a++) {
            if (e.getSource() == buttons[a] && stringGame.charAt(a) == '-') {
                buttons[a].setText(String.valueOf(player));
                updateGameString(a, player);

                if (model.isWinner(player, stringGame)) {
                    System.out.println("Player Wins");
                    gameReset();
                    return;
                } else if (model.isDraw(stringGame)) {
                    System.out.println("Draw");
                    gameReset();
                    return;
                }

                computerMove();
                return;
            }
        }

        if (e.getSource() == Restart) {
            gameReset();
        }
    }

    void computerMove() {
        int move = model.computerMove(stringGame);
        if (move != -1) {
            String previousString = stringGame;
            buttons[move].setText(String.valueOf(computer));
            updateGameString(move, computer);

            if (model.isWinner(computer, stringGame)) {
                double reward = model.evaluation(stringGame, computer, player);
                model.learn(previousString, move, stringGame, reward, beta);
                System.out.println("Computer Wins");
                gameReset();
                return;
            } else if (model.isDraw(stringGame)) {
                double reward = model.evaluation(stringGame, computer, player);
                model.learn(previousString, move, stringGame, reward, beta);
                System.out.println("Draw");
                gameReset();
                return;
            }

            double reward = model.evaluation(stringGame, computer, player);
            model.learn(previousString, move, stringGame, reward, beta);
        }
    }

    void gameReset() {
        model.boardReset(buttons);
        stringGame = "---------";
        computerMove();
    }

    void updateGameString(int index, char player) {
        StringBuilder stringBuilder = new StringBuilder(stringGame);
        stringBuilder.setCharAt(index, player);
        stringGame = stringBuilder.toString();
    }
}
