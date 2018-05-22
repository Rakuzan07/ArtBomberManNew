package Artbomberman.gui;

import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Artbomberman.logic.Color;
import Artbomberman.logic.Gmanager;
import Artbomberman.logic.Player;
import Artbomberman.logic.Position;
import Artbomberman.logic.World;

public class GameFrame extends JFrame {

	private static final int HEIGHT = 1080, WIDTH = 1920;
	
	
	
	
	private GamePanel gamepanel;
	
	private ArrayList<Player> players=new ArrayList<Player>();
	
	private World world;
	
	public GameFrame() {
		this.setSize(WIDTH,HEIGHT);
		this.setTitle("ArtBomberMan");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		try {
			this.setIconImage(ImageIO.read(this.getClass().getResource("resources//icon//Icon.png")));
		} catch (IOException e) {}
		gamepanel=new GamePanel();
		this.setContentPane(gamepanel);
		this.gamepanel.setVisible(true);
		this.setVisible(true);
	}
}
