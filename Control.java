
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Control extends View implements ActionListener {

    private String stringGame = "---------";
    private char player = 'O';
    private char computer = 'X';
    private boolean isPlayerTurn = false;
    private Model model = new Model();

    public static void main(String[] args) {
        new Control();
    }


    Control() {
        for(int a =0; a < buttons.length; a ++){
            buttons[a].addActionListener(this);
        }

        computerMove();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        for(int a =0; a < buttons.length; a ++){
            if(e.getSource() == buttons[a] && stringGame.charAt(a) == '-'){
                buttons[a].setText(String.valueOf(player));
                updateGameString(a, player);
                isPlayerTurn = false;


                if(model.isWinner(player, stringGame)){
                    gameReset();
                    System.out.println("Player Wins");
                    return;
                } else if(model.isDraw(stringGame)){
                    gameReset();
                    System.out.println("Draw");
                    return;
                }

                computerMove();
            }
        }

        // new game button clicked
        if(e.getSource() == Restart){
            gameReset();
        }
    }

    void computerMove(){
        int move = model.computerMove(stringGame);
        if(move != -1){
            buttons[move].setText(String.valueOf(computer));
            updateGameString(move, computer);
            isPlayerTurn = true;

            if(model.isWinner(computer, stringGame)){
                gameReset();
                System.out.println("Computer Wins");
            } else if(model.isDraw(stringGame)){
                gameReset();
                System.out.println("Draw");
            }
        }
    }

    void gameReset(){
        model.boardReset(buttons);
        stringGame = "---------";
        isPlayerTurn = false;
        computerMove();
    }

    void updateGameString(int index, char player){
        StringBuilder stringBuilder = new StringBuilder(stringGame);
        stringBuilder.setCharAt(index, player);
        stringGame = stringBuilder.toString();
    }
}