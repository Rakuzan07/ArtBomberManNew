package Artbomberman.logic;

import java.util.Random;

public class Block {
	
	private Color color;
	
	private int breakCont;
	
	private Color[] arrayColor;
	
	public Block(Color color) {
		this.color=color;
		breakCont=0;
		arrayColor=Color.values();
	}
	
	public Block(Color color ,  int breakCont) {
		this.color=color;
		this.breakCont=breakCont;
		arrayColor=Color.values();
	}
	
	public boolean isPhysical() {
		return breakCont>0;
	}
	
	
	public void repaint() {
		Random random=new Random();
		Color tempColor=color;
		while(color.equals(tempColor)) {
			int index=random.nextInt(arrayColor.length);
			tempColor=arrayColor[index];
		}
		breakCont--;
		color=tempColor;
	}
	
	public void setColor(Color color) {
		this.color=color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getBreakCont() {
		return breakCont;
	}
	
	public void setBreakCont(int breakCont) {
		this.breakCont=breakCont;
	}
	
	public void incrBreakCont() {
		breakCont++;
	}
	
	public void decrBreakCont() {
		breakCont--;
	}
	
	
}
