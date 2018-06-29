package Artbomberman.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class GameFrame extends JFrame {

	private static final int HEIGHT = 1080, WIDTH = 1920;
	
	private GamePanel gamepanel;
	
	
	public GameFrame() {
		this.setSize(WIDTH,HEIGHT);
		this.setTitle("ArtBomberMan");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		try {
			this.setIconImage(ImageIO.read(this.getClass().getResource("resources//icon//Icon.png")));
		} catch (IOException e) {}
		gamepanel=new GamePanel();
		this.setContentPane(gamepanel);
		this.addKeyListener(gamepanel);
		this.gamepanel.setVisible(true);
		this.setVisible(true);
	
	
	this.addWindowListener (new WindowAdapter () {		
		public void windowClosing (WindowEvent e) {
			if(gamepanel.multiplayer)
			 {
				gamepanel.closeSocket();			//SE SI VERIFICA LA CHIUSURA DELLA FINESTRA E SIAMO IN MULTIPLAYER CHIUDIAMO I CANALI DI COMUNICAZIONE
			 }
	  }
	  });
	}
	
	
}
