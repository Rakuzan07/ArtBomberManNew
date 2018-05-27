package Artbomberman.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Gmanager {

	private ArrayList<Player> players;

	private World world;

	private ArrayList<PlayerManager> playersManager = new ArrayList<PlayerManager>();

	public Gmanager(ArrayList<Player> players, World world) {
		this.players = players;
		this.world = world;
		for (int i = 0; i < players.size(); i++) {
			playersManager.add(new PlayerManager(players.get(i), world));
		}
	}
	
	
	public World getWorld() {
		return world;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void tryToExplodeAll(int cont) {
		for (int i = 0; i < players.size(); i++) {
			playersManager.get(i).tryExplosion(cont);
		}
	}

	public void updateAll() {
		for (int i = 0; i < players.size(); i++) {
			update(players.get(i));
		}
	}

	public void update(Player p) {
		int indexPlayer = players.indexOf(p);
		playersManager.get(indexPlayer).update();
	}

	
	public void tryToMoveLeft(Player player) {
		int x=player.getX();
		int y=player.getY();
		if (x-1>=0&&!world.getBlockMatrix()[x-1][y].isPhysical()) player.moveLeft();
	}
	
	public void tryToMoveRight(Player player) {
		int x=player.getX();
		int y=player.getY();
		if (x+1<world.getDimension()&&!world.getBlockMatrix()[x+1][y].isPhysical()) player.moveRight();
	}
	
	public void tryToMoveUp(Player player) {
		int x=player.getX();
		int y=player.getY();
		if(y-1>=0&&!world.getBlockMatrix()[x][y-1].isPhysical()) player.moveUp();
	}
	
	public void tryToMoveDown(Player player) {
		int x=player.getX();
		int y=player.getY();
		if (y+1<world.getDimension()&&!world.getBlockMatrix()[x][y+1].isPhysical()) player.moveDown();
	}
	
	public void tryToReloadTank(Player player ,Position p) {
		int x=p.getX();
		int y=p.getY();
		if(world.getBlockMatrix()[x][y].getColor()==player.getColor()) player.reloadTank();
	}
	
	
	
	

	
	
}