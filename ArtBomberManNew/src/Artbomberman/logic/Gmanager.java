package Artbomberman.logic;

import java.util.ArrayList;
import java.util.Random;

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
		for(int i=0;i<players.size();i++) {
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
}