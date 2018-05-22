package Artbomberman.logic;

import java.util.ArrayList;


public class Player {

	private static final int TANK_VALUE = 4;

	private int inkTank, numBomb;

	private Color color;

	private Position position;

	private ArrayList<Position> bombPosition;

	private ArrayList<Integer> contBomb;
	
	public enum Status{UP,DOWN,LEFT,RIGHT}
	
	private Status state;
	
	public Player(Color color, Position position,Status state) {
		this.color = color;
		this.numBomb = this.inkTank = TANK_VALUE;
		this.position = new Position(position);
		this.state=state;
		bombPosition = new ArrayList<>();
		contBomb=new ArrayList<Integer>();
	}

	public Player(Color color, Position position, int inkTank,Status state) {
		this.color = color;
		this.numBomb = this.inkTank = inkTank;
		this.position = new Position(position);
		this.state=state;
		bombPosition = new ArrayList<>();
		contBomb=new ArrayList<Integer>();
	}

	public Player(Player p,Status state) {
		this.color = p.color;
		this.numBomb = this.inkTank = p.inkTank;
		this.state=state;
		this.position = new Position(p.position);
		bombPosition = new ArrayList<>();
		contBomb=new ArrayList<Integer>();
		for (int i = 0; i < p.bombPosition.size(); i++) {
			bombPosition.add(new Position(p.bombPosition.get(i)));
			contBomb.add(p.contBomb.get(i));
		}
	}

	public Status getState() {
		return state;
	}
	
	public void setState(Status state) {
		this.state=state;
	}
	
	public void reloadTank() {
		if (inkTank == 0)
			inkTank = numBomb;
	}

	public void reloadTankIfNotFull() {
		if (inkTank == numBomb)
			return;
		inkTank++;
	}

	public int getX() {
		return position.getX();
	}

	public int getY() {
		return position.getY();
	}

	public ArrayList<Position> getBombPosition() {
		return bombPosition;
	}
	
	public Position getPosition(){
		return position;
	}
	
	public boolean placeBomb(Position p) {
		/*if(inkTank>0) {
			boolean okInsert=true;
			for(int i=0;i<bombPosition.size();i++){
				if(bombPosition.get(i).equals(p))okInsert=false;
			}
			if(okInsert){
				inkTank--;
				bombPosition.add(p);
			}
			return okInsert;
		}
		return false;*/
	    boolean insert=bombPosition.contains(p);
	    if(!insert) { bombPosition.add(p); inkTank--;}
	    return !insert;
	}

	public final boolean equals(Object arg0) {
		return color == (((Player) arg0).color);
	}

	public final int hashCode() {
		return color.hashCode();
	}

	public boolean checkTank() {
		return inkTank > 0;
	}

	public int getInkTank() {
		return inkTank;
	}

	public Color getColor() {
		return color;
	}

	public void moveUp() {
		position.setY(position.getY() - 1);
	}

	public void moveDown() {
		position.setY(position.getY() + 1);
	}

	public void moveLeft() {
		position.setX(position.getX() - 1);
	}

	public void moveRight() {
		position.setX(position.getX() + 1);
	}

	
}
