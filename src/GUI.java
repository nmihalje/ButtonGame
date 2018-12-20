import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GUI extends JFrame {

    private int x;
    protected int port;
    protected String host;
    private boolean startButtonClick = true;
    protected boolean buttonClicked = false;
    private JPanel buttonPanel = new JPanel(new GridLayout(4, 4));
    private JPanel northpanel = new JPanel();
    private JLabel hostlabel = new JLabel("Host");
    private JLabel portlabel = new JLabel("Port");
    private JButton startButton = new JButton("START");
    private JButton readyButton = new JButton("READY");
    private JButton stopButton = new JButton("EXIT");

    private JTextField textfield = new JTextField(10);
    private JTextField textfield2 = new JTextField(4);
    protected List<JButton> buttonList = new ArrayList<>();
    protected List<Integer> actualButtonList = new ArrayList<>();
    private RandomTimer timer;

    public static void main(String[] args) {
        new GUI();
    }

    public GUI() {
        this.setTitle("Button-Game");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        startButton.setBackground(Color.GREEN);

        GenerateButton();

        northpanel.setLayout(new FlowLayout());
        northpanel.add(startButton);
        northpanel.add(hostlabel);
        northpanel.add(textfield);
        northpanel.add(portlabel);
        northpanel.add(textfield2);
        northpanel.add(readyButton);

        this.add(northpanel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);
        this.add(stopButton, BorderLayout.SOUTH);

        for (JButton button : buttonList) {
            button.addActionListener(ae -> {
                button.setEnabled(false);
                button.setBackground(null);
                x++;
                if (x == actualButtonList.size()) {
                    x = 0;
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

            } else if (!buttonClicked) {
                StartGame();
            }
        });

        readyButton.addActionListener(a -> {
            Client c = new Client(host, port);
            if (host != null && port != 0) {
                c.start(this);
                c.sendMessage("is ready");
                System.out.println("Connected Client is ready.");
            }
        });

        textfield.addActionListener(e -> {
            host = textfield.getText();
        });

        textfield2.addActionListener(e -> {
            port = Integer.parseInt(textfield2.getText());
        });

        stopButton.addActionListener(ae -> {
            StopGame();
        });

        this.setVisible(true);
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

    private void StopGame() {
        System.exit(0);
    }
}