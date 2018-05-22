package Artbomberman.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import Artbomberman.logic.Color;
import Artbomberman.logic.Gmanager;
import Artbomberman.logic.Player;
import Artbomberman.logic.Position;
import Artbomberman.logic.World;
import Artbomberman.logic.Player.Status;

public class GamePanel extends JPanel {

	private static final int DIM_ASSET = 64;

	private static final int INIT_WORLD_DIM = 13 , NUM_STEP=16 , SPEED=4 , IDLE=0 , EXPLOSION=500;

	private ArrayList<Player> players;

	private World world;
	
	private Gmanager gmanager;

	private Toolkit tk;
	
	private int contUpdate=NUM_STEP-1 , contMovement=0;
	
	private ArrayList<Integer> contAnimation=new ArrayList<Integer>();

	private Image title, ground, groundGreen , groundRed , groundBlue , ring1, ring2, ring3, ring4, ring5, editor, play , bomb;

	private ArrayList<Image> playerUp = new ArrayList<Image>();

	private ArrayList<Image> playerDown = new ArrayList<Image>();

	private ArrayList<Image> playerLeft = new ArrayList<Image>();

	private ArrayList<Image> playerRight = new ArrayList<Image>();
	
	private ArrayList<Position> initPosition = new ArrayList<Position>();

	private double height, width;

	private Random random = new Random();

	public GamePanel() {
		super();
		players = new ArrayList<Player>();
		contAnimation.add(0);
		contAnimation.add(0);
		contAnimation.add(0);
		initPosition.add(new Position(0,0));
		initPosition.add(new Position(INIT_WORLD_DIM-1,0));
		initPosition.add(new Position(INIT_WORLD_DIM / 2, INIT_WORLD_DIM - 1));
		players.add(new Player(Color.BLUE, new Position(0, 0),Status.DOWN));
		players.add(new Player(Color.GREEN, new Position(INIT_WORLD_DIM - 1, 0),Status.DOWN));
		players.add(new Player(Color.RED, new Position(INIT_WORLD_DIM / 2, INIT_WORLD_DIM - 1),Status.UP));
		System.out.println(players.get(0).getPosition()+" "+players.get(1).getPosition()+" "+players.get(2).getPosition());
		world = new World(players, INIT_WORLD_DIM);
		gmanager=new Gmanager(players,world);
		height = this.getHeight();
		width = this.getWidth();
		tk = Toolkit.getDefaultToolkit();
		title = tk.getImage(this.getClass().getResource("resources//entry//Titolo.png"));
		ground = tk.getImage(this.getClass().getResource("resources//entry//ground_gray.png"));
		groundGreen = tk.getImage(this.getClass().getResource("resources//entry//ground_green.png"));
		groundBlue = tk.getImage(this.getClass().getResource("resources//entry//ground_blue.png"));
		groundRed = tk.getImage(this.getClass().getResource("resources//entry//ground_red.png"));
		ring1 = tk.getImage(this.getClass().getResource("resources//entry//ring1.png"));
		ring2 = tk.getImage(this.getClass().getResource("resources//entry//ring2.png"));
		ring3 = tk.getImage(this.getClass().getResource("resources//entry//ring3.png"));
		ring4 = tk.getImage(this.getClass().getResource("resources//entry//ring4.png"));
		ring5 = tk.getImage(this.getClass().getResource("resources//entry//ring5.png"));
		editor = tk.getImage(this.getClass().getResource("resources//entry//editor.png"));
		bomb= tk.getImage(this.getClass().getResource("resources//player//bomb.png"));
		for (int i = 1; i < 4; i++) {
			playerUp.add(tk.getImage(this.getClass().getResource("resources//player//player_" + i + ".png")));
			playerDown.add(tk.getImage(this.getClass().getResource("resources//player//player_" + (3 + i) + ".png")));
			playerRight.add(tk.getImage(this.getClass().getResource("resources//player//player_" + (6 + i) + ".png")));
			playerLeft.add(tk.getImage(this.getClass().getResource("resources//player//player_" + (9 + i) + ".png")));
		}
		play = tk.getImage(this.getClass().getResource("resources//entry//play.png"));
		new Thread(new Sketcher()).start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintEntryScreen(g);
	}

	private void paintEntryScreen(Graphics g) {
		for (int i = 0; i < (this.getHeight() / DIM_ASSET) + 1; i++) {
			for (int j = 0; j < (this.getWidth() / DIM_ASSET) + 1; j++) {
				int rand = random.nextInt(5) + 1;
				if (rand == 1)
					g.drawImage(ring1, j * DIM_ASSET, i * DIM_ASSET, this);
				if (rand == 2)
					g.drawImage(ring2, j * DIM_ASSET, i * DIM_ASSET, this);
				if (rand == 3)
					g.drawImage(ring3, j * DIM_ASSET, i * DIM_ASSET, this);
				if (rand == 4)
					g.drawImage(ring4, j * DIM_ASSET, i * DIM_ASSET, this);
				if (rand == 5)
					g.drawImage(ring5, j * DIM_ASSET, i * DIM_ASSET, this);
			}
		}
		int midWidth = (int) this.getWidth() / 2;
		int titleWidth = title.getWidth(this);
		int shiftWidth = (this.getWidth() - (64 * world.getDimension())) / 2;
		int shiftHeight = (this.getHeight() - (64 * world.getDimension())) / 2;
		for (int i = 0; i < world.getDimension(); i++) {
			for (int j = 0; j < world.getDimension(); j++) {
				if (world.getBlockMatrix()[i][j].getColor() == Color.GREY && !world.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(ground, (j + shiftWidth / 64) * DIM_ASSET,
							((int) (i + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
				if (world.getBlockMatrix()[i][j].getColor() == Color.BLUE && !world.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(groundBlue, (j + shiftWidth / 64) * DIM_ASSET,
							((int) (i + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
				if (world.getBlockMatrix()[i][j].getColor() == Color.RED && !world.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(groundRed, (j + shiftWidth / 64) * DIM_ASSET,
							((int) (i + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
				if (world.getBlockMatrix()[i][j].getColor() == Color.GREEN && !world.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(groundGreen, (j + shiftWidth / 64) * DIM_ASSET,
							((int) (i + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
			}
		}
		g.drawImage(play, this.getWidth() - (play.getWidth(this) * 2), (play.getHeight(this) * 2), this);
		g.drawImage(editor, this.getWidth() - (editor.getWidth(this) * 2), (editor.getHeight(this) * 3) + 15, this);
		if(contUpdate==NUM_STEP) {
			for(int i=0;i<initPosition.size();i++) {
				initPosition.set(i, new Position(players.get(i).getX(),players.get(i).getY()));
			}
			gmanager.updateAll();
		}
		contUpdate=(contUpdate+1)%17;
		for(int i=0;i<players.size();i++) {
			ArrayList<Position> bombPosition=players.get(i).getBombPosition();
			for(int j=0;j<bombPosition.size();j++) {
				g.drawImage(bomb, ((bombPosition.get(j).getX())+ shiftWidth / DIM_ASSET)* DIM_ASSET,((int) (bombPosition.get(j).getY() + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
			}
			gmanager.tryToExplodeAll(EXPLOSION);
		}
		drawPlayer(players.get(0), g , shiftHeight , shiftWidth);
		drawPlayer(players.get(1), g , shiftHeight , shiftWidth);
		drawPlayer(players.get(2), g , shiftHeight , shiftWidth);
		g.drawImage(title, midWidth - titleWidth / 2, 0, this);
	}
	
	private void drawPlayer(Player p , Graphics g , int localHeight , int localWidth) {
		int posPlayer=players.indexOf(p);
		if(initPosition.get(posPlayer).equals(p.getPosition())) {
			if(p.getState()==Status.UP) g.drawImage(playerUp.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
			if(p.getState()==Status.DOWN) g.drawImage(playerDown.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
			if(p.getState()==Status.RIGHT) g.drawImage(playerRight.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
			if(p.getState()==Status.LEFT) g.drawImage(playerLeft.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
		    contAnimation.set(posPlayer, 0);
		}
		else {
			if(initPosition.get(posPlayer).getX()-p.getX()<0) {
				p.setState(Status.RIGHT);
				g.drawImage(playerRight.get(contAnimation.get(posPlayer)+1), ((initPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET)+contUpdate*SPEED,((int) (initPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
				contAnimation.set(posPlayer, (contAnimation.get(posPlayer)+1)%(playerRight.size()-1));
			}
			if(initPosition.get(posPlayer).getX()-p.getX()>0) {
				p.setState(Status.LEFT);
				g.drawImage(playerLeft.get(contAnimation.get(posPlayer)+1), ((initPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET)-contUpdate*SPEED,((int) (initPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
				contAnimation.set(posPlayer, (contAnimation.get(posPlayer)+1)%(playerRight.size()-1));
			}
			if(initPosition.get(posPlayer).getY()-p.getY()<0) {
				p.setState(Status.DOWN);
				g.drawImage(playerDown.get(contAnimation.get(posPlayer)+1), ((initPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET),(((int) (initPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET)+contUpdate*SPEED, this);
				contAnimation.set(posPlayer, (contAnimation.get(posPlayer)+1)%(playerRight.size()-1));
			}
			if(initPosition.get(posPlayer).getY()-p.getY()>0) {
				p.setState(Status.UP);
				g.drawImage(playerUp.get(contAnimation.get(posPlayer)+1), ((initPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET),(((int) (initPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET)-contUpdate*SPEED, this);
				contAnimation.set(posPlayer, (contAnimation.get(posPlayer)+1)%(playerRight.size()-1));
			}
		}
	}

	private class Sketcher implements Runnable {

		private static final int FPS = 60;

		public void run() {
			while (true) {
				try {
					TimeUnit.MILLISECONDS.sleep(FPS);
					GamePanel.this.repaint();
				} catch (InterruptedException e) {
				}
			}
		}

	}
}