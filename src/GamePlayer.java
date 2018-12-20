
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class GamePlayer {
    /**
     * Setup Server Connection
     */
    private boolean running = false;
    private Socket connection;

    private DataInputStream receive;
    private DataOutputStream send;

    private ArrayList<ActionListener> listener = new ArrayList<>();

    public GamePlayer(Socket player) {
        this.connection = player;
    }

    public void start() {
        this.running = true;
        try {
            this.receive = new DataInputStream(this.connection.getInputStream());
            this.send = new DataOutputStream(this.connection.getOutputStream());
        } catch (Exception e) {

        }
        Thread t = new Thread() {
            public void run() {
                while (running) {
                    try {
                        String message = receive.readUTF();
                        notifyListener(message);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    public void addActionListener(ActionListener l) {
        this.listener.add(l);
    }

    public void send(String message) {
        if (this.send != null) {
            try {
                send.writeUTF(message);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    //gets a message from a player
    public String getMsg(GamePlayer p) {
        return p.receive.toString();
    }

    public void notifyListener(String message) {
        for (ActionListener l : listener) {
            l.actionPerformed(new ActionEvent(this, 0, message));
        }
    }

    public void stop() {
        this.running = false;

    }

    // Method for setting a boolean variable true
    public boolean started(boolean started) {
        if (!started) {

            return started = true;
        } else {
            return started;
        }

    }
}
