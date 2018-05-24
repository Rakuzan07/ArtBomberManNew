package Artbomberman.logic;

import java.util.ArrayList;

import org.omg.Messaging.SyncScopeHelper;

import Artbomberman.logic.Player.Status;

public class World {

	private Block[][] blockMatrix;

	private ArrayList<Player> players;

	private static final int DIM_WORLD = 50;

	private static final double VICTORY_PERCENTAGE = 0.4;

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

	public int getDimension() {
		return dimWorld;
	}
	
	public Block[][] getBlockMatrix(){
		return blockMatrix;
	}
	
	public void reloadTank(Player p) {
		if(p.getColor().equals(blockMatrix[p.getY()][p.getX()].getColor()))p.reloadTank();
	}

	public void paint(Position p, Color color) {
		if (p.getX() < 0 || p.getX() > dimWorld || p.getY() < 0 || p.getY() > dimWorld)
			throw new IllegalArgumentException();
		int x = p.getX();
		int y = p.getY();
		blockMatrix[y][x].setColor(color);
		if (y - 1 >= 0) {
			if (blockMatrix[y - 1][x].getBreakCont() == 0)
				blockMatrix[y - 1][x].setColor(color);
			else if (blockMatrix[y - 1][x].getBreakCont() == 1) {
				blockMatrix[y - 1][x].decrBreakCont();
				blockMatrix[y - 1][x].setColor(Color.GREY);
			} else {
				blockMatrix[y - 1][x].repaint();
			}
		}
		if (y - 1 >= 0 && x - 1 >= 0)
			if (blockMatrix[y - 1][x - 1].getBreakCont() == 0)
				blockMatrix[y - 1][x - 1].setColor(color);
			else if (blockMatrix[y - 1][x - 1].getBreakCont() == 1) {
				blockMatrix[y - 1][x - 1].decrBreakCont();
				blockMatrix[y - 1][x - 1].setColor(Color.GREY);
			} else {
				blockMatrix[y - 1][x - 1].repaint();
			}
		if (y - 1 >= 0 && x + 1 < dimWorld)
			if (blockMatrix[y - 1][x + 1].getBreakCont() == 0)
				blockMatrix[y - 1][x + 1].setColor(color);
			else if (blockMatrix[y - 1][x + 1].getBreakCont() == 1) {
				blockMatrix[y - 1][x + 1].decrBreakCont();
				blockMatrix[y - 1][x + 1].setColor(Color.GREY);
			} else {
				blockMatrix[y - 1][x + 1].repaint();
			}
		if (x - 1 >= 0)
			if (blockMatrix[y][x - 1].getBreakCont() == 0)
				blockMatrix[y][x - 1].setColor(color);
			else if (blockMatrix[y][x - 1].getBreakCont() == 1) {
				blockMatrix[y][x - 1].decrBreakCont();
				blockMatrix[y][x - 1].setColor(Color.GREY);
			} else {
				blockMatrix[y][x - 1].repaint();
			}
		if (x + 1 < dimWorld)
			if (blockMatrix[y][x + 1].getBreakCont() == 0)
				blockMatrix[y][x + 1].setColor(color);
			else if (blockMatrix[y][x + 1].getBreakCont() == 1) {
				blockMatrix[y][x + 1].decrBreakCont();
				blockMatrix[y][x + 1].setColor(Color.GREY);
			} else {
				blockMatrix[y][x + 1].repaint();
			}
		if (y + 1 < dimWorld)
			if (blockMatrix[y + 1][x].getBreakCont() == 0)
				blockMatrix[y + 1][x].setColor(color);
			else if (blockMatrix[y + 1][x].getBreakCont() == 1) {
				blockMatrix[y + 1][x].decrBreakCont();
				blockMatrix[y + 1][x].setColor(Color.GREY);
			} else {
				blockMatrix[y + 1][x].repaint();
			}
		if (y + 1 < dimWorld && x - 1 >= 0)
			if (blockMatrix[y + 1][x - 1].getBreakCont() == 0)
				blockMatrix[y + 1][x - 1].setColor(color);
			else if (blockMatrix[y + 1][x - 1].getBreakCont() == 1) {
				blockMatrix[y + 1][x - 1].decrBreakCont();
				blockMatrix[y + 1][x - 1].setColor(Color.GREY);
			} else {
				blockMatrix[y + 1][x - 1].repaint();
			}
		if (y + 1 < dimWorld && x + 1 < dimWorld)
			if (blockMatrix[y + 1][x + 1].getBreakCont() == 0)
				blockMatrix[y + 1][x + 1].setColor(color);
			else if (blockMatrix[y + 1][x + 1].getBreakCont() == 1) {
				blockMatrix[y + 1][x + 1].decrBreakCont();
				blockMatrix[y + 1][x + 1].setColor(Color.GREY);
			} else {
				blockMatrix[y + 1][x + 1].repaint();
			}
	}
	
	public boolean checkVictory(Player p) {
		if (!players.contains(p))
			throw new IllegalArgumentException("The player does not exist in this world");
		int cont = 0;
		for (int i = 0; i < dimWorld; i++) {
			for (int j = 0; j < dimWorld; j++) {
				if (blockMatrix[i][j].getColor()==p.getColor())
					cont++;
			}
		}
		return ((double) cont / dimWorld) >= VICTORY_PERCENTAGE;
	}
	
	public double checkMatrix(Color color,int x, int y) {
		int posPlayer=players.indexOf(new Player(color,new Position(0,0),Status.DOWN));
		ArrayList<Position> bomb=players.get(posPlayer).getBombPosition();
		double cont = -returncontBomb(bomb,x,y);
		if (blockMatrix[y][x].getColor() != color) {
			if(!blockMatrix[y][x].isPhysical())cont++;
			else cont=cont+1/3;}
		if (y - 1 >= 0 && blockMatrix[y - 1][x].getColor() != color) {
			if(!blockMatrix[y-1][x].isPhysical())cont++;
			else cont=cont+1/3;}
		if (y + 1 < blockMatrix.length && blockMatrix[y + 1][x].getColor() != color) {
			if(!blockMatrix[y+1][x].isPhysical())cont++;
			else cont=cont+1/3;}
		if (y - 1 >= 0 && x - 1 >= 0 && blockMatrix[y - 1][x - 1].getColor() != color) {
			if(!blockMatrix[y-1][x-1].isPhysical())cont++;
			else cont=cont+1/3;}
		if (y - 1 >= 0 && x + 1 < blockMatrix.length && blockMatrix[y - 1][x + 1].getColor() != color) {
			if(!blockMatrix[y-1][x+1].isPhysical())cont++;
			else cont=cont+1/3;}
		if (x - 1 >= 0 && blockMatrix[y][x - 1].getColor() != color) {
			if(!blockMatrix[y][x-1].isPhysical())cont++;
			else cont=cont+1/3;}
		if (x + 1 < blockMatrix.length && blockMatrix[y][x + 1].getColor() != color) {
			if(!blockMatrix[y][x+1].isPhysical())cont++;
			else cont=cont+1/3;}
		if (y + 1 < blockMatrix.length && x - 1 >= 0 && blockMatrix[y + 1][x - 1].getColor() != color) {
			if(!blockMatrix[y+1][x-1].isPhysical())cont++;
			else cont=cont+1/3;}
		if (y + 1 < blockMatrix.length && x + 1 < blockMatrix.length && blockMatrix[y + 1][x + 1].getColor() != color) {
			if(!blockMatrix[y+1][x+1].isPhysical())cont++;
			else cont=cont+1/3;}
		return cont;
	}
	
	private int returncontBomb(ArrayList<Position> bomb , int x , int y) {
		if(bomb.size()==0) return 0;
		ArrayList<Position> posBombs=tryExplosion(bomb.get(0),x,y);
		for(int i=1;i<bomb.size();i++) {
			ArrayList<Position> tempPosition=tryExplosion(bomb.get(i),x,y);
			for(int j=0;j<tempPosition.size();j++) {
				if(!posBombs.contains(tempPosition.get(j))) posBombs.add(tempPosition.get(j));
			}
		}
		/*for(int i=0;i<posBombs.size();i++) {
			System.out.print(posBombs.get(i));
		}*/
		return posBombs.size();
	}
	
	private ArrayList<Position> tryExplosion(Position bomb , int nx , int ny){
		ArrayList<Position> tempPosition=new ArrayList<Position>();
		int x=bomb.getX();
		int y=bomb.getY();
		/*System.out.println((Math.abs(nx-x)<=1&&Math.abs(ny-y)<=1)+" <nx,x>:<"+nx+","+x+"> <ny,y>:<"+ny+","+y+">");
		System.out.println((Math.abs(nx-x)<=1&&Math.abs(ny-y-1)<=1)+" <nx,x>:<"+nx+","+x+"> <ny,y>:<"+ny+","+(y-1)+">");
		System.out.println((Math.abs(nx-x)<=1&&Math.abs(ny-y+1)<=1)+" <nx,x>:<"+nx+","+x+"> <ny,y>:<"+ny+","+(y+1)+">");
		System.out.println((Math.abs(nx-x-1)<=1&&Math.abs(ny-y-1)<=1)+" <nx,x>:<"+nx+","+(x-1)+"> <ny,y>:<"+ny+","+(y-1)+">");
		System.out.println((Math.abs(nx-x+1)<=1&&Math.abs(ny-y-1)<=1)+" <nx,x>:<"+nx+","+(x+1)+"> <ny,y>:<"+ny+","+(y-1)+">");
		System.out.println((Math.abs(nx-x-1)<=1&&Math.abs(ny-y)<=1)+" <nx,x>:<"+nx+","+(x-1)+"> <ny,y>:<"+ny+","+y+">");
		System.out.println((Math.abs(nx-x+1)<=1&&Math.abs(ny-y)<=1)+" <nx,x>:<"+nx+","+(x+1)+"> <ny,y>:<"+ny+","+y+">");
		System.out.println((Math.abs(nx-x-1)<=1&&Math.abs(ny-y+1)<=1)+" <nx,x>:<"+nx+","+(x-1)+"> <ny,y>:<"+ny+","+(y+1)+">");
		System.out.println((Math.abs(nx-x)<=1&&Math.abs(ny-y)<=1)+" <nx,x>:<"+nx+","+x+"> <ny,y>:<"+ny+","+y+">");*/
		
		if(Math.abs(nx-x)<=1&&Math.abs(ny-y)<=1)tempPosition.add(new Position(x,y));
		if (y - 1 >= 0 &&Math.abs(ny-(y-1))<=1 && Math.abs(nx-x)<=1) tempPosition.add(new Position(x,y-1));
		if (y + 1 < blockMatrix.length&&Math.abs(ny-(y+1))<=1 && Math.abs(nx-x)<=1) tempPosition.add(new Position(x,y+1));
		if (y - 1 >= 0 && x - 1 >= 0 && Math.abs(ny-(y-1))<=1 && Math.abs(nx-(x-1))<=1) tempPosition.add(new Position(x-1,y-1));
		if (y - 1 >= 0 && x + 1 < blockMatrix.length && Math.abs(ny-(y-1))<=1 && Math.abs(nx-(x+1))<=1)tempPosition.add(new Position(x+1,y-1));
		if (x - 1 >= 0 && Math.abs(ny-y)<=1 && Math.abs(nx-(x-1))<=1) tempPosition.add(new Position(x-1,y));
		if (x + 1 < blockMatrix.length && Math.abs(ny-y)<=1 && Math.abs(nx-(x+1))<=1) tempPosition.add(new Position(x+1,y));
		if (y + 1 < blockMatrix.length && x - 1 >= 0 && Math.abs(ny-(y+1))<=1 && Math.abs(nx-(x-1))<=1 ) tempPosition.add(new Position(x-1,y+1));
		if (y + 1 < blockMatrix.length && x + 1 < blockMatrix.length && Math.abs(ny-(y+1))<=1 && Math.abs(nx-(x+1))<=1 ) tempPosition.add(new Position(x+1,y+1));
		return tempPosition;
	}
	
	public double checkMatrixTemp(Color color,int x,int y) {
		double cont = 0;
		if (blockMatrix[y][x].getColor() != color) {
			if(!blockMatrix[y][x].isPhysical())cont++;
			else cont=cont+1/3;}
		if (y - 1 >= 0 && blockMatrix[y - 1][x].getColor() != color) {
			if(!blockMatrix[y-1][x].isPhysical())cont++;
			else cont=cont+1/3;}
		if (y + 1 < blockMatrix.length && blockMatrix[y + 1][x].getColor() != color) {
			if(!blockMatrix[y+1][x].isPhysical())cont++;
			else cont=cont+1/3;}
		if (y - 1 >= 0 && x - 1 >= 0 && blockMatrix[y - 1][x - 1].getColor() != color) {
			if(!blockMatrix[y-1][x-1].isPhysical())cont++;
			else cont=cont+1/3;}
		if (y - 1 >= 0 && x + 1 < blockMatrix.length && blockMatrix[y - 1][x + 1].getColor() != color) {
			if(!blockMatrix[y-1][x+1].isPhysical())cont++;
			else cont=cont+1/3;}
		if (x - 1 >= 0 && blockMatrix[y][x - 1].getColor() != color) {
			if(!blockMatrix[y][x-1].isPhysical())cont++;
			else cont=cont+1/3;}
		if (x + 1 < blockMatrix.length && blockMatrix[y][x + 1].getColor() != color) {
			if(!blockMatrix[y][x+1].isPhysical())cont++;
			else cont=cont+1/3;}
		if (y + 1 < blockMatrix.length && x - 1 >= 0 && blockMatrix[y + 1][x - 1].getColor() != color) {
			if(!blockMatrix[y+1][x-1].isPhysical())cont++;
			else cont=cont+1/3;}
		if (y + 1 < blockMatrix.length && x + 1 < blockMatrix.length && blockMatrix[y + 1][x + 1].getColor() != color) {
			if(!blockMatrix[y+1][x+1].isPhysical())cont++;
			else cont=cont+1/3;}
		return cont;
	}

	public static void fillWay(Position departure,Position arrival,World world,ArrayList<Position> tempPosition) {
		int x=departure.getX();
		int y=departure.getY();
		if(x-1>=0&&world.getBlockMatrix()[y][x-1].getBreakCont()==0) tempPosition.add(new Position(x-1,y));
		if(x+1<world.getDimension()&&world.getBlockMatrix()[y][x+1].getBreakCont()==0) tempPosition.add(new Position(x+1,y));
		if(y-1>=0&&world.getBlockMatrix()[y-1][x].getBreakCont()==0) tempPosition.add(new Position(x,y-1));
		if(y+1<world.getDimension()&&world.getBlockMatrix()[y+1][x].getBreakCont()==0) tempPosition.add(new Position(x,y+1));
		if(x-1>=0&&y-1>=0&&world.getBlockMatrix()[y-1][x-1].getBreakCont()==0) tempPosition.add(new Position(x-1,y-1));
		if(x+1<world.getDimension()&&y-1>=0&&world.getBlockMatrix()[y-1][x+1].getBreakCont()==0) tempPosition.add(new Position(x+1,y-1));
		if(x-1>=0&&y+1<world.getDimension()&&world.getBlockMatrix()[y+1][x-1].getBreakCont()==0) tempPosition.add(new Position(x-1,y+1));
		if(x+1<world.getDimension()&&y+1<world.getDimension()&&world.getBlockMatrix()[y+1][x+1].getBreakCont()==0) tempPosition.add(new Position(x+1,y+1));
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}

}
