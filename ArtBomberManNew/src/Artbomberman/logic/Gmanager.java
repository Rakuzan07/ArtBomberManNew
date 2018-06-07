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
	
	private Player playerOneMulti;
	private PlayerManager playerOneManager;
	
	private Player winningPlayer;

	public Gmanager(ArrayList<Player> players, World world) {
		this.players = players;
		this.world = world;
		for (int i = 0; i < players.size(); i++) {
			playersManager.add(new PlayerManager(players.get(i), world));
		}
	}
	
	public Gmanager(Player playerOneMulti, World world) {
		this.playerOneMulti = playerOneMulti ;
		this.world = world;
		playerOneManager=new PlayerManager(playerOneMulti, world);
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
	
	public void tryToExplode(int cont,int index) {
		
		playersManager.get(index).tryExplosion(cont);
	}
	
	public void TryToExplodeSinglePlayer(int cont) {
		playerOneManager.tryExplosion(cont);
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
		if (x-1>=0&&!world.getBlockMatrix()[y][x-1].isPhysical()) player.moveLeft();
	}
	
	public void tryToMoveRight(Player player) {
		int x=player.getX();
		int y=player.getY();
		if (x+1<world.getDimension()&&!world.getBlockMatrix()[y][x+1].isPhysical()) player.moveRight();
	}
	
	public void tryToMoveUp(Player player) {
		int x=player.getX();
		int y=player.getY();
		if(y-1>=0&&!world.getBlockMatrix()[y-1][x].isPhysical()) player.moveUp();
	}
	
	public void tryToMoveDown(Player player) {
		int x=player.getX();
		int y=player.getY();
		if (y+1<world.getDimension()&&!world.getBlockMatrix()[y+1][x].isPhysical()) player.moveDown();
	}
	
	public void tryToReloadTank(Player player ,Position p) {
		int x=p.getX();
		int y=p.getY();
		if(world.getBlockMatrix()[y][x].getColor()==player.getColor()) player.reloadTank();
	}
	
	
	
	

	
	
}