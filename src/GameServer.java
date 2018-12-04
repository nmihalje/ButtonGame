import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GameServer {
	private int port;
	private boolean running = false;
	private boolean clientready = false;

	private ArrayList<GamePlayer> clients = new ArrayList<>();

	public GameServer(int port) {
		this.port = port;
	}

	private ActionListener broadcastListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("ready")) {

				for (GamePlayer gp : clients) {
					//TODO: 
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
