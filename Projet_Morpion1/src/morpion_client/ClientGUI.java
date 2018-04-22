package morpion_client;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class ClientGUI{
	private JFrame frame = new JFrame("Morpion");
    private JPanel boardPanel;
    private JLabel messageLabel = new JLabel("");
    private Square[] board = new Square[9];
    private ImageIcon croix, rond;
 
    public ClientGUI() throws Exception{
    	// Icons
    	croix = new ImageIcon("iconx.gif");
    	rond = new ImageIcon("icono.gif");
        // GUI
        boardPanel = new JPanel();
        boardPanel.setBackground(Color.black);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
        for (int i = 0; i < board.length; i++) {
            final int j = i;
            board[i] = new Square();
            boardPanel.add(board[i]);
            // Ajouter listener
            board[i].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    try {
                    	if (SocketClient.isMyTurn()) {
                    		SocketClient.getObjectOutput().writeObject(new String[]{"Step",Integer.toString(j)});
                    		System.out.println("Envoye de la commande vers serveur");
                    	}
					} catch (IOException e1) {
						e1.printStackTrace();
					};}});       
        }
        frame.getContentPane().add(boardPanel, "Center");
        messageLabel.setBackground(Color.lightGray);
        messageLabel.setForeground(Color.BLUE);
        frame.getContentPane().add(messageLabel, "South");
        frame.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		try {
					SocketClient.getObjectOutput().writeObject(new String[]{"Disconnect"});
				} catch (IOException e1) {
					e1.printStackTrace();
				}finally {
					SocketClient.exit();  
				}
            }
		});
        frame.setBounds(new Random().nextInt(300), new Random().nextInt(300), 318, 325);
        frame.setResizable(false);
        
    }
    
    public void comfirmServerInfo() {
        JPanel p = new JPanel(new BorderLayout(5,5));
        p.setBounds(300, 300, 30, 60);

        JPanel labels = new JPanel(new GridLayout(0,1,2,2));
        labels.add(new JLabel("URL de serveur", SwingConstants.RIGHT));
        labels.add(new JLabel("Port", SwingConstants.RIGHT));
        p.add(labels, BorderLayout.WEST);

        JPanel controls = new JPanel(new GridLayout(0,1,2,2));
        JTextField url = new JTextField(SocketClient.getServerUrl());
        controls.add(url);
        JTextField port = new JTextField(Integer.toString(SocketClient.getPort()));
        controls.add(port);
        p.add(controls, BorderLayout.CENTER);
        
//        JOptionPane.showMessageDialog(
//                frame, p, "Server Information", JOptionPane.QUESTION_MESSAGE);
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(frame, p, "Server Information", JOptionPane.DEFAULT_OPTION)){
        	SocketClient.setServerUrl(url.getText());
        	SocketClient.setPort(Integer.parseInt(port.getText()));
        }
    }

    /**
	 * Construire un carré pour placer le rond ou le croix
	 */
    static class Square extends JPanel {
        
		private static final long serialVersionUID = 1L;
		JLabel label = new JLabel((Icon)null);

        public Square() {
            setBackground(Color.WHITE);
            add(label);
        }

        public void setIcon(Icon icon) {
            label.setIcon(icon);
        }
    }

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JLabel getMessageLabel() {
		return messageLabel;
	}

	public Square[] getBoard() {
		return board;
	}

	public void validerStep(int i, String icon) {
		board[i].setIcon( icon.equals("croix") ? croix: rond);		
	}
}