import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class GameServer {
    private int port;
    protected boolean running = false;
    private boolean clientready = false;
    private int clientreadycounter = 0;

    Random random_waittime = new Random();
    Random random_buttons = new Random();
    int[] randombuttonarray;

    private ArrayList<GamePlayer> clients = new ArrayList<>();

    public GameServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        GameServer s = new GameServer(8888);
        s.start();
        //Gets message from client back
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        while (!input.equalsIgnoreCase("stop")) {
            try {
                input = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        s.stop();
        System.out.println("SERVER: Server stopped");
    }

    private ActionListener broadcastListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            System.out.println("Nachricht empfangen: " + e.getActionCommand());
            GameConnection connection = (GameConnection) e.getSource();
            if (e.getActionCommand().equals("ready")) {
                clientreadycounter++;

                int randomTime = random_waittime.nextInt(4000) + 2000;

                for (int j = 0; j <= 3; j++) {
                    int randombutton = random_waittime.nextInt(1) + 15;
                    randombuttonarray[j] = randombutton;
                }


                for (GamePlayer gp : clients) {

                    gp.send("" + randomTime);

                    gp.send("" + randombuttonarray[0] + "," + randombuttonarray[1] + "," + randombuttonarray[2] + ","
                            + randombuttonarray[3]);
                    System.out.println("" + randombuttonarray[0]);


                }
            } else if (e.getActionCommand().equals("finished")) {
                // sende Nachricht an alle clients
                // die dem Server derzeit bekannt sind
                for (GamePlayer gp : clients) {
                    if (!gp.equals(e.getSource())) {
                        gp.send("you lose");
                    }
                }

                ((GamePlayer) e.getSource()).send("you win");
            }

        }
    };

    public void start() {
        this.running = true;

        Thread serverThread = new Thread() {
            public void run() {
                try (ServerSocket server = new ServerSocket(GameServer.this.port)) {
                    while (running) {
                        Socket client = server.accept(); // Server bleibt solange hier stehen bis
                        // dass sich 1Client verbunden hat
                        GamePlayer p = new GamePlayer(client);

                        p.addActionListener(broadcastListener);

                        clients.add(p);
                    }
                } catch (Exception e) {

                }
            }
        };
        serverThread.start();
    }

    public void stop() {
        this.running = false;
    }
}
