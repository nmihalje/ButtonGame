import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class GameConnection {

    private Socket socket;
    private DataInputStream clientToServer;
    private DataOutputStream serverToClient;
    private boolean runningReceiveThread = true;
    private boolean running = false;
    private int i = 0;

    private ArrayList<ActionListener> listeners = new ArrayList<>();

    public GameConnection(Socket connection) {
        socket = connection;

        try {
            clientToServer = new DataInputStream(socket.getInputStream());
            serverToClient = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server: Client connected");
    }

    public void startReceiveThread() {
        runningReceiveThread = true;
        new Thread() {
            public void run() {
                while (runningReceiveThread) {
                    if (listeners != null) {
                        try {
                            // receive message from Client
                            String message = clientToServer.readUTF();
                            System.out.println("    Message:" + message);
                            // send to all connected Listeners
                            notifyListeners(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        i++;
                        if (i >= 10000) {
                            System.out.println("Server: No Client connected");
                        }
                    }
                }
            }
        }.start();
    }

    public void stopReceiveThread() {
        runningReceiveThread = false;
    }

    public void close() {
        try {
            serverToClient.close();
            clientToServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String massage) {
        try {
            serverToClient.writeUTF(massage);
            serverToClient.flush();
        } catch (IOException e) {
            e.printStackTrace();
            GameConnection.this.close();
        }

    }

    private void notifyListeners(String massage) {
        for (ActionListener l : listeners) {
            l.actionPerformed(new ActionEvent(this, 0, massage));

        }
    }

    public void startConnection() {
        new Thread(() -> {
            running = true;
            while (running) {
                try {
                    String input = clientToServer.readUTF();
                    notifyListeners(input);
                } catch (IOException e) {
                    GameConnection.this.close();
                }
            }
        }).start();
    }

    public void addMessageListener(ActionListener l) {
        listeners.add(l);
    }

    public void addActionListener(ActionListener actionListener) {
        listeners.add(actionListener);
    }
}
