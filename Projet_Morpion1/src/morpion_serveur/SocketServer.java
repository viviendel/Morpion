package morpion_serveur;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import net_utils.HostAdress;


public class SocketServer {
	static final int port = 8051;
	static int nbJoueur;
	static ServerGUI serverGUI;
	static private Socket socketService1, socketService2;

	public static void main(String[] args) throws Exception {
		String myadress = HostAdress.getHostAddress();
		System.out.println("Serveur: " + myadress);
		System.out.println("Port: " + port );
		//  GUI  serveur
		serverGUI = new ServerGUI(myadress, port);
		serverGUI.getFrame().setVisible(true);
		
		
		ServerSocket socketEcoute = new ServerSocket(port);
		boolean end = false;
		nbJoueur = 0;
		while (!end) {
			Game game = new Game();
			System.out.println("\n<<<Attente de connexion>>>");
			socketService1 = socketEcoute.accept(); 
			nbJoueur++; 
			serverGUI.getLblNewLabel_1().setText(Integer.toString(nbJoueur));
			game.joueur1 = game.new Joueur(socketService1,"x");
			System.out.println("\nUne connexion est etablie ("
					+ socketService1.getRemoteSocketAddress() + ")");
			
			socketService2 = socketEcoute.accept();
			nbJoueur++;
			serverGUI.getLblNewLabel_1().setText(Integer.toString(nbJoueur));
			game.joueur2 = game.new Joueur(socketService2,"o");
			System.out.println("Autre connexion etablie ("
					+ socketService2.getRemoteSocketAddress() + ")");
			if (socketService1.isInputShutdown() || socketService2.isInputShutdown()) {
			nbJoueur--;
			serverGUI.getLblNewLabel_1().setText(Integer.toString(nbJoueur));
				continue;
		}
			game.startGame();
			System.out.println("\n<<<Nouvelle partie de morpion commence>>>"); 
		}

		socketEcoute.close();
		System.out.println("Fin !");
	}
}


class Game {	
	int [] grille = {0,0,0,
					0,0,0,
					0,0,0};
	Joueur joueur1,joueur2;
	private boolean stoped = false;
	
	public void startGame() {
		joueur1.setOpponent(joueur2);
		joueur2.setOpponent(joueur1);
		joueur1.start();
		joueur2.start();
		}

	public boolean checkWin() {
        return
            (grille[0] != 0 && grille[0] == grille[1] && grille[0] == grille[2])
          ||(grille[3] != 0 && grille[3] == grille[4] && grille[3] == grille[5])
          ||(grille[6] != 0 && grille[6] == grille[7] && grille[6] == grille[8])
          ||(grille[0] != 0 && grille[0] == grille[3] && grille[0] == grille[6])
          ||(grille[1] != 0 && grille[1] == grille[4] && grille[1] == grille[7])
          ||(grille[2] != 0 && grille[2] == grille[5] && grille[2] == grille[8])
          ||(grille[0] != 0 && grille[0] == grille[4] && grille[0] == grille[8])
          ||(grille[2] != 0 && grille[2] == grille[4] && grille[2] == grille[6]);
    }
	
	class Joueur extends Thread{
		String type;
		ObjectOutputStream myOutput;
		ObjectInputStream myInput;
		private Joueur opponent;
		Socket mySocket;
		
		public Joueur(Socket mySocket, String type) throws Exception {
			this.type = type;
			this.mySocket = mySocket;
			myOutput = new ObjectOutputStream(mySocket.getOutputStream()); // Construire ObjectOutputStream avant ObjectInputStream !!!
			myInput = new ObjectInputStream(mySocket.getInputStream());
		}
			
		public void run() {
			try {			
				String[] cmd;
				myOutput.writeObject(new String[]{"Start", type});
				while (true) {
					cmd = (String[]) myInput.readObject();
					System.out.println("Requete envoye au serveur par les clients: "  +String.join(" ", cmd));
					

					if (stoped) {
						myOutput.writeObject(new String[]{"Disconnected"});
						break;
					}
					if(cmd[0].equals("Step") && grille[Integer.parseInt(cmd[1])] == 0) {
						System.out.println("Validation et transfert de la requete aux deux clients....");
						opponent.myOutput.writeObject(new String[]{"OpStep",cmd[1]}); 
						myOutput.writeObject(cmd);
						grille[Integer.parseInt(cmd[1])] = (type.equals("x")) ? 1: 2;
						if (checkWin()) {
							myOutput.writeObject(new String[]{"Win"});
							opponent.myOutput.writeObject(new String[]{"Lose"});
						}
						if (filledUp()) {
							myOutput.writeObject(new String[]{"Tie"});
							opponent.myOutput.writeObject(new String[]{"Tie"});
						}
					}
					if (cmd[0].equals("Disconnect")) {
						opponent.myOutput.writeObject(cmd);
						SocketServer.nbJoueur -= 2;
						SocketServer.serverGUI.getLblNewLabel_1().setText(Integer.toString(SocketServer.nbJoueur));
						stoped = true;
					}
					/*if (cmd[0].equals("Disconnect")) {
						opponent.myOutput.writeObject(cmd);
						SocketServer.nbJoueur -= 2;
						SocketServer.serverGUI.getLblNewLabel_1().setText(Integer.toString(SocketServer.nbJoueur));
						stoped = true;
					}*/
				}
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {mySocket.close();} catch (IOException e) {}
			}
		}

		public Joueur getOpponent() {
			return opponent;
		}

		public void setOpponent(Joueur opponent) {
			this.opponent = opponent;
		}
	}

	public boolean filledUp() {
		for (int i = 0; i < grille.length; i++) {
			if (grille[i] == 0) return false;
		}
		return true;

	}
		

}
	
	
