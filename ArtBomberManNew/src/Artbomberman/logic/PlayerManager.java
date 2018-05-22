package Artbomberman.logic;

import java.util.ArrayList;
import java.util.Random;

public class PlayerManager {
	
	private ArrayList<Player> players;

	private World world;

	Player player;
	
	private static final int  SHIFT = 5;

	private ArrayList<Integer> tempTarget = new ArrayList<Integer>();

	private ArrayList<Position> way = new ArrayList<Position>();

	private Position tempPos = null;

	private boolean firstCheck = true;

	private boolean checkPosition = false;

	private boolean inkFocussed = false, targetFocussed = false;

	private double cont = 0;

	private int indexTarget, posxTarget, posyTarget;

	private Random random = new Random();

	public PlayerManager(Player player , World world) {
		this.player=player;
		this.players = world.getPlayers();
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	
	
	public void update() {
		int rand=random.nextInt(10);
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
			way = chooseWay(player.getPosition(), tempPos);
		} else if (player.getInkTank() == 0) {
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
		} /*else if (!targetFocussed) {
			firstCheck = true;
			for (int i = 0; i < world.getPlayers().size(); i++) {
				if (!player.equals(world.getPlayers().get(i))
						&& player.getPosition().shift(world.getPlayers().get(i).getPosition()) - SHIFT <= 0) {
					tempTarget.add(i);
					checkPosition = false;
				}
			}
		}*/
		/*if (tempTarget.size() > 0&&rand<=3) {
			if (!targetFocussed) {
				indexTarget = random.nextInt(tempTarget.size());
				posxTarget = world.getPlayers().get((tempTarget.get(indexTarget))).getX();
				posyTarget = world.getPlayers().get((tempTarget.get(indexTarget))).getY();
				targetFocussed = true;
				tempPos = new Position(posxTarget, posyTarget);
				way = chooseWay(player.getPosition(), tempPos);
			} else {
				tempPos = way.get(0);
				if (tempPos != null && player.getX() - tempPos.getX() > 0)
					player.moveLeft();
				else if (tempPos != null && player.getX() - tempPos.getX() < 0)
					player.moveRight();
				else if (tempPos != null && player.getY() - tempPos.getY() > 0)
					player.moveUp();
				else if (tempPos != null && player.getY() - tempPos.getY() < 0)
					player.moveDown();
				else {
					player.placeBomb(player.getPosition());
					targetFocussed = false;
					tempTarget.clear();
				}
				if (player.getX() == (tempPos.getX()) && player.getY() == (tempPos.getY())) {
					way.remove(0);
					way.add(way.size(), null);
				}
			}
		}
		else if(tempTarget.size() > 0&&rand>3){
			targetFocussed = false;
			tempTarget.clear();
		}else {*/
			if (!checkPosition) {
				for (int i = 0; i < world.getDimension(); i++) {
					for (int j = 0; j < world.getDimension(); j++) {
						if (world.getBlockMatrix()[i][j].getColor() != player.getColor()
								&& !world.getBlockMatrix()[i][j].isPhysical() && firstCheck) {
							tempPos = new Position(j, i);
							cont = world.checkMatrix(player.getColor(), i, j);
							System.out.println("Sono entrato");
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
				checkPosition = true;
			} else {
				System.out.println(player.getPosition()+"  "+tempPos);
				if (tempPos!=null&&player.getX() - tempPos.getX() > 0) {
					player.moveLeft();
				} else if (tempPos!=null&&player.getX() - tempPos.getX() < 0) {
					player.moveRight();
				} else if (tempPos!=null&&player.getY() - tempPos.getY() > 0) {
					player.moveUp();
				} else if (tempPos!=null&&player.getY() - tempPos.getY() < 0) {
					player.moveDown();
				} else if (way.get(0)==null){
					checkPosition = false;
					player.placeBomb(player.getPosition());
				}
				if (tempPos!=null &&player.getX() == (tempPos.getX()) && player.getY() == (tempPos.getY())) {
					way.remove(0);
					way.add(way.size(), null);
					tempPos=way.get(0);
				}
				firstCheck = true;
			}
			
		}
	//}

	private ArrayList<Position> chooseWay(Position departure, Position arrival) {
		ArrayList<Position> empty = new ArrayList<Position>();
		chooseWayRic(departure, arrival, empty);
		return empty;
	}

	private void chooseWayRic(Position departure, Position arrival, ArrayList<Position> position) {
		if (arrival.equals(departure)) {
			position.add(arrival);
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
			position.remove(position.size());
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
