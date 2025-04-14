import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class View {

    JFrame frame = new JFrame("Tic Tac Toe with Learning");

    JButton[] buttons = new JButton[9];

    JPanel bord_panel = new JPanel(),
            button_panel = new JPanel(),
            name_score_panel = new JPanel();

    // low componets
    JTextField betaValue = new JTextField();
    JLabel betaValueLabel = new JLabel("Beta ");
    JButton Restart = new JButton("Start New Game");
    JLabel scoreTracker = new JLabel(" 0 / 0 ");

    public static void main(String[] args) {
        new View();
    }

    View() {


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100,500,700);
        frame.setVisible(true);

        // adding panels
        
            // board settings
            bord_panel.setLayout(new GridLayout(3,3));


            // adding buttons to the bord panel using a for loop.
            for(int a =0; a < buttons.length; a ++){
                buttons[a] = new JButton();
                buttons[a].setBackground(Color.white);
                bord_panel.add(buttons[a]);
            }

        frame.setLayout(new BorderLayout());
        frame.add(bord_panel);

        // bottom panel settigns
            button_panel.add(Restart);
            button_panel.add(betaValueLabel);
            button_panel.add(betaValue);
            button_panel.add(scoreTracker);
            button_panel.setLayout(new GridLayout(1,4));

            // editing the view
            Restart.setBackground(Color.white);
            betaValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            scoreTracker.setHorizontalAlignment(SwingConstants.CENTER);

        frame.add(button_panel,BorderLayout.SOUTH);
    }
}