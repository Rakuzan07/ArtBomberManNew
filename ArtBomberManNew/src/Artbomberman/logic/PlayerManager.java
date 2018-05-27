package Artbomberman.logic;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class PlayerManager {

	private ArrayList<Player> players;

	private World world;

	private Player player;

	private ArrayList<Position> bombPosition;

	private static final int SHIFT = 5;

	private ArrayList<Integer> tempTarget = new ArrayList<Integer>();

	private ArrayList<Position> way = new ArrayList<Position>(); //ARRAY DI POSIZIONI CHE IL NEMICO DEVE PERCORRERE

	private Position tempPos = null;

	private boolean firstCheck = true;

	private boolean checkPosition = false;

	private boolean inkFocussed = false, targetFocussed = false;

	private double cont = 0;

	private int indexTarget, posxTarget, posyTarget;

	public PlayerManager(Player player, World world) {
		this.player = player;
		this.players = world.getPlayers();
		this.world = world;
		this.bombPosition = player.getBombPosition();
	}

	public World getWorld() {
		return world;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void tryExplosion(int trigger) { //TRIGGER SAREBBE IL VALORE 7 CHE FA ESPLODERE LE BOMBE
		if (bombPosition.size() == 0)
			return;
		else {
			player.increaseContBomb(); //INCREMENTA I VALORI DELLE BOMBE
			ArrayList<Position> bombToTrigger=player.removeBomb(trigger); //RIMUOVE QUELLE CON UN VALORE DEL CONT PARI AL TRIGGER
			if(bombToTrigger.size()>0) {
				for(int i=0;i<bombToTrigger.size();i++) {
					world.paint(bombToTrigger.get(i), player.getColor()); //COLORA LE CASELLE DELLE BOMBE RIMOSSE DEL COLORE DEL GIOCATORE CHE LE AVEVA PIAZZATE
				}
			}
		}
	}
	

	public void update() {
		if (!inkFocussed && player.getInkTank() == 0) { //TROVARE IL PUNTO PIU' VICINO PER RIFORNIRSI 
			for (int i = 0; i < world.getDimension(); i++) {
				for (int j = 0; j < world.getDimension(); j++) {
					if (!world.getBlockMatrix()[i][j].isPhysical()
							&& world.getBlockMatrix()[i][j].getColor() == player.getColor() && firstCheck) {
						tempPos = new Position(j, i);
						firstCheck = false;
					} else if (!world.getBlockMatrix()[i][j].isPhysical()
							&& world.getBlockMatrix()[i][j].getColor() == player.getColor()) {
						int xf = player.getX() - j;
						int yf = player.getY() - i;
						double shift = Math.sqrt(Math.pow(xf, 2) + Math.pow(yf, 2));
						xf = player.getX() - tempPos.getX();
						yf = player.getY() - tempPos.getY();
						if (shift - Math.sqrt(Math.pow(xf, 2) + Math.pow(yf, 2)) < 0)
							tempPos = new Position(j, i);
					}
				}
			}
			inkFocussed = true;   //CI DICE CHE ABBIAMO TROVATO IL PUNTO MIGLIORE 
			if (tempPos != null)   //TEMPPOS E' NULLO SE NON VI E' NESSUN PUNTO DEL NOSTRO STESSO COLORE
				way = chooseWay(player.getPosition(), tempPos);
		} else if (player.getInkTank() == 0) {
			if (way.size() == 0)
				inkFocussed = false;
			else {
				tempPos = way.get(0);
				if (player.getX() - tempPos.getX() > 0) {
					player.moveLeft();
					world.reloadTank(player);
					inkFocussed = !(player.getInkTank() > 0);
				} else if (player.getX() - tempPos.getX() < 0) {
					player.moveRight();
					world.reloadTank(player);
					inkFocussed = !(player.getInkTank() > 0);
				} else if (player.getY() - tempPos.getY() > 0) {
					player.moveUp();
					world.reloadTank(player);
					inkFocussed = !(player.getInkTank() > 0);
				} else if (player.getY() - tempPos.getY() < 0) {
					player.moveDown();
					world.reloadTank(player);
					inkFocussed = !(player.getInkTank() > 0);
				}
				firstCheck = true;
				if (player.getX() == (tempPos.getX()) && player.getY() == (tempPos.getY()))
					way.remove(0);
			}
		}
		if (!checkPosition && player.getInkTank() > 0) {
			for (int i = 0; i < world.getDimension(); i++) {
				for (int j = 0; j < world.getDimension(); j++) {
					if (world.getBlockMatrix()[i][j].getColor() != player.getColor()
							&& !world.getBlockMatrix()[i][j].isPhysical() && firstCheck) {
						tempPos = new Position(j, i);
						cont = world.checkMatrix(player.getColor(), j, i);
						firstCheck = false;
					} else if (world.getBlockMatrix()[i][j].getColor() != player.getColor()) {
						double cont1 = world.checkMatrix(player.getColor(), j, i);
						if (cont < cont1) {
							cont = cont1;
							tempPos = new Position(j, i);
						} else if (cont == cont1) {
							int xf = player.getX() - j;
							int yf = player.getY() - i;
							double shift = Math.sqrt(Math.pow(xf, 2) + Math.pow(yf, 2));
							xf = player.getX() - tempPos.getX();
							yf = player.getY() - tempPos.getY();
							if (shift - Math.sqrt(Math.pow(xf, 2) + Math.pow(yf, 2)) < 0)
								tempPos = new Position(j, i);
						}
					}
				}
			}
			way = chooseWay(player.getPosition(), tempPos);
			checkPosition = true;
			if(way.size()!=0)tempPos = way.get(0);
		} else if (checkPosition && player.getInkTank() > 0) {
			if (tempPos != null && player.getX() - tempPos.getX() > 0) {
				player.moveLeft();
			} else if (tempPos != null && player.getX() - tempPos.getX() < 0) {
				player.moveRight();
			} else if (tempPos != null && player.getY() - tempPos.getY() > 0) {
				player.moveUp();
			} else if (tempPos != null && player.getY() - tempPos.getY() < 0) {
				player.moveDown();
			} else if (way.size()!=0&&way.get(0) == null) {
				checkPosition = false;
				player.placeBomb(player.getPosition());
			}
			if (way.size()!=0&&tempPos != null && player.getX() == (tempPos.getX()) && player.getY() == (tempPos.getY())) {
				way.remove(0);
				way.add(way.size(), null);
				tempPos = way.get(0);
			}
			firstCheck = true;
		}

	}

	private ArrayList<Position> chooseWay(Position departure, Position arrival) {
		ArrayList<Position> empty = new ArrayList<Position>();
		ArrayList<Position> visited=new ArrayList<Position>();
		chooseWayRic(departure, arrival, empty , visited);
		ArrayList<Position> trimmedWay=trim(departure,empty);
		if(trimmedWay!=null) empty=trimmedWay;
		return empty;
	}
	
	private ArrayList<Position> trim(Position departure , ArrayList<Position> way) {
		int index=way.indexOf(departure);
		if(index<0) return null;
		ArrayList<Position> trimmedWay=new ArrayList<Position>();
		for(int i=index+1;i<way.size();i++) {
			trimmedWay.add(way.get(i));
		}
		return trimmedWay;
	}
	


	private void chooseWayRic(Position departure, Position arrival, ArrayList<Position> position , ArrayList<Position> visited) {
		if (arrival.equals(departure)) {
			return;
		}
		ArrayList<Position> tempPosition = new ArrayList<Position>();
		World.fillWay(departure, arrival, world, tempPosition);
		orderPosition(departure, arrival, tempPosition);
		for (int i = 0; i < tempPosition.size(); i++) {
			boolean isVisited=visited.contains(tempPosition.get(i));
			if(!isVisited) {
			visited.add(tempPosition.get(i));
			position.add(tempPosition.get(i));
			chooseWayRic(tempPosition.get(i), arrival, position,visited);
			if (position.get(position.size() - 1).equals(arrival))
				return;
			position.remove(position.size() - 1);
			}
		}
	}

	private void orderPosition(Position departure, Position arrival, ArrayList<Position> position) {
		if (position.size() == 0)
			return;
		for (int i = 0; i < position.size(); i++) {
			Position tempPosition = position.get(i);
			for (int j = i + 1; j < position.size(); j++) {
				if (departure.shift(tempPosition) + tempPosition.shift(arrival) > departure.shift(position.get(j))
						+ arrival.shift(position.get(j))) {

					position.set(i, position.get(j));
					position.set(j, tempPosition);
					tempPosition = position.get(i);
				}
			}
		}
	}
	
	public void tryToMoveLeft() {
		int x=player.getX();
		int y=player.getY();
		if (x-1>=0&&!world.getBlockMatrix()[x-1][y].isPhysical()) player.moveLeft();
	}
	
	public void tryToMoveRight() {
		int x=player.getX();
		int y=player.getY();
		if (x+1<world.getDimension()&&!world.getBlockMatrix()[x+1][y].isPhysical()) player.moveRight();
	}
	
	public void tryToMoveUp() {
		int x=player.getX();
		int y=player.getY();
		if(y-1>=0&&!world.getBlockMatrix()[x][y-1].isPhysical()) player.moveUp();
	}
	
	public void tryToMoveDown() {
		int x=player.getX();
		int y=player.getY();
		if (y+1<world.getDimension()&&!world.getBlockMatrix()[x][y+1].isPhysical()) player.moveDown();
	}

}
