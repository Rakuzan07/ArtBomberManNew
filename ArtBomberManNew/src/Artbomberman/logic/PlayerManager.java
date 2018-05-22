package Artbomberman.logic;

import java.util.ArrayList;
import java.util.Random;

public class PlayerManager {

	private ArrayList<Player> players;

	private World world;

	private Player player;

	private ArrayList<Position> bombPosition;

	private static final int SHIFT = 5;

	private ArrayList<Integer> tempTarget = new ArrayList<Integer>();

	private ArrayList<Position> way = new ArrayList<Position>();

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

	public void tryExplosion(int trigger) {
		if (bombPosition.size() == 0)
			return;
		else {
			player.increaseContBomb();
			ArrayList<Position> bombToTrigger=player.removeBomb(trigger);
			if(bombToTrigger.size()>0) {
				for(int i=0;i<bombToTrigger.size();i++) {
					world.paint(bombToTrigger.get(i), player.getColor());
				}
			}
		}
	}

	public void update() {
		if (!inkFocussed && player.getInkTank() == 0) {
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
			inkFocussed = true;
			if (tempPos != null)
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
						cont = world.checkMatrix(player.getColor(), i, j);
						firstCheck = false;
					} else if (world.getBlockMatrix()[i][j].getColor() != player.getColor()) {
						double cont1 = world.checkMatrix(player.getColor(), i, j);
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
			String s = "way ";
			for (int i = 0; i < way.size(); i++) {
				s = s + way.get(i) + " ";
			}
			System.out.println(s);
			checkPosition = true;
			tempPos = way.get(0);
		} else if (checkPosition && player.getInkTank() > 0) {
			System.out.println(player.getPosition() + "  " + tempPos);
			if (tempPos != null && player.getX() - tempPos.getX() > 0) {
				player.moveLeft();
			} else if (tempPos != null && player.getX() - tempPos.getX() < 0) {
				player.moveRight();
			} else if (tempPos != null && player.getY() - tempPos.getY() > 0) {
				player.moveUp();
			} else if (tempPos != null && player.getY() - tempPos.getY() < 0) {
				player.moveDown();
			} else if (way.get(0) == null) {
				checkPosition = false;
				player.placeBomb(player.getPosition());
				System.out.println(player.getBombPosition().size() + " " + player.getPosition());
			}
			if (tempPos != null && player.getX() == (tempPos.getX()) && player.getY() == (tempPos.getY())) {
				way.remove(0);
				way.add(way.size(), null);
				tempPos = way.get(0);
			}
			firstCheck = true;
		}

	}

	private ArrayList<Position> chooseWay(Position departure, Position arrival) {
		ArrayList<Position> empty = new ArrayList<Position>();
		chooseWayRic(departure, arrival, empty);
		return empty;
	}

	private void chooseWayRic(Position departure, Position arrival, ArrayList<Position> position) {
		if (arrival.equals(departure)) {
			return;
		}
		ArrayList<Position> tempPosition = new ArrayList<Position>();
		World.fillWay(departure, arrival, world, tempPosition);
		orderPosition(departure, arrival, tempPosition);
		for (int i = 0; i < tempPosition.size(); i++) {
			position.add(tempPosition.get(i));
			chooseWayRic(tempPosition.get(i), arrival, position);
			if (position.get(position.size() - 1).equals(arrival))
				return;
			position.remove(position.size() - 1);
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

}
