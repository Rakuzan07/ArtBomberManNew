package Artbomberman.logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.omg.Messaging.SyncScopeHelper;

import Artbomberman.logic.Player.Status;

public class World {

	private Block[][] blockMatrix; //MATRICE DI BLOCCHI

	private ArrayList<Player> players;

	private static final int DIM_WORLD = 50;


	private int dimWorld;

	public World(ArrayList<Player> players) {
		dimWorld = DIM_WORLD;
		blockMatrix = new Block[dimWorld][dimWorld];
		for (int i = 0; i < dimWorld; i++) {
			for (int j = 0; j < dimWorld; j++) {
				blockMatrix[i][j] = new Block(Color.GREY);
			}
		}
		this.players = new ArrayList<Player>();
		for (int i = 0; i < players.size(); i++) {
			this.players.add(players.get(i));
		}
	}

	public World(ArrayList<Player> players, int dimWorld) {
		this.dimWorld = dimWorld;
		blockMatrix = new Block[dimWorld][dimWorld];
		for (int i = 0; i < this.dimWorld; i++) {
			for (int j = 0; j < this.dimWorld; j++) {
				blockMatrix[i][j] = new Block(Color.GREY);
			}
		}
		this.players = new ArrayList<Player>();
		for (int i = 0; i < players.size(); i++) {
			this.players.add(players.get(i));
		}
	}

	public void setBlockMatrix(Block[][] BlockMatrix) {
		this.blockMatrix = BlockMatrix;
	}

	public int getDimension() {
		return dimWorld;
	}

	public Block[][] getBlockMatrix() {
		return blockMatrix;
	}

	public void reloadTank(Player p) {  //RICARICA BOMBE DEL PLAYER P
		if (p.getColor().equals(blockMatrix[p.getY()][p.getX()].getColor()))
			p.reloadTank();
	}
	
	public boolean checkColor(Color c) { //CONTROLLA SE VI è UNA CASELLA DELLO STESSO COLORE PASSATO COME PARAMETRO
		for (int i=0;i<dimWorld;i++) {
			for (int j=0;j<dimWorld;j++) {
				if(blockMatrix[i][j].equals(c)) return true;
			}
		}
		return false;
	}

	public void paint(Position p, Color color) {  //COLORAMENTO DEI BLOCCHI (MATRICE 3X3) ALL'ESPOSIONE DELLE BOMBE
		if (p.getX() < 0 || p.getX() > dimWorld || p.getY() < 0 || p.getY() > dimWorld)
			throw new IllegalArgumentException();
		int x = p.getX();
		int y = p.getY();
		blockMatrix[y][x].setColor(color);
		if (y - 1 >= 0) {
			if (blockMatrix[y - 1][x].getBreakCont() == 0)  //SE C'ERA IL PAVIMENTO LO COLORA DEL COLORE DELLA BOMBA ESPLOSA
				blockMatrix[y - 1][x].setColor(color);
			else if (blockMatrix[y - 1][x].getBreakCont() == 1 || blockMatrix[y - 1][x].getColor() == color) {  //ALTRIMENTI SE IL CONT DEL BLOCCO E' PARI AD UNO LO DISTRUGGE E COLORA LA SUA PARTE DI GRIGIO
				blockMatrix[y - 1][x].setBreakCont(0);
				blockMatrix[y - 1][x].setColor(Color.GREY);
			} else {
				blockMatrix[y - 1][x].repaint(); //ALTRIMENTI EFFETTUA IL REPAINT
			}
		}
		if (y - 1 >= 0 && x - 1 >= 0)
			if (blockMatrix[y - 1][x - 1].getBreakCont() == 0)
				blockMatrix[y - 1][x - 1].setColor(color);
			else if (blockMatrix[y - 1][x - 1].getBreakCont() == 1 || blockMatrix[y - 1][x - 1].getColor() == color) {
				blockMatrix[y - 1][x - 1].setBreakCont(0);
				blockMatrix[y - 1][x - 1].setColor(Color.GREY);
			} else {
				blockMatrix[y - 1][x - 1].repaint();
			}
		if (y - 1 >= 0 && x + 1 < dimWorld)
			if (blockMatrix[y - 1][x + 1].getBreakCont() == 0)
				blockMatrix[y - 1][x + 1].setColor(color);
			else if (blockMatrix[y - 1][x + 1].getBreakCont() == 1 || blockMatrix[y - 1][x + 1].getColor() == color) {
				blockMatrix[y - 1][x + 1].setBreakCont(0);
				blockMatrix[y - 1][x + 1].setColor(Color.GREY);
			} else {
				blockMatrix[y - 1][x + 1].repaint();
			}
		if (x - 1 >= 0)
			if (blockMatrix[y][x - 1].getBreakCont() == 0)
				blockMatrix[y][x - 1].setColor(color);
			else if (blockMatrix[y][x - 1].getBreakCont() == 1 || blockMatrix[y][x - 1].getColor() == color) {
				blockMatrix[y][x - 1].setBreakCont(0);
				blockMatrix[y][x - 1].setColor(Color.GREY);
			} else {
				blockMatrix[y][x - 1].repaint();
			}
		if (x + 1 < dimWorld)
			if (blockMatrix[y][x + 1].getBreakCont() == 0)
				blockMatrix[y][x + 1].setColor(color);
			else if (blockMatrix[y][x + 1].getBreakCont() == 1 || blockMatrix[y][x + 1].getColor() == color) {
				blockMatrix[y][x + 1].setBreakCont(0);
				blockMatrix[y][x + 1].setColor(Color.GREY);
			} else {
				blockMatrix[y][x + 1].repaint();
			}
		if (y + 1 < dimWorld)
			if (blockMatrix[y + 1][x].getBreakCont() == 0)
				blockMatrix[y + 1][x].setColor(color);
			else if (blockMatrix[y + 1][x].getBreakCont() == 1 || blockMatrix[y + 1][x].getColor() == color) {
				blockMatrix[y + 1][x].setBreakCont(0);
				blockMatrix[y + 1][x].setColor(Color.GREY);
			} else {
				blockMatrix[y + 1][x].repaint();
			}
		if (y + 1 < dimWorld && x - 1 >= 0)
			if (blockMatrix[y + 1][x - 1].getBreakCont() == 0)
				blockMatrix[y + 1][x - 1].setColor(color);
			else if (blockMatrix[y + 1][x - 1].getBreakCont() == 1 || blockMatrix[y + 1][x - 1].getColor() == color) {
				blockMatrix[y + 1][x - 1].setBreakCont(0);
				blockMatrix[y + 1][x - 1].setColor(Color.GREY);
			} else {
				blockMatrix[y + 1][x - 1].repaint();
			}
		if (y + 1 < dimWorld && x + 1 < dimWorld)
			if (blockMatrix[y + 1][x + 1].getBreakCont() == 0)
				blockMatrix[y + 1][x + 1].setColor(color);
			else if (blockMatrix[y + 1][x + 1].getBreakCont() == 1 || blockMatrix[y + 1][x + 1].getColor() == color) {
				blockMatrix[y + 1][x + 1].setBreakCont(0);
				blockMatrix[y + 1][x + 1].setColor(Color.GREY);
			} else {
				blockMatrix[y + 1][x + 1].repaint();
			}
	}

	public double checkVictory(Player p) { //CONTROLLA QUANTE CASELLE DELLO STESSO COLORE SONO STATE COLORATE PER DETERMINARE LA VITTORIA
		if (!players.contains(p))
			throw new IllegalArgumentException("The player does not exist in this world");
		double cont = 0;
		for (int i = 0; i < dimWorld; i++) {
			for (int j = 0; j < dimWorld; j++) {
				if (blockMatrix[i][j].getColor() == p.getColor()&&!blockMatrix[i][j].isPhysical())
					cont++;
			}
		}
		return ( cont / (dimWorld*dimWorld));
	}

	public double checkMatrix(Color color, int x, int y) { //RESTITUISCE UN DOUBLE CHE RAPPRESENTA L'EFFICACIA DELL'ESPLOSIONE DI UNA BOMBA IN UNA POSIZIONE. IL COLORAMENTO DI UNA CASELLA VALE 1 MENTRE QUELLA DI UN BLOCCO VALE 1/3. METODO UTILIZZATO DAI NEMICI PER TROVARE UNA POSIZIONE OTTIMA NELLA QUALE PIAZZARE LA BOMBA
		int posPlayer = players.indexOf(new Player(color, new Position(0, 0)));
		ArrayList<Position> bomb = players.get(posPlayer).getBombPosition();
		double cont = -returncontBomb(bomb, x, y);
		if (blockMatrix[y][x].getColor() != color) {
			if (!blockMatrix[y][x].isPhysical())
				cont++;
			else
				cont = cont + 1 / 3;
		}
		if (y - 1 >= 0 && blockMatrix[y - 1][x].getColor() != color) {
			if (!blockMatrix[y - 1][x].isPhysical())
				cont++;
			else
				cont = cont + 1 / 3;
		}
		if (y + 1 < blockMatrix.length && blockMatrix[y + 1][x].getColor() != color) {
			if (!blockMatrix[y + 1][x].isPhysical())
				cont++;
			else
				cont = cont + 1 / 3;
		}
		if (y - 1 >= 0 && x - 1 >= 0 && blockMatrix[y - 1][x - 1].getColor() != color) {
			if (!blockMatrix[y - 1][x - 1].isPhysical())
				cont++;
			else
				cont = cont + 1 / 3;
		}
		if (y - 1 >= 0 && x + 1 < blockMatrix.length && blockMatrix[y - 1][x + 1].getColor() != color) {
			if (!blockMatrix[y - 1][x + 1].isPhysical())
				cont++;
			else
				cont = cont + 1 / 3;
		}
		if (x - 1 >= 0 && blockMatrix[y][x - 1].getColor() != color) {
			if (!blockMatrix[y][x - 1].isPhysical())
				cont++;
			else
				cont = cont + 1 / 3;
		}
		if (x + 1 < blockMatrix.length && blockMatrix[y][x + 1].getColor() != color) {
			if (!blockMatrix[y][x + 1].isPhysical())
				cont++;
			else
				cont = cont + 1 / 3;
		}
		if (y + 1 < blockMatrix.length && x - 1 >= 0 && blockMatrix[y + 1][x - 1].getColor() != color) {
			if (!blockMatrix[y + 1][x - 1].isPhysical())
				cont++;
			else
				cont = cont + 1 / 3;
		}
		if (y + 1 < blockMatrix.length && x + 1 < blockMatrix.length && blockMatrix[y + 1][x + 1].getColor() != color) {
			if (!blockMatrix[y + 1][x + 1].isPhysical())
				cont++;
			else
				cont = cont + 1 / 3;
		}
		return cont;
	}

	private int returncontBomb(ArrayList<Position> bomb, int x, int y) { //RESTITUISCE IL NUMERO DI BLOCCHI CHE VERRANNO COLORATI DA ALTRE BOMBE IN ADIACENZA ALLA POS X,Y
		if (bomb.size() == 0)
			return 0;
		ArrayList<Position> posBombs = tryExplosion(bomb.get(0), x, y);
		for (int i = 1; i < bomb.size(); i++) {
			ArrayList<Position> tempPosition = tryExplosion(bomb.get(i), x, y);
			for (int j = 0; j < tempPosition.size(); j++) {
				if (!posBombs.contains(tempPosition.get(j)))
					posBombs.add(tempPosition.get(j));
			}
		}
		return posBombs.size();
	}

	private ArrayList<Position> tryExplosion(Position bomb, int nx, int ny) { //SIMULA L'ESPLOSIONE DI UNA BOMBA UTILE PER DETERMINARE L'EFFICACIA DI UN'ESPLOSIONE
		ArrayList<Position> tempPosition = new ArrayList<Position>();
		int x = bomb.getX();
		int y = bomb.getY();

		if (Math.abs(nx - x) <= 1 && Math.abs(ny - y) <= 1)
			tempPosition.add(new Position(x, y));
		if (y - 1 >= 0 && Math.abs(ny - (y - 1)) <= 1 && Math.abs(nx - x) <= 1)
			tempPosition.add(new Position(x, y - 1));
		if (y + 1 < blockMatrix.length && Math.abs(ny - (y + 1)) <= 1 && Math.abs(nx - x) <= 1)
			tempPosition.add(new Position(x, y + 1));
		if (y - 1 >= 0 && x - 1 >= 0 && Math.abs(ny - (y - 1)) <= 1 && Math.abs(nx - (x - 1)) <= 1)
			tempPosition.add(new Position(x - 1, y - 1));
		if (y - 1 >= 0 && x + 1 < blockMatrix.length && Math.abs(ny - (y - 1)) <= 1 && Math.abs(nx - (x + 1)) <= 1)
			tempPosition.add(new Position(x + 1, y - 1));
		if (x - 1 >= 0 && Math.abs(ny - y) <= 1 && Math.abs(nx - (x - 1)) <= 1)
			tempPosition.add(new Position(x - 1, y));
		if (x + 1 < blockMatrix.length && Math.abs(ny - y) <= 1 && Math.abs(nx - (x + 1)) <= 1)
			tempPosition.add(new Position(x + 1, y));
		if (y + 1 < blockMatrix.length && x - 1 >= 0 && Math.abs(ny - (y + 1)) <= 1 && Math.abs(nx - (x - 1)) <= 1)
			tempPosition.add(new Position(x - 1, y + 1));
		if (y + 1 < blockMatrix.length && x + 1 < blockMatrix.length && Math.abs(ny - (y + 1)) <= 1
				&& Math.abs(nx - (x + 1)) <= 1)
			tempPosition.add(new Position(x + 1, y + 1));
		return tempPosition;
	}

	
	public static void fillWay(Position departure, Position arrival, World world, ArrayList<Position> tempPosition) { //RIEMPIE L'ARRAY TEMPPOSITION CON LE POSIZIONI ADIACENTI A DEPARTURE CHE NON SONO BLOCCHI
		int x = departure.getX();
		int y = departure.getY();
		if (x - 1 >= 0 && world.getBlockMatrix()[y][x - 1].getBreakCont() == 0)
			tempPosition.add(new Position(x - 1, y));
		if (x + 1 < world.getDimension() && world.getBlockMatrix()[y][x + 1].getBreakCont() == 0)
			tempPosition.add(new Position(x + 1, y));
		if (y - 1 >= 0 && world.getBlockMatrix()[y - 1][x].getBreakCont() == 0)
			tempPosition.add(new Position(x, y - 1));
		if (y + 1 < world.getDimension() && world.getBlockMatrix()[y + 1][x].getBreakCont() == 0)
			tempPosition.add(new Position(x, y + 1));
	}

	public void clear() {
		for (int i = 0; i < dimWorld; i++) {
			for (int j = 0; j < dimWorld; j++) {
				blockMatrix[i][j].setBreakCont(0);
				blockMatrix[i][j].setColor(Color.GREY);
			}
		}
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void setPlayer(ArrayList<Player> players) {
		this.players=players;
	}

}
