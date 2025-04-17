import javax.swing.JButton;

public class Model {

    // winning condition
    boolean isWinner(char player, String gameString){
        int[][] winConditions = {
            {0,1,2},
            {3,4,5},
            {6,7,8},
            {0,3,6},
            {1,4,7},
            {2,5,8},
            {0,4,8},
            {2,4,6}
        };

        for(int[] winCondition : winConditions){
            if(gameString.charAt(winCondition[0]) == player &&
                gameString.charAt(winCondition[1]) == player &&
                gameString.charAt(winCondition[2]) == player){
                return true;
            }
        }
        return false;
    }

    // given a board, return true if the game is draw.
    boolean isDraw(String gameString){
        return !gameString.contains("-");
    }

    // computer move
    int computerMove(String gameString){
        for(int a =0;a < gameString.length(); a++){
            if(gameString.charAt(a) == '-'){
                return a;
            }
        }
        return -1;
    }

    void boardReset(JButton[] buttons){
        for(JButton button : buttons){
            button.setText("");
        }
    }
}