package Artbomberman.logic;

import java.util.ArrayList;


public class Player {

	private static final int TANK_VALUE = 4;

	private int inkTank, numBomb;  //INKTANK NUMERO DI BOMBE UTILIZZABILI, NUMBOMB NUMERO MAX DI BOMBE UTILIZZABILI

	private Color color;

	private Position position;

	private ArrayList<Position> bombPosition; //ARRAY DI POSIZIONI DELLE BOMBE PER NON PIAZZARLE NELLA STESSA POSIZIONE

	private ArrayList<Integer> contBomb; //ARRAY DI CONT CORRISPONTENDI AD OGNI BOMBA PIAZZATA, QUANDO IL CONT DELLA BOMBA E' MAGG UGUALE A 7 LA BOMBA ESPLODE
	
	public enum Status{IDLE,UP,DOWN,LEFT,RIGHT} //UTILIZZATO PER GEST LE ANIMAZIONI
	
	private Status state;
	
	public Player(Color color, Position position) {
		this.color = color;
		this.numBomb = this.inkTank = TANK_VALUE;
		this.position = new Position(position);
		this.state=Status.IDLE;
		bombPosition = new ArrayList<>();
		contBomb=new ArrayList<Integer>();
	}

	public Player(Color color, Position position, int inkTank) {
		this.color = color;
		this.numBomb = this.inkTank = inkTank;
		this.position = new Position(position);
		this.state=Status.IDLE;
		bombPosition = new ArrayList<>();
		contBomb=new ArrayList<Integer>();
	}

	public Player(Player p) {
		this.color = p.color;
		this.numBomb = this.inkTank = p.inkTank;
		this.state=Status.IDLE;
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
	
	public void setInkTank(int value) {
		inkTank=value;
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
		Position position=new Position(p.getX(),p.getY());
		if(inkTank>0) {
			boolean okInsert=true;
			for(int i=0;i<bombPosition.size();i++){
				if(bombPosition.get(i).equals(position))okInsert=false;
			}
			if(okInsert){
				inkTank--;
				bombPosition.add(position);
				contBomb.add(0);
			}
			return okInsert;
		}
		return false;
	}
	
	public ArrayList<Integer> getContBomb() {
		return contBomb;
	}
	
	public void increaseContBomb() {
		for(int i=0;i<contBomb.size();i++) {
			contBomb.set(i, contBomb.get(i)+1);
		}
	}
	
	public ArrayList<Position> removeBomb(int cont) {  //SE IL CONT DELLE BOMBE E' MAGGIORE DI 7 ELIMINA LE BOMBE PERCHE' ESPLOSE DALL'ARRAY DI POS E REST UN ARRAY CON LE POS DELLE BOMBE ESPLOSE PER RICOLORARE LE POSIZIONI
		ArrayList<Position> tempPos=new ArrayList<Position>();
		for(int i=0;i<bombPosition.size();) {
			if(contBomb.get(i)>=cont) {
				tempPos.add(bombPosition.remove(i));
				contBomb.remove(i);
			}
			else return tempPos;
		}
		return tempPos;
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
