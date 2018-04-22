package morpion_serveur;

import javax.swing.JFrame;

import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JLabel;

import java.awt.Color;


public class ServerGUI {
	private JFrame fenetre;
	private JTextPane texteServeur;
	private JLabel lblNewLabel_1;
	JTextField jtf= new JTextField("");

	public ServerGUI(String myadress, int port) {
		initialize();
		setServerInfo(myadress, port);
	}

	private void initialize() {
		fenetre = new JFrame("Serveur - Morpion");
		fenetre.setBounds(300, 300, 300, 300);
		fenetre.getContentPane().setLayout(new GridLayout(2, 1, 30, 0));
		//fenetre.setBackground(Color.WHITE);
	    fenetre.setLocationRelativeTo(null);
	    fenetre.setResizable(false);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		//panel.setBackground(Color.WHITE);
		fenetre.getContentPane().add(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		texteServeur = new JTextPane();
		texteServeur.setEditable(false);
		panel.add(texteServeur);
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(Color.LIGHT_GRAY);
		fenetre.getContentPane().add(panel1);
		FlowLayout fl_panel1 = new FlowLayout(FlowLayout.CENTER, 10, 2);
		panel1.setLayout(fl_panel1);
		
		JLabel lblNewLabel = new JLabel("78");
		panel1.add(lblNewLabel);
		lblNewLabel.setText("Joueurs");
		
		lblNewLabel_1 = new JLabel("0");
		panel1.add(lblNewLabel_1);

	}

	public void setServerInfo(String myadress, int port) {
		texteServeur.setContentType( "text/html" );
		texteServeur.setText(String.format("<html><body align=\"center\" \n"
				+ "<br>Serveur Jeu du Morpion<br><br>Serveur Adresse : %s <br> Port : %d</body></html>", 
				myadress, port));	
		texteServeur.setBackground(Color.LIGHT_GRAY);
		
	}

	public JFrame getFrame() {
		return fenetre;
	}

	public JTextPane gettexteServeur() {
		return texteServeur;
	}

	public JLabel getLblNewLabel_1() {
		return lblNewLabel_1;
	}

}
