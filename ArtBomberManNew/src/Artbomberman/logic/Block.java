package Artbomberman.logic;

import java.util.Random;

public class Block {
	
	private Color color;
	
	private int breakCont; //CONTATORE CHE CONTROLLA LO STATO DEL BLOCCO. SE IL CONT E' 0 IL BLOCCO VIENE DISTRUTTO
	
	private Color[] arrayColor; //ARRAY DI COLORI UTILIZZATO PER CAMBIARE IL COLORE AL BLOCCO UNA VOLTA COLPITO
	
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
	
	public boolean isPhysical() { //CAPIAMO SE IL BLOCCO E' ESISTENTE O MENO
		return breakCont>0;
	}
	
	
	public void repaint() {
		Random random=new Random();
		Color tempColor=color;
		while(color.equals(tempColor)&&color==Color.GREY) {
			int index=random.nextInt(arrayColor.length); //L'INDICE PUO' ASSUMERE VALORE COMPRESO TRA 0 E ARRAYCOLOR.LENGHT()-1
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
