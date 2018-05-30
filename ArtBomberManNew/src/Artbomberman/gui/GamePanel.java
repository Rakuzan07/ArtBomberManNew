package Artbomberman.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Artbomberman.logic.Color;
import Artbomberman.logic.GameReader;
import Artbomberman.logic.Gmanager;
import Artbomberman.logic.Player;
import Artbomberman.logic.Player.Status;
import Artbomberman.logic.Position;
import Artbomberman.logic.World;

public class GamePanel extends JPanel implements KeyListener {

	private static final int DIM_ASSET = 64;

	private static final int INIT_WORLD_DIM = 13, NUM_STEP = 16, SPEED = 4, IDLE = 0, EXPLOSION = 48, DEMO_SCREEN = 0,
			PLAY_SCREEN = 1, EDITOR_SCREEN = 2, SELECT_WORLD_SCREEN = 3, NUM_WORLDS = 15;

	private ArrayList<Player> demoPlayers, players;

	private World demoWorld, world;

	private Gmanager demoGmanager, Gmanager;

	private GameReader gamereader = new GameReader();

	private Player winningPlayer = null;

	private Toolkit tk;

	private int screenStatus, contSelectionBlock = 0, contBlock = 0;

	private int contUpdate = NUM_STEP - 1, update = NUM_STEP - 1, contMovement = 0;

	private boolean isPressed = false;

	private ArrayList<Integer> contAnimation = new ArrayList<Integer>();

	private Image title, ground, groundGreen, groundRed, groundBlue, ring1, ring2, ring3, ring4, ring5, editor, play,
			bomb, backGroundEditor, editorTitle, up, down, save, clear, load, faceBlue, faceGreen, faceRed, minor,
			major, one, two, three, zero, five, gameOver, win, home, oneRed, twoRed, threeRed, zeroRed, fourRed,
			bombText;

	private static final int SHIFT_PLAY = 2, SHIFT_EDITOR = 3, TABLE_EDITOR = 7, BLUE = 0, GREEN = 1, RED = 2;

	private static final int ENTER_KEY = 10, RIGHT_KEY = 39, LEFT_KEY = 37, UP_KEY = 38, DOWN_KEY = 40, ESC_KEY = 27,
			BOMB_KEY = 32;

	private ArrayList<Image> playerUpGreen = new ArrayList<Image>();

	private ArrayList<Image> playerDownGreen = new ArrayList<Image>();

	private ArrayList<Image> playerLeftGreen = new ArrayList<Image>();

	private ArrayList<Image> playerRightGreen = new ArrayList<Image>();

	private ArrayList<Image> playerUpBlue = new ArrayList<Image>();

	private ArrayList<Image> playerDownBlue = new ArrayList<Image>();

	private ArrayList<Image> playerLeftBlue = new ArrayList<Image>();

	private ArrayList<Image> playerRightBlue = new ArrayList<Image>();

	private ArrayList<Image> playerUpRed = new ArrayList<Image>();

	private ArrayList<Image> playerDownRed = new ArrayList<Image>();

	private ArrayList<Image> playerLeftRed = new ArrayList<Image>();

	private ArrayList<Image> playerRightRed = new ArrayList<Image>();

	private ArrayList<Position> demoinitPosition = new ArrayList<Position>();

	private ArrayList<Position> initPosition = new ArrayList<Position>();

	private ArrayList<Image> selectedBlocks = new ArrayList<Image>();

	private ArrayList<Image> blocks = new ArrayList<Image>();

	private ArrayList<Image> worlds = new ArrayList<Image>();

	private int keyPressed = 0, tickCount = 0, contForRealPlayer = 0;;

	private boolean moved = false;

	private int blockX, blockY;
	private Random random = new Random();

	public GamePanel() {
		super();
		/*
		 * INIZIO PARTE COMMENTATA
		 * Nel costruttore , imposto lo stato del JPanel a DEMO_SCREEN per indicare che quando avvio il gioco voglio vedere il demo 
		 * L'oggetto gamereader mi serve per effettuare le operazioni di input/output ossia salvare su un file di testo il mondo o ricaricarlo una volta salvato
		 * i metodi sono load per caricarlo e save per salvarlo .
		 * Una volta invocato il metodo load su gamereader , riempio le variabili dell'oggetto gamereader (vedi la classe gamereader ) e per passare il	 
		 * il mondo a demoWorld (*) e players (*) utilizzo i metodi get di gamereader ;
		 * contAnimation è un array di contatori che viene usato per simulare i movimenti dei personaggi ( indica quale immagine mostrare su schermo in base
		 * al contatore , ogni indice della lista è collegato ad un giocatore , indice 0 per il contAnimazione del personaggio0) 
		 */
		screenStatus = DEMO_SCREEN;
		demoPlayers = new ArrayList<Player>();
		try {
			gamereader.load(urlToString(this.getClass().getResource("saveData//demoWorld.txt").getPath()));
			demoPlayers = gamereader.getPlayers(); //(*)
			for (int i = 0; i < demoPlayers.size(); i++) {
				demoinitPosition.add(new Position(demoPlayers.get(i).getX(), demoPlayers.get(i).getY()));
			}
			demoWorld = gamereader.getWorld(); //(*)
			demoGmanager = new Gmanager(demoPlayers, demoWorld);
			demoPlayers.get(0).setState(Status.DOWN);
			demoPlayers.get(1).setState(Status.DOWN);
			demoPlayers.get(2).setState(Status.UP);
		} catch (IOException e) {
		}
		contAnimation.add(0);
		contAnimation.add(0);
		contAnimation.add(0);
		/*
		 * FINE PARTE COMMENTATA
		 */
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
		bomb = tk.getImage(this.getClass().getResource("resources//player//bomb.png"));
		editorTitle = tk.getImage(this.getClass().getResource("var//editor.png"));
		backGroundEditor = tk.getImage(this.getClass().getResource("block//editor_table.png"));
		up = tk.getImage(this.getClass().getResource("var//UP.png"));
		down = tk.getImage(this.getClass().getResource("var//DOWN.png"));
		save = tk.getImage(this.getClass().getResource("var//save.png"));
		load = tk.getImage(this.getClass().getResource("var//load.png"));
		clear = tk.getImage(this.getClass().getResource("var//clear.png"));
		faceRed = tk.getImage(this.getClass().getResource("var//playerFaceRed.png"));
		faceGreen = tk.getImage(this.getClass().getResource("var//playerFaceGreen.png"));
		faceBlue = tk.getImage(this.getClass().getResource("var//playerFaceBlue.png"));
		minor = tk.getImage(this.getClass().getResource("var//minor.png"));
		major = tk.getImage(this.getClass().getResource("var//major.png"));
		one = tk.getImage(this.getClass().getResource("var//number1.png"));
		two = tk.getImage(this.getClass().getResource("var//number2.png"));
		three = tk.getImage(this.getClass().getResource("var//number3.png"));
		five = tk.getImage(this.getClass().getResource("var//number5.png"));
		zero = tk.getImage(this.getClass().getResource("var//number0.png"));
		oneRed = tk.getImage(this.getClass().getResource("var//number1red.png"));
		twoRed = tk.getImage(this.getClass().getResource("var//number2red.png"));
		threeRed = tk.getImage(this.getClass().getResource("var//number3red.png"));
		fourRed = tk.getImage(this.getClass().getResource("var//number4red.png"));
		bombText = tk.getImage(this.getClass().getResource("var//bombText.png"));
		zeroRed = tk.getImage(this.getClass().getResource("var//number0red.png"));
		home = tk.getImage(this.getClass().getResource("var//home.png"));
		win = tk.getImage(this.getClass().getResource("var//Win.png"));
		gameOver = tk.getImage(this.getClass().getResource("var//textGameOver.png"));
		selectedBlocks.add(tk.getImage(this.getClass().getResource("block//block_BlueSelected.png")));
		selectedBlocks.add(tk.getImage(this.getClass().getResource("block//block_GreenSelected.png")));
		selectedBlocks.add(tk.getImage(this.getClass().getResource("block//block_RedSelected.png")));
		blocks.add(tk.getImage(this.getClass().getResource("block//block_Blue.png")));
		blocks.add(tk.getImage(this.getClass().getResource("block//block_Green.png")));
		blocks.add(tk.getImage(this.getClass().getResource("block//block_Red.png")));
		for (int i = 0; i < NUM_WORLDS; i++) {
			worlds.add(tk.getImage(this.getClass().getResource("Worlds//world" + (i + 1) + ".png")));
		}
		for (int i = 1; i < 4; i++) {
			playerUpGreen.add(tk.getImage(this.getClass().getResource("resources//player//player_" + i + "green.png")));
			playerDownGreen.add(
					tk.getImage(this.getClass().getResource("resources//player//player_" + (3 + i) + "green.png")));
			playerRightGreen.add(
					tk.getImage(this.getClass().getResource("resources//player//player_" + (6 + i) + "green.png")));
			playerLeftGreen.add(
					tk.getImage(this.getClass().getResource("resources//player//player_" + (9 + i) + "green.png")));
			playerUpBlue.add(tk.getImage(this.getClass().getResource("resources//player//player_" + i + "blue.png")));
			playerDownBlue
					.add(tk.getImage(this.getClass().getResource("resources//player//player_" + (3 + i) + "blue.png")));
			playerRightBlue
					.add(tk.getImage(this.getClass().getResource("resources//player//player_" + (6 + i) + "blue.png")));
			playerLeftBlue
					.add(tk.getImage(this.getClass().getResource("resources//player//player_" + (9 + i) + "blue.png")));
			playerUpRed.add(tk.getImage(this.getClass().getResource("resources//player//player_" + i + "red.png")));
			playerDownRed
					.add(tk.getImage(this.getClass().getResource("resources//player//player_" + (3 + i) + "red.png")));
			playerRightRed
					.add(tk.getImage(this.getClass().getResource("resources//player//player_" + (6 + i) + "red.png")));
			playerLeftRed
					.add(tk.getImage(this.getClass().getResource("resources//player//player_" + (9 + i) + "red.png")));
		}
		play = tk.getImage(this.getClass().getResource("resources//entry//play.png"));
		this.setEventManager();
		this.addKeyListener(this);
		new Thread(new Sketcher()).start();
	}

	public static String urlToString(String s) {
		if(!s.substring(0, 1).equals("/")) return s;
		StringTokenizer st = new StringTokenizer(s, "/");
		String string = "";
		while (st.hasMoreTokens()) {
			string = string + st.nextToken() + "//";
		}
		return string.substring(0, string.length() - 2);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (screenStatus == DEMO_SCREEN) {
			paintEntryScreen(g);
		}
		if (screenStatus == EDITOR_SCREEN) {
			paintEditorScreen(g);
		}
		if (screenStatus == PLAY_SCREEN) {
			paintPlayScreen(g);
		}
		if (screenStatus == SELECT_WORLD_SCREEN) {
			paintSelectWorldScreen(g);
		}
	}

	private void setEventManager() {

		this.addMouseListener(new MouseAdapter() {
			
          /*
           * 
           */
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if (screenStatus == DEMO_SCREEN) {
					int playX = GamePanel.this.getWidth() - (play.getWidth(GamePanel.this) * SHIFT_PLAY);
					int playY = play.getHeight(GamePanel.this) * SHIFT_PLAY;
					int editorX = GamePanel.this.getWidth() - (play.getWidth(GamePanel.this) * SHIFT_PLAY);
					int editorY = play.getHeight(GamePanel.this) * SHIFT_EDITOR;
					if (x >= playX && x <= (playX + play.getWidth(GamePanel.this)) && y >= playY
							&& y <= playY + play.getHeight(GamePanel.this)) {
						screenStatus = SELECT_WORLD_SCREEN;
						winningPlayer=null;
					}
					if (x >= editorX && x <= (editorX + play.getWidth(GamePanel.this)) && y >= editorY
							&& y <= editorY + play.getHeight(GamePanel.this)) {
						screenStatus = EDITOR_SCREEN;
						initPosition.clear();
						players = new ArrayList<Player>();
						players.add(new Player(Color.BLUE, new Position(0, 0)));
						players.add(new Player(Color.GREEN, new Position(INIT_WORLD_DIM - 1, 0)));
						players.add(new Player(Color.RED, new Position(INIT_WORLD_DIM / 2, INIT_WORLD_DIM - 1)));
						players.get(0).setState(Status.DOWN);
						players.get(1).setState(Status.DOWN);
						players.get(2).setState(Status.UP);
						world = new World(players, INIT_WORLD_DIM);
						for (int i = 0; i < players.size(); i++) {
							initPosition.add(new Position(players.get(i).getX(), players.get(i).getY()));
						}
						Gmanager = new Gmanager(players, world);
					}
				} else if (screenStatus == EDITOR_SCREEN) {
					int upX = DIM_ASSET * 4;
					int upY = DIM_ASSET * 3;
					int downX = DIM_ASSET * 4;
					int downY = DIM_ASSET * 7;
					int clearX = (TABLE_EDITOR / 2) * DIM_ASSET;
					int clearY = (TABLE_EDITOR + 3) * DIM_ASSET;
					int saveX = 23 * DIM_ASSET;
					int saveY = DIM_ASSET;
					int loadX = 23 * DIM_ASSET;
					int loadY = (3 * DIM_ASSET) + 12;
					int playX = 23 * DIM_ASSET;
					int playY = (5 * DIM_ASSET) + 24;
					if (x >= upX && x <= (upX + up.getWidth(GamePanel.this)) && y >= upY
							&& y <= upY + up.getHeight(GamePanel.this)) {
						if (contSelectionBlock - 1 < 0)
							contSelectionBlock = selectedBlocks.size() - 1;
						else
							contSelectionBlock = (contSelectionBlock - 1) % selectedBlocks.size();
					} else if (x >= downX && x <= (downX + up.getWidth(GamePanel.this)) && y >= downY
							&& y <= downY + up.getHeight(GamePanel.this)) {
						contSelectionBlock = (contSelectionBlock + 1) % selectedBlocks.size();
					} else if (x >= clearX && x <= (clearX + clear.getWidth(GamePanel.this)) && y >= clearY
							&& y <= clearY + clear.getHeight(GamePanel.this)) {
						world.clear();
					} else if (x >= saveX && x <= (saveX + save.getWidth(GamePanel.this)) && y >= saveY
							&& y <= saveY + save.getHeight(GamePanel.this)) {
						JFileChooser jfc = new JFileChooser();
						int val = jfc.showSaveDialog(GamePanel.this);
						if (val == JFileChooser.APPROVE_OPTION) {
							String pathName = jfc.getSelectedFile().getAbsolutePath();
							try {
								gamereader.save(pathName, players, world);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						} else if (val == JFileChooser.CANCEL_OPTION) {
							JOptionPane.showMessageDialog(GamePanel.this, "Hai annullato il salvataggio del file !");
						}
					} else if (x >= loadX && x <= (loadX + load.getWidth(GamePanel.this)) && y >= loadY
							&& y <= loadY + load.getHeight(GamePanel.this)) {
						JFileChooser jfc = new JFileChooser();
						int val = jfc.showOpenDialog(GamePanel.this);
						if (val == JFileChooser.APPROVE_OPTION) {
							String pathName = jfc.getSelectedFile().getAbsolutePath();
							try {
								gamereader.load(pathName);
								players = gamereader.getPlayers();
								initPosition.clear();
								for (int i = 0; i < players.size(); i++) {
									initPosition.add(new Position(players.get(i).getX(), players.get(i).getY()));
								}
								world = gamereader.getWorld();
								Gmanager = new Gmanager(players, world);
								players.get(0).setState(Status.DOWN);
								players.get(1).setState(Status.DOWN);
								players.get(2).setState(Status.UP);
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(GamePanel.this,
										"Hai selezionato un file di salvataggio non valido !");
							}
						} else if (val == JFileChooser.CANCEL_OPTION) {
							JOptionPane.showMessageDialog(GamePanel.this, "Hai annullato il caricamento del file !");
						}
					} else if (x >= playX && x <= (playX + play.getWidth(GamePanel.this)) && y >= playY
							&& y <= playY + play.getHeight(GamePanel.this)) {
						screenStatus = PLAY_SCREEN;
						contUpdate = NUM_STEP - 1;
						contForRealPlayer=0;
						winningPlayer = null;
					}

				}

				if (screenStatus == PLAY_SCREEN) {
					int homeX = 22 * DIM_ASSET;
					int homeY = 2 * DIM_ASSET;
					if (x >= homeX && x <= (homeX + home.getWidth(GamePanel.this)) && y >= homeY
							&& y <= homeY + home.getHeight(GamePanel.this))
						screenStatus = DEMO_SCREEN;
				}
				
				if(screenStatus==SELECT_WORLD_SCREEN) {
					int numImageforCol=3;
					int numImageforRow=5;
					for (int i = 0; i < numImageforCol; i++) {
						for (int j = 0; j < numImageforRow; j++) {
							int worldX=(4*DIM_ASSET*(j+1))+DIM_ASSET*2;
							int worldY=(3*DIM_ASSET*(i+1));
							if(x>=worldX&&x<=(worldX + worlds.get(i).getWidth(GamePanel.this))&&y >= worldY
									&& y <= worldY + worlds.get(i).getHeight(GamePanel.this)) {
								loadWorld(i*5+j);
								screenStatus = PLAY_SCREEN;
								contUpdate = NUM_STEP - 1;
								contForRealPlayer=0;
								winningPlayer = null;
								return;
							}
						}
					}
				}
			}

		});

	}
	
	private void loadWorld(int numWorld) {
		try {
			gamereader.load(urlToString(this.getClass().getResource("saveData//world"+numWorld+".txt").getPath()));
		}catch(IOException e) {}
		initPosition.clear();
		players = gamereader.getPlayers();
		players.get(0).setState(Status.DOWN);
		players.get(1).setState(Status.DOWN);
		players.get(2).setState(Status.UP);
		world = gamereader.getWorld();
		for (int i = 0; i < players.size(); i++) {
			initPosition.add(new Position(players.get(i).getX(), players.get(i).getY()));
		}
		Gmanager = new Gmanager(players, world);
	}

	private void paintEditorScreen(Graphics g) {
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
		for (int i = 0; i < TABLE_EDITOR + 1; i++) {
			for (int j = 0; j < TABLE_EDITOR; j++) {
				g.drawImage(backGroundEditor, (j + 1) * DIM_ASSET, (i + 1) * DIM_ASSET, this);
			}
		}
		g.drawImage(editorTitle, 3 * DIM_ASSET + 12, DIM_ASSET, this);
		g.drawImage(up, DIM_ASSET * 4, DIM_ASSET * 3, this);
		g.drawImage(selectedBlocks.get(contSelectionBlock), DIM_ASSET * 4, DIM_ASSET * 5, this);
		g.drawImage(down, DIM_ASSET * 4, DIM_ASSET * 7, this);
		for (int i = 0; i < world.getDimension(); i++) {
			for (int j = 0; j < world.getDimension(); j++) {
				if (world.getBlockMatrix()[i][j].getColor() == Color.GREY && !world.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(ground, (9 + j) * DIM_ASSET, (i + 1) * DIM_ASSET, this);
				if (world.getBlockMatrix()[i][j].getColor() == Color.BLUE && world.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(blocks.get(BLUE), (9 + j) * DIM_ASSET, (i + 1) * DIM_ASSET, this);
				if (world.getBlockMatrix()[i][j].getColor() == Color.RED && world.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(blocks.get(RED), (9 + j) * DIM_ASSET, (i + 1) * DIM_ASSET, this);
				if (world.getBlockMatrix()[i][j].getColor() == Color.GREEN && world.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(blocks.get(GREEN), (9 + j) * DIM_ASSET, (i + 1) * DIM_ASSET, this);
			}
		}
		g.drawImage(playerDownBlue.get(0), 9 * DIM_ASSET, 1 * DIM_ASSET, this);
		g.drawImage(playerDownGreen.get(0), 21 * DIM_ASSET, 1 * DIM_ASSET, this);
		g.drawImage(playerUpRed.get(2), (9 + demoWorld.getDimension() / 2) * DIM_ASSET, 13 * DIM_ASSET, this);
		g.drawImage(clear, (TABLE_EDITOR / 2) * DIM_ASSET, (TABLE_EDITOR + 3) * DIM_ASSET, this);
		g.drawImage(save, 23 * DIM_ASSET, DIM_ASSET, this);
		g.drawImage(load, 23 * DIM_ASSET, (3 * DIM_ASSET) + 12, this);
		g.drawImage(play, 23 * DIM_ASSET, (5 * DIM_ASSET) + 24, this);
		if (keyPressed == ENTER_KEY) {
			if (tickCount == 1) {
				g.drawImage(blocks.get(contBlock), (blockX + 9) * DIM_ASSET, (blockY + 1) * DIM_ASSET, this);
			}
		}
		if (keyPressed == LEFT_KEY) {
			boolean find = true;
			if (!moved) {
				blockX--;
				if (blockX < 0) {
					blockX = blockX + world.getDimension();
				}
				if ((blockX == players.get(BLUE).getX() && blockY == players.get(BLUE).getY())
						|| (blockX == players.get(RED).getX() && blockY == players.get(RED).getY())
						|| (blockX == players.get(GREEN).getX() && blockY == players.get(GREEN).getY())) {
					while (find) {
						if ((blockX == players.get(BLUE).getX() && blockY == players.get(BLUE).getY())
								|| (blockX == players.get(RED).getX() && blockY == players.get(RED).getY())
								|| (blockX == players.get(GREEN).getX() && blockY == players.get(GREEN).getY())) {
							blockX = blockX - 1;
						}
						if (blockX < 0)
							blockX = blockX + (world.getDimension());
						find = ((blockX == players.get(BLUE).getX() && blockY == players.get(BLUE).getY())
								|| (blockX == players.get(RED).getX() && blockY == players.get(RED).getY())
								|| (blockX == players.get(GREEN).getX() && blockY == players.get(GREEN).getY()));
					}
				}
				moved = true;
			} else
				g.drawImage(blocks.get(contBlock), (blockX + 9) * DIM_ASSET, (blockY + 1) * DIM_ASSET, this);
		}
		if (keyPressed == RIGHT_KEY) {
			boolean find = true;
			if (!moved) {
				blockX++;
				if (blockX >= world.getDimension()) {
					blockX = blockX - world.getDimension();
				}
				if ((blockX == players.get(BLUE).getX() && blockY == players.get(BLUE).getY())
						|| (blockX == players.get(RED).getX() && blockY == players.get(RED).getY())
						|| (blockX == players.get(GREEN).getX() && blockY == players.get(GREEN).getY())) {
					while (find) {
						if ((blockX == players.get(BLUE).getX() && blockY == players.get(BLUE).getY())
								|| (blockX == players.get(RED).getX() && blockY == players.get(RED).getY())
								|| (blockX == players.get(GREEN).getX() && blockY == players.get(GREEN).getY())) {
							blockX = blockX + 1;
						}
						if (blockX >= world.getDimension())
							blockX = blockX - (world.getDimension());
						find = ((blockX == players.get(BLUE).getX() && blockY == players.get(BLUE).getY())
								|| (blockX == players.get(RED).getX() && blockY == players.get(RED).getY())
								|| (blockX == players.get(GREEN).getX() && blockY == players.get(GREEN).getY()));
					}
				}
				moved = true;

			} else
				g.drawImage(blocks.get(contBlock), (blockX + 9) * DIM_ASSET, (blockY + 1) * DIM_ASSET, this);
		}
		if (keyPressed == UP_KEY) {
			boolean find = true;
			if (!moved) {
				blockY--;
				if (blockY < 0) {
					blockY = world.getDimension() - 1;
				}
				if ((blockY == players.get(BLUE).getY() && blockX == players.get(BLUE).getX())
						|| (blockY == players.get(RED).getY() && blockX == players.get(RED).getX())
						|| (blockY == players.get(GREEN).getY() && blockX == players.get(GREEN).getX())) {
					while (find) {
						if ((blockY == players.get(BLUE).getY() && blockX == players.get(BLUE).getX())
								|| (blockY == players.get(RED).getY() && blockX == players.get(RED).getX())
								|| (blockY == players.get(GREEN).getY() && blockX == players.get(GREEN).getX())) {
							blockY = blockY - 1;
						}
						if (blockY < 0)
							blockY = blockY + (world.getDimension());
						find = ((blockY == players.get(BLUE).getY() && blockX == players.get(BLUE).getX())
								|| (blockY == players.get(RED).getY() && blockX == players.get(RED).getX())
								|| (blockY == players.get(GREEN).getY() && blockX == players.get(GREEN).getX()));
					}
				}
				moved = true;
			} else
				g.drawImage(blocks.get(contBlock), (blockX + 9) * DIM_ASSET, (blockY + 1) * DIM_ASSET, this);
		}
		if (keyPressed == DOWN_KEY) {
			boolean find = true;
			if (!moved) {
				blockY++;
				if (blockY >= world.getDimension()) {
					blockY = blockY - world.getDimension();
				}
				if ((blockY == players.get(BLUE).getY() && blockX == players.get(BLUE).getX())
						|| (blockY == players.get(RED).getY() && blockX == players.get(RED).getX())
						|| (blockY == players.get(GREEN).getY() && blockX == players.get(GREEN).getX())) {
					while (find) {
						if ((blockY == players.get(BLUE).getY() && blockX == players.get(BLUE).getX())
								|| (blockY == players.get(RED).getY() && blockX == players.get(RED).getX())
								|| (blockY == players.get(GREEN).getY() && blockX == players.get(GREEN).getX())) {
							blockY = blockY + 1;
						}
						if (blockY >= world.getDimension())
							blockY = blockY - (world.getDimension());
						find = ((blockY == players.get(BLUE).getY() && blockX == players.get(BLUE).getX())
								|| (blockY == players.get(RED).getY() && blockX == players.get(RED).getX())
								|| (blockY == players.get(GREEN).getY() && blockX == players.get(GREEN).getX()));
					}
				}
				moved = true;
			} else
				g.drawImage(blocks.get(contBlock), (blockX + 9) * DIM_ASSET, (blockY + 1) * DIM_ASSET, this);
		}

	}

	private void paintPlayScreen(Graphics g) {
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
				if (world.getBlockMatrix()[i][j].getColor() == Color.GREEN
						&& !world.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(groundGreen, (j + shiftWidth / 64) * DIM_ASSET,
							((int) (i + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
				if (world.getBlockMatrix()[i][j].getColor() == Color.BLUE && world.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(blocks.get(BLUE), (j + shiftWidth / 64) * DIM_ASSET,
							((int) (i + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
				if (world.getBlockMatrix()[i][j].getColor() == Color.RED && world.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(blocks.get(RED), (j + shiftWidth / 64) * DIM_ASSET,
							((int) (i + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
				if (world.getBlockMatrix()[i][j].getColor() == Color.GREEN && world.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(blocks.get(GREEN), (j + shiftWidth / 64) * DIM_ASSET,
							((int) (i + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
			}
		}
		g.drawImage(home, 22 * DIM_ASSET, 2 * DIM_ASSET, this);
		if (winningPlayer == null) {
			if (contUpdate == NUM_STEP) {
				for (int i = 0; i < initPosition.size() - 1; i++) {
					initPosition.set(i, new Position(players.get(i).getX(), players.get(i).getY()));
				}
				if(players.get(0).getInkTank()>0||players.get(0).getInkTank()==0&&world.checkColor(players.get(0).getColor()))Gmanager.update(players.get(0));
				if(players.get(1).getInkTank()>0||players.get(1).getInkTank()==0&&world.checkColor(players.get(1).getColor()))Gmanager.update(players.get(1));
			}
			contUpdate = (contUpdate + 1) % 17;
			for (int i = 0; i < players.size(); i++) {
				ArrayList<Position> bombPosition = players.get(i).getBombPosition();
				for (int j = 0; j < bombPosition.size(); j++) {
					g.drawImage(bomb, ((bombPosition.get(j).getX()) + shiftWidth / DIM_ASSET) * DIM_ASSET,
							((int) (bombPosition.get(j).getY() + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET,
							this);
				}
			}
			if (contForRealPlayer == NUM_STEP) {
				initPosition.set(2, new Position(players.get(2).getX(), players.get(2).getY()));
				isPressed = false;
				contForRealPlayer = 0;
			}
			if (isPressed)
				contForRealPlayer = (contForRealPlayer + 1);
			if (keyPressed == LEFT_KEY) {
				Gmanager.tryToMoveLeft(players.get(2));
				keyPressed = 0;
			}
			if (keyPressed == RIGHT_KEY) {
				Gmanager.tryToMoveRight(players.get(2));
				keyPressed = 0;
			}
			if (keyPressed == UP_KEY) {
				Gmanager.tryToMoveUp(players.get(2));
				keyPressed = 0;
			}
			if (keyPressed == DOWN_KEY) {
				Gmanager.tryToMoveDown(players.get(2));
				keyPressed = 0;
			}
			if (keyPressed == BOMB_KEY) {
				players.get(2).placeBomb(players.get(2).getPosition());
				keyPressed = 0;
				isPressed = false;
			}
			Gmanager.tryToReloadTank(players.get(2), initPosition.get(2));
			Gmanager.tryToExplodeAll(EXPLOSION);
		}
		drawPlayer(players.get(0), g, shiftHeight, shiftWidth, initPosition, contUpdate);
		drawPlayer(players.get(1), g, shiftHeight, shiftWidth, initPosition, contUpdate);
		drawPlayer(players.get(2), g, shiftHeight, shiftWidth, initPosition, contForRealPlayer);
		drawPlayerPercentual(players.get(0), 3, 3, g);
		drawPlayerPercentual(players.get(1), 3, 5, g);
		drawPlayerPercentual(players.get(2), 3, 7, g);
		drawBombText(players.get(2), 1, 9, g);
		if (winningPlayer != null) {
			if (winningPlayer.equals(players.get(2)))
				g.drawImage(win, (((world.getDimension() / 4) + (shiftWidth / 64)) * DIM_ASSET) + 32,
						((int) (world.getDimension() / 2 + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
			else
				g.drawImage(gameOver, (((world.getDimension() / 4) + (shiftWidth / 64)) * DIM_ASSET) + 32,
						((int) (world.getDimension() / 2 + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
		}
	}

	private void paintSelectWorldScreen(Graphics g) {
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
		drawWorlds(4,3,g);
	}

	
	private void drawWorlds(int x, int y,Graphics g) {
		int numImageforRow = 5;
		int numImageforCol = 3;
		int cont=0;
		for (int i = 0; i < numImageforCol; i++) {
			for (int j = 0; j < numImageforRow; j++) {
				g.drawImage(worlds.get(cont),(x*DIM_ASSET*(j+1))+DIM_ASSET*2,(y*DIM_ASSET*(i+1)),this);
				cont++;
			}
		}
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
		int shiftWidth = (this.getWidth() - (64 * demoWorld.getDimension())) / 2;
		int shiftHeight = (this.getHeight() - (64 * demoWorld.getDimension())) / 2;
		for (int i = 0; i < demoWorld.getDimension(); i++) {
			for (int j = 0; j < demoWorld.getDimension(); j++) {
				if (demoWorld.getBlockMatrix()[i][j].getColor() == Color.GREY
						&& !demoWorld.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(ground, (j + shiftWidth / 64) * DIM_ASSET,
							((int) (i + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
				if (demoWorld.getBlockMatrix()[i][j].getColor() == Color.BLUE
						&& !demoWorld.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(groundBlue, (j + shiftWidth / 64) * DIM_ASSET,
							((int) (i + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
				if (demoWorld.getBlockMatrix()[i][j].getColor() == Color.RED
						&& !demoWorld.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(groundRed, (j + shiftWidth / 64) * DIM_ASSET,
							((int) (i + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
				if (demoWorld.getBlockMatrix()[i][j].getColor() == Color.GREEN
						&& !demoWorld.getBlockMatrix()[i][j].isPhysical())
					g.drawImage(groundGreen, (j + shiftWidth / 64) * DIM_ASSET,
							((int) (i + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
			}
		}
		g.drawImage(play, this.getWidth() - (play.getWidth(this) * 2), (play.getHeight(this) * 2), this);
		g.drawImage(editor, this.getWidth() - (editor.getWidth(this) * 2), (editor.getHeight(this) * 3) + 15, this);
		if (contUpdate == NUM_STEP) {
			for (int i = 0; i < demoinitPosition.size(); i++) {
				demoinitPosition.set(i, new Position(demoPlayers.get(i).getX(), demoPlayers.get(i).getY()));
			}
			demoGmanager.updateAll();
		}
		contUpdate = (contUpdate + 1) % 17;
		for (int i = 0; i < demoPlayers.size(); i++) {
			ArrayList<Position> bombPosition = demoPlayers.get(i).getBombPosition();
			for (int j = 0; j < bombPosition.size(); j++) {
				g.drawImage(bomb, ((bombPosition.get(j).getX()) + shiftWidth / DIM_ASSET) * DIM_ASSET,
						((int) (bombPosition.get(j).getY() + Math.ceil((double) shiftHeight / 64))) * DIM_ASSET, this);
			}
		}
		demoGmanager.tryToExplodeAll(EXPLOSION);
		drawPlayer(demoPlayers.get(0), g, shiftHeight, shiftWidth, demoinitPosition, contUpdate);
		drawPlayer(demoPlayers.get(1), g, shiftHeight, shiftWidth, demoinitPosition, contUpdate);
		drawPlayer(demoPlayers.get(2), g, shiftHeight, shiftWidth, demoinitPosition, contUpdate);
		g.drawImage(title, midWidth - titleWidth / 2, 0, this);
	}

	private void drawPlayer(Player p, Graphics g, int localHeight, int localWidth, ArrayList<Position> demoinitPosition,
			int contUpdate) {
		int posPlayer = demoPlayers.indexOf(p);
		if (demoinitPosition.get(posPlayer).equals(p.getPosition())) {
			if (p.getState() == Status.UP) {
				if (p.getColor() == Color.GREEN)
					g.drawImage(playerUpGreen.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,
							((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
				if (p.getColor() == Color.BLUE)
					g.drawImage(playerUpBlue.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,
							((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
				if (p.getColor() == Color.RED)
					g.drawImage(playerUpRed.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,
							((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
			}
			if (p.getState() == Status.DOWN) {
				if (p.getColor() == Color.GREEN)
					g.drawImage(playerDownGreen.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,
							((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
				if (p.getColor() == Color.BLUE)
					g.drawImage(playerDownBlue.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,
							((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
				if (p.getColor() == Color.RED)
					g.drawImage(playerDownRed.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,
							((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
			}
			if (p.getState() == Status.RIGHT) {
				if (p.getColor() == Color.GREEN)
					g.drawImage(playerRightGreen.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,
							((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
				if (p.getColor() == Color.BLUE)
					g.drawImage(playerRightBlue.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,
							((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
				if (p.getColor() == Color.RED)
					g.drawImage(playerRightRed.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,
							((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
			}
			if (p.getState() == Status.LEFT) {
				if (p.getColor() == Color.GREEN)
					g.drawImage(playerLeftGreen.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,
							((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
				if (p.getColor() == Color.BLUE)
					g.drawImage(playerLeftBlue.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,
							((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
				if (p.getColor() == Color.RED)
					g.drawImage(playerLeftRed.get(IDLE), (p.getX() + localWidth / 64) * DIM_ASSET,
							((int) (p.getY() + Math.ceil((double) localHeight / 64))) * DIM_ASSET, this);
			}
			contAnimation.set(posPlayer, 0);
		} else {
			if (demoinitPosition.get(posPlayer).getX() - p.getX() < 0) {
				p.setState(Status.RIGHT);
				if (p.getColor() == Color.GREEN)
					g.drawImage(playerRightGreen.get(contAnimation.get(posPlayer) + 1),
							((demoinitPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET)
									+ contUpdate * SPEED,
							((int) (demoinitPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64)))
									* DIM_ASSET,
							this);
				if (p.getColor() == Color.BLUE)
					g.drawImage(playerRightBlue.get(contAnimation.get(posPlayer) + 1),
							((demoinitPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET)
									+ contUpdate * SPEED,
							((int) (demoinitPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64)))
									* DIM_ASSET,
							this);
				if (p.getColor() == Color.RED)
					g.drawImage(playerRightRed.get(contAnimation.get(posPlayer) + 1),
							((demoinitPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET)
									+ contUpdate * SPEED,
							((int) (demoinitPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64)))
									* DIM_ASSET,
							this);
				contAnimation.set(posPlayer, (contAnimation.get(posPlayer) + 1) % (playerRightGreen.size() - 1));
			}
			if (demoinitPosition.get(posPlayer).getX() - p.getX() > 0) {
				p.setState(Status.LEFT);
				if (p.getColor() == Color.GREEN)
					g.drawImage(playerLeftGreen.get(contAnimation.get(posPlayer) + 1),
							((demoinitPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET)
									- contUpdate * SPEED,
							((int) (demoinitPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64)))
									* DIM_ASSET,
							this);
				if (p.getColor() == Color.BLUE)
					g.drawImage(playerLeftBlue.get(contAnimation.get(posPlayer) + 1),
							((demoinitPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET)
									- contUpdate * SPEED,
							((int) (demoinitPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64)))
									* DIM_ASSET,
							this);
				if (p.getColor() == Color.RED)
					g.drawImage(playerLeftRed.get(contAnimation.get(posPlayer) + 1),
							((demoinitPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET)
									- contUpdate * SPEED,
							((int) (demoinitPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64)))
									* DIM_ASSET,
							this);
				contAnimation.set(posPlayer, (contAnimation.get(posPlayer) + 1) % (playerRightGreen.size() - 1));
			}
			if (demoinitPosition.get(posPlayer).getY() - p.getY() < 0) {
				p.setState(Status.DOWN);
				if (p.getColor() == Color.GREEN)
					g.drawImage(playerDownGreen.get(contAnimation.get(posPlayer) + 1),
							((demoinitPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET),
							(((int) (demoinitPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64)))
									* DIM_ASSET) + contUpdate * SPEED,
							this);
				if (p.getColor() == Color.BLUE)
					g.drawImage(playerDownBlue.get(contAnimation.get(posPlayer) + 1),
							((demoinitPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET),
							(((int) (demoinitPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64)))
									* DIM_ASSET) + contUpdate * SPEED,
							this);
				if (p.getColor() == Color.RED)
					g.drawImage(playerDownRed.get(contAnimation.get(posPlayer) + 1),
							((demoinitPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET),
							(((int) (demoinitPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64)))
									* DIM_ASSET) + contUpdate * SPEED,
							this);
				contAnimation.set(posPlayer, (contAnimation.get(posPlayer) + 1) % (playerRightGreen.size() - 1));
			}
			if (demoinitPosition.get(posPlayer).getY() - p.getY() > 0) {
				p.setState(Status.UP);
				if (p.getColor() == Color.GREEN)
					g.drawImage(playerUpGreen.get(contAnimation.get(posPlayer) + 1),
							((demoinitPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET),
							(((int) (demoinitPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64)))
									* DIM_ASSET) - contUpdate * SPEED,
							this);
				if (p.getColor() == Color.BLUE)
					g.drawImage(playerUpBlue.get(contAnimation.get(posPlayer) + 1),
							((demoinitPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET),
							(((int) (demoinitPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64)))
									* DIM_ASSET) - contUpdate * SPEED,
							this);
				if (p.getColor() == Color.RED)
					g.drawImage(playerUpRed.get(contAnimation.get(posPlayer) + 1),
							((demoinitPosition.get(posPlayer).getX() + localWidth / 64) * DIM_ASSET),
							(((int) (demoinitPosition.get(posPlayer).getY() + Math.ceil((double) localHeight / 64)))
									* DIM_ASSET) - contUpdate * SPEED,
							this);
				contAnimation.set(posPlayer, (contAnimation.get(posPlayer) + 1) % (playerRightGreen.size() - 1));
			}
		}
	}

	private void drawPlayerPercentual(Player p, int x, int y, Graphics g) {
		double percentual = world.checkVictory(p);
		if (p.getColor() == Color.BLUE)
			g.drawImage(faceBlue, x * DIM_ASSET, y * DIM_ASSET, this);
		if (p.getColor() == Color.RED)
			g.drawImage(faceRed, x * DIM_ASSET, y * DIM_ASSET, this);
		if (p.getColor() == Color.GREEN)
			g.drawImage(faceGreen, x * DIM_ASSET, y * DIM_ASSET, this);
		if (percentual < 0.05) {
			g.drawImage(minor, x * DIM_ASSET + DIM_ASSET, y * DIM_ASSET, this);
			g.drawImage(five, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this), y * DIM_ASSET, this);
		} else if (percentual < 0.1) {
			g.drawImage(minor, x * DIM_ASSET + DIM_ASSET, y * DIM_ASSET, this);
			g.drawImage(one, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this), y * DIM_ASSET, this);
			g.drawImage(zero, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this) + one.getWidth(this), y * DIM_ASSET,
					this);
		} else if (percentual < 0.15) {
			g.drawImage(minor, x * DIM_ASSET + DIM_ASSET, y * DIM_ASSET, this);
			g.drawImage(one, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this), y * DIM_ASSET, this);
			g.drawImage(five, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this) + one.getWidth(this), y * DIM_ASSET,
					this);
		} else if (percentual < 0.20) {
			g.drawImage(minor, x * DIM_ASSET + DIM_ASSET, y * DIM_ASSET, this);
			g.drawImage(two, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this), y * DIM_ASSET, this);
			g.drawImage(zero, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this) + two.getWidth(this), y * DIM_ASSET,
					this);
		} else if (percentual < 0.25) {
			g.drawImage(minor, x * DIM_ASSET + DIM_ASSET, y * DIM_ASSET, this);
			g.drawImage(two, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this), y * DIM_ASSET, this);
			g.drawImage(five, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this) + two.getWidth(this), y * DIM_ASSET,
					this);
		} else if (percentual < 0.30) {
			g.drawImage(minor, x * DIM_ASSET + DIM_ASSET, y * DIM_ASSET, this);
			g.drawImage(three, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this), y * DIM_ASSET, this);
			g.drawImage(zero, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this) + three.getWidth(this), y * DIM_ASSET,
					this);
		} else if (percentual < 0.33) {
			g.drawImage(minor, x * DIM_ASSET + DIM_ASSET, y * DIM_ASSET, this);
			g.drawImage(three, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this), y * DIM_ASSET, this);
			g.drawImage(three, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this) + three.getWidth(this), y * DIM_ASSET,
					this);
		} else {
			g.drawImage(major, x * DIM_ASSET + DIM_ASSET, y * DIM_ASSET, this);
			g.drawImage(three, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this), y * DIM_ASSET, this);
			g.drawImage(three, x * DIM_ASSET + DIM_ASSET + minor.getWidth(this) + three.getWidth(this), y * DIM_ASSET,
					this);
			winningPlayer = p;
		}
	}

	private void drawBombText(Player p, int x, int y, Graphics g) {
		int bomb = p.getInkTank();
		g.drawImage(bombText, x * DIM_ASSET + DIM_ASSET, y * DIM_ASSET, this);
		if (bomb == 0)
			g.drawImage(zeroRed, x * DIM_ASSET + DIM_ASSET + bombText.getWidth(this), y * DIM_ASSET, this);
		else if (bomb == 1)
			g.drawImage(oneRed, x * DIM_ASSET + DIM_ASSET + bombText.getWidth(this), y * DIM_ASSET, this);
		else if (bomb == 2)
			g.drawImage(twoRed, x * DIM_ASSET + DIM_ASSET + bombText.getWidth(this), y * DIM_ASSET, this);
		else if (bomb == 3)
			g.drawImage(threeRed, x * DIM_ASSET + DIM_ASSET + bombText.getWidth(this), y * DIM_ASSET, this);
		else
			g.drawImage(fourRed, x * DIM_ASSET + DIM_ASSET + bombText.getWidth(this), y * DIM_ASSET, this);
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

	public void keyPressed(KeyEvent e) {
		moved = false;
		if (screenStatus == EDITOR_SCREEN) {
			if (e.getKeyCode() == ENTER_KEY) {
				keyPressed = ENTER_KEY;
				tickCount = (tickCount + 1) % 2;
				if (tickCount == 1) {
					contBlock = contSelectionBlock;
					blockX = 1;
					blockY = 0;
				}
				if (tickCount == 0) {
					if (contBlock == BLUE) {
						world.getBlockMatrix()[blockY][blockX].setColor(Color.BLUE);
						world.getBlockMatrix()[blockY][blockX].setBreakCont(3);
					}
					if (contBlock == RED) {
						world.getBlockMatrix()[blockY][blockX].setColor(Color.RED);
						world.getBlockMatrix()[blockY][blockX].setBreakCont(3);
					}
					if (contBlock == GREEN) {
						world.getBlockMatrix()[blockY][blockX].setColor(Color.GREEN);
						world.getBlockMatrix()[blockY][blockX].setBreakCont(3);
					}
				}
			}
			if (e.getKeyCode() == RIGHT_KEY) {
				if (tickCount == 1)
					keyPressed = RIGHT_KEY;
			}
			if (e.getKeyCode() == LEFT_KEY) {
				if (tickCount == 1)
					keyPressed = LEFT_KEY;
			}
			if (e.getKeyCode() == UP_KEY) {
				if (tickCount == 1)
					keyPressed = UP_KEY;
			}
			if (e.getKeyCode() == DOWN_KEY) {
				if (tickCount == 1)
					keyPressed = DOWN_KEY;
			}
			if (e.getKeyCode() == ESC_KEY) {
				tickCount = 0;
				keyPressed = ESC_KEY;
			}
		} else if (screenStatus == PLAY_SCREEN && !isPressed) {
			if (e.getKeyCode() == RIGHT_KEY) {
				keyPressed = RIGHT_KEY;
				isPressed = true;
			}
			if (e.getKeyCode() == LEFT_KEY) {
				keyPressed = LEFT_KEY;
				isPressed = true;
			}
			if (e.getKeyCode() == UP_KEY) {
				keyPressed = UP_KEY;
				isPressed = true;
			}
			if (e.getKeyCode() == DOWN_KEY) {
				keyPressed = DOWN_KEY;
				isPressed = true;
			}
			if (e.getKeyCode() == BOMB_KEY) {
				keyPressed = BOMB_KEY;
				isPressed = true;
			}

		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}
}