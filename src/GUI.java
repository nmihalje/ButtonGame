import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.sql.Timestamp;

public class GUI extends JFrame {

    private int x;
    private boolean startButtonClick = true;
    protected boolean buttonClicked = false;
    private JPanel buttonPanel = new JPanel(new GridLayout(4, 4));;
    private JPanel timePanel;
    private JLabel timeLabel = new JLabel();
    private JButton startButton = new JButton("START");
    private JButton stopButton = new JButton("EXIT");
    private JButton multiplayer = new JButton("Multiplayer");
    private JPanel westPanel;
    private JLabel hostlabel = new JLabel("Host");
    private JTextField textfield = new JTextField("          ");
    protected List<JButton> buttonList = new ArrayList<>();
    protected List<Integer> actualButtonList = new ArrayList<>();
    protected Timestamp timestamp;
    private RandomTimer timer;
    private Client c;
    private GameServer s;

    public static void main(String[] args) {

        new GUI();
    }

    public GUI() {
        this.setTitle("Button-Game");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        c = new Client("localhost",8888);

        startButton.setBackground(Color.GREEN);

        //ButtonEnable();

        GenerateButton();

        timePanel = new JPanel(new BorderLayout());
        timePanel.add(timeLabel, BorderLayout.NORTH);
        timePanel.add(stopButton, BorderLayout.SOUTH);

        westPanel = new JPanel(new BorderLayout());
        westPanel.add(hostlabel, BorderLayout.WEST);
        westPanel.add(textfield, BorderLayout.EAST);

        this.add(startButton, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);
        this.add(timePanel, BorderLayout.SOUTH);
        this.add(westPanel, BorderLayout.WEST);

        addAllActionListeners();
        this.setVisible(true);
    }

    private void addAllActionListeners() {
        for (JButton button : buttonList) {
            button.addActionListener(ae -> {
                button.setEnabled(false);
                button.setBackground(null);
                x++;
                if (x == actualButtonList.size()) {
                    x = 0;
                    timeLabel.setText(((new Timestamp(System.currentTimeMillis())).getTime() - timestamp.getTime()) + " ms");
                    StartGame();
                }
            });
        }
        startButton.addActionListener(ae -> {
            if (startButtonClick) {
                startButtonClick = false;
                StartGame();
                startButton.setEnabled(false);
                startButton.setBackground(null);
                timeLabel.setText("Get ready!");

            }  else if(!buttonClicked) {
                StartGame();
                timeLabel.setText("Get ready!");
            }
        });

        stopButton.addActionListener(ae -> {
            StopGame();
        });
    }

    private void GenerateButton() {
        for (int i = 0; i < 16; i++) {
            buttonList.add(new JButton());
        }
        for (JButton button : buttonList) {
            buttonPanel.add(button);
            button.setEnabled(false);
            button.setBackground(null);
            button.setOpaque(true);
        }
    }

    private void StartGame() {
        timer = new RandomTimer();
        timer.setCallBack(this);
        timer.start();
        buttonClicked = true;
    }

    private void StopGame(){
        System.exit(0);
    }

    /*private void ButtonEnable(){
        startButton.setEnabled(false);
        if(s.running == true) {
            startButton.setEnabled(true);
        }
            else{
                startButton.setEnabled(false);
            }
        }*/
    }
