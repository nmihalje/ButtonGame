import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private int port;
    private String host;
    private GameConnection connection;
    private GUI g;
    protected List<Integer> actualButtonList = new ArrayList<>();


    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start(GUI gui) {
        g = gui;
        try {
            connection = new GameConnection(new Socket(host, port));
            connection.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    getMessage(ae.getActionCommand(), gui);
                }
            });
            connection.startConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getMessage(String msg, GUI gui) {
        System.out.println(msg);
        System.out.println(msg);

        if(msg.contains("you win")) {
            String winner = msg.split(":")[1].split(";")[0];
            int winnerTime = Integer.parseInt(msg.split(":")[1].split(";")[1]);
        }
    }

    public void sendMessage(String msg) {
        connection.sendMessage(msg);
    }

    public void stop() {
        connection.close();
    }
}

