import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;

public class Model {

    private HashMap<String, HashMap<Integer, Double>> qTable = new HashMap<>();
    private double gamma = 0.9;
    private double epsilon = 0.1;

    boolean isWinner(char player, String gameString) {
        int[][] winConditions = {
                { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 },
                { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
                { 0, 4, 8 }, { 2, 4, 6 }
        };
        for (int[] cond : winConditions) {
            if (gameString.charAt(cond[0]) == player &&
                    gameString.charAt(cond[1]) == player &&
                    gameString.charAt(cond[2]) == player) {
                return true;
            }
        }
        return false;
    }

    boolean isDraw(String gameString) {
        return !gameString.contains("-");
    }

    int computerMove(String gameString) {
        Random rand = new Random();

        List<Integer> validMoves = new ArrayList<>();
        for (int i = 0; i < gameString.length(); i++) {
            if (gameString.charAt(i) == '-')
                validMoves.add(i);
        }

        if (rand.nextDouble() < epsilon && !validMoves.isEmpty()) {
            return validMoves.get(rand.nextInt(validMoves.size()));
        }

        HashMap<Integer, Double> actions = qTable.getOrDefault(gameString, new HashMap<>());

        double bestValue = Double.NEGATIVE_INFINITY;
        int bestMove = -1;

        for (int move : validMoves) {
            double value = getQVvalue(gameString, move);
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        return bestMove;
    }

    void learn(String currentState, int action, String nextState, double reward, double beta) {
        double maxNextQ = qTable.getOrDefault(nextState, new HashMap<>())
                .values().stream().max(Double::compare).orElse(0.0);
        updateQValue(currentState, action, reward, maxNextQ, beta);
    }

    double evaluation(String gameString, char computer, char player) {
        int movesMade = 9 - countDashes(gameString);

        if (isWinner(computer, gameString))
            return 1.0 - (movesMade * 0.05);
        if (isWinner(player, gameString))
            return -1.0 + (movesMade * 0.05);
        if (isDraw(gameString))
            return 0.2;

        // reward shaping
        // according to my understanding, reward shaping is when you reward the agent
        // (in our case computer)
        // for making 'good moves' example: blocking the opponent's win, taking the
        // center or cornter

        double reward = 0.0;

        if (gameString.charAt(4) == computer)
            reward += 0.05;

        // corners
        int[] corners = { 0, 2, 6, 8 };

        for (int corner : corners) {
            if (gameString.charAt(corner) == computer)
                reward += 0.025;
        }

        if ((thereIsTwoInRow(gameString, player)))
            reward -= 0.1;
        if ((thereIsTwoInRow(gameString, computer)))
            reward += 0.1;

        return reward;
    }

    // is there two marks in a row? return true if there is
    boolean thereIsTwoInRow(String gameString, char mark) {
        int[][] lines = {
                { 0, 1, 2 },
                { 3, 4, 5 },
                { 6, 7, 8 },
                { 0, 3, 6 },
                { 1, 4, 7 },
                { 2, 5, 8 },
                { 0, 4, 8 },
                { 2, 4, 6 }
        };

        for (int[] line : lines) {
            int count = 0;
            int empty = 0;

            for (int index : line) {
                if (gameString.charAt(index) == mark)
                    count++;
                if (gameString.charAt(index) == '-')
                    empty++;
            }

            if (count == 2 && empty == 1)
                return true;
        }

        return false;
    }

    // counting the dashes in the game string
    int countDashes(String gameString) {
        int count = 0;
        for (char c : gameString.toCharArray()) {
            if (c == '-')
                count++;
        }
        return count;
    }

    void boardReset(JButton[] buttons) {
        for (JButton button : buttons) {
            button.setText("");
        }
    }

    private double getQVvalue(String state, int action) {
        return qTable.getOrDefault(state, new HashMap<>()).getOrDefault(action, 0.0);
    }

    private void updateQValue(String state, int action, double reward, double maxNextQ, double beta) {
        double currentQ = getQVvalue(state, action);
        double newQ = (1 - beta) * currentQ + beta * (reward + gamma * maxNextQ);
        qTable.computeIfAbsent(state, k -> new HashMap<>()).put(action, newQ);

        System.out.println(
                "Action: " + action + " | Reward: " + reward + " | MaxNextQ: " + maxNextQ + " | NewQ: " + newQ);
    }

    // pre trained model
    public void trainAgainstItself(int episodes) {
        char[] players = { 'X', 'O' };
        Random rand = new Random();

        for (int episode = 0; episode < episodes; episode++) {
            String state = "---------";
            char currentPlayer = players[rand.nextInt(2)];
            boolean gameOver = false;

            while (!gameOver) {
                List<Integer> validMoves = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    if (state.charAt(i) == '-')
                        validMoves.add(i);
                }

                int action;
                if (rand.nextDouble() < epsilon) {
                    // Explore: pick random
                    action = validMoves.get(rand.nextInt(validMoves.size()));
                } else {
                    // Exploit: pick best
                    double bestValue = Double.NEGATIVE_INFINITY;
                    int bestMove = validMoves.get(0);

                    for (int move : validMoves) {
                        double value = getQVvalue(state, move);
                        if (value > bestValue) {
                            bestValue = value;
                            bestMove = move;
                        }
                    }

                    action = bestMove;
                }

                // Generate the next state
                StringBuilder nextStateBuilder = new StringBuilder(state);
                nextStateBuilder.setCharAt(action, currentPlayer);
                String nextState = nextStateBuilder.toString();

                // Evaluate and learn immediately
                double reward = evaluation(nextState, 'X', 'O');
                learn(state, action, nextState, reward, 0.5);

                // Check for terminal condition
                if (isWinner(currentPlayer, nextState) || isDraw(nextState)) {
                    gameOver = true;
                }

                state = nextState;
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }

        }

        System.out.println("Pre-training complete with " + episodes + " games.");
    }

}
