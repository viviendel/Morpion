package morpion_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;


public class SocketClient{
	// Singleton
	//private static String myadress;
	private static InetAddress myadress;
	private static ObjectOutputStream objectOutput;
	private static Socket socket;
	private static ObjectInputStream objectInput;
	
	//static String serverUri = "192.168.1.95"; // si serveur sur une autre machine
	private static String serverUrl = "127.0.0.1"; //127.0.0.1 si serveur sur son propor host
	private static int port = 8051;
	private static String[] cmd;
	private static ClientGUI client;
	
	// marks
	private static String type;
	private static boolean myTurn;
	private static String icon, opIcon;
	private static String stopType;
	 
	public static void main(String[] args) throws Exception {
		while(true){
			client = new ClientGUI();
	        client.getFrame().setVisible(true);
	        client.comfirmServerInfo();
	        client.getMessageLabel().setText("Attente d'un adversaire...");
	        Connect();
	        if (!playAgain()) break;
		}	
	}

	private static void Connect() throws Exception{
		myadress = InetAddress.getLocalHost();
		//myadress = HostAdress.getHostAddress();
		System.out.println("adresse du client :" + myadress);
		try {
			socket = new Socket(serverUrl, port);
		}catch (IOException e) {
			JOptionPane.showMessageDialog(client.getFrame(), "\tProbleme avec le serveur " + serverUrl + " et le port " + port);
			System.exit(0);
		}
		System.out.println("socket :" + serverUrl + " " + port);
		objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectInput = new ObjectInputStream(socket.getInputStream());
				
		// traiter les commands
		while (true) {
			System.out.println("Start scanning commands");
			cmd = (String[]) objectInput.readObject();
			System.out.println("Client recoit une commande: " + String.join(" ", cmd));

            if (cmd[0].equals("Start")) {
				String type = cmd[1];
				icon = type.equals("x") ? "croix" : "rond";
	            opIcon  = type.equals("x") ? "rond" : "croix"; 
	            System.out.println("Icon established...");
				myTurn = (type.equals("x")) ? true: false;
				String mes = myTurn ? "A vous de jouer": "En attente du coup de votre adversaire";
				client.getMessageLabel().setText(mes);
				client.getFrame().setTitle("Morpion" + type.toUpperCase());
            }
            
            if (cmd[0].equals("Step")) {
            	client.validerStep(Integer.parseInt(cmd[1]), icon);
            	System.out.println("Commande valide: "+icon);
				client.getMessageLabel().setText("En attente du coup de votre adversaire...");
            	myTurn = false;
				
			}
            
            if (cmd[0].equals("OpStep")) {
            	client.validerStep(Integer.parseInt(cmd[1]), opIcon);
				client.getMessageLabel().setText("A vous  de jouer...");
            	myTurn = true;
				
			}
            
			if (cmd[0].equals("Disconnect")) {
				stopType = "Disconnect";
				break;
			}
			
			if (cmd[0].equals("Win")) {
				stopType = "Win";
				break;		
			}
			
			if (cmd[0].equals("Lose")) {
				stopType = "Lose";
				break;
			}
			
			if (cmd[0].equals("Tie")) {
				stopType = "Tie";
				break;
			}
		}
		
		socket.close();
		System.out.println("Fin");
	}

	private static boolean playAgain() {
		client.getMessageLabel().setText(null);
		String mes;
		switch (stopType) {
		case "Win":
			mes = "<html><body>Victoire !!!";
			break;
		case "Tie":
			mes = "<html><body>Vous avez fait un match nul";
			break;
		case "Lose":
			mes =  "<html><body>Vous avez perdu";
			break;
		default:
			mes = "<html><body>Votre adversaire a quitte la partie";
			break;
		}
        int response = JOptionPane.showConfirmDialog(client.getFrame(), mes+"<br>Voulez vous rejouer?</body></html>",
            "", JOptionPane.YES_NO_OPTION);
        client.getFrame().dispose();
        return response == JOptionPane.YES_OPTION;
    }
	
	public static String getType() {
		return type;
	}

	public static void setType(String type) {
		SocketClient.type = type;
	}

	public static ObjectOutputStream getObjectOutput() {
		return objectOutput;
	}

	public static Socket getSocket() {
		return socket;
	}

	public static ObjectInputStream getObjectInput() {
		return objectInput;
	}

	public static int getPort() {
		return port;
	}

	public static boolean isMyTurn() {
		return myTurn;
	}

	public static void setMyTurn(boolean myTurn) {
		SocketClient.myTurn = myTurn;
	}

	public static void setIcon(String icon) {
		SocketClient.icon = icon;
	}

	public static void setOpIcon(String opIcon) {
		SocketClient.opIcon = opIcon;
	}

	public static String getIcon() {
		return icon;
	}

	public static String getOpIcon() {
		return opIcon;
	}

	public static void exit() {
		System.exit(0);
	}

	public static String getServerUrl() {
		return serverUrl;
	}

	public static void setServerUrl(String serverUrl) {
		SocketClient.serverUrl = serverUrl;
	}

	public static void setPort(int port) {
		SocketClient.port = port;
	}

}
