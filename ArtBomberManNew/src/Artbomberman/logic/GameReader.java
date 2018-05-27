package Artbomberman.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class GameReader {
	
	private ArrayList<Player> players=new ArrayList<Player>();
	
	private World world;
	
	public void save() {
		
	}
	
	public void load(String nameFile) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(nameFile));
		ArrayList<Player> tempPlayers = new ArrayList<Player>();
		Block[][] tempBlockMatrix = null;
		String line = null;
		StringTokenizer st = null;
		boolean okReading = true;
		boolean firstLine = true;
		int i = 0, j = 0;
		for (;;) {
			line = br.readLine();
			if (line == null)
				break;
			if (firstLine)
				st = new StringTokenizer(line, "<,> ");
			else
				st = new StringTokenizer(line, " |");
			try {
				if (firstLine) {
					int dimWorld = Integer.parseInt(st.nextToken());
					tempBlockMatrix = new Block[dimWorld][dimWorld];
					while (st.hasMoreTokens()) {
						int x = Integer.parseInt(st.nextToken());
						int y = Integer.parseInt(st.nextToken());
						String color = st.nextToken();
						Color c = null;
						if (color.equalsIgnoreCase("BLUE"))
							c = Color.BLUE;
						else if (color.equalsIgnoreCase("RED"))
							c = Color.RED;
						else if (color.equalsIgnoreCase("GREEN"))
							c = Color.GREEN;
						tempPlayers.add(new Player(c, new Position(x, y)));
					}
					firstLine = false;
				} else {
					while (st.hasMoreTokens()) {
						Color c = null;
						String color = st.nextToken();
						if (color.equalsIgnoreCase("BLUE"))
							c = Color.BLUE;
						else if (color.equalsIgnoreCase("RED"))
							c = Color.RED;
						else if (color.equalsIgnoreCase("GREEN"))
							c = Color.GREEN;
						else if (color.equalsIgnoreCase("GREY"))
							c = Color.GREY;
						int cont = Integer.parseInt(st.nextToken());
						tempBlockMatrix[i][j] = new Block(c, cont);
						j++;
					}
					i++;
					j = 0;
				}
			} catch (Exception e) {
				okReading = false;
				break;
			}
		}
		br.close();
		if (okReading) {
			world = new World(tempPlayers,tempBlockMatrix.length);
			world.setBlockMatrix(tempBlockMatrix);
			players = tempPlayers;
		} else
			throw new IOException();
	}
	
	
	public void save(String nameFile,ArrayList<Player> players,World world) throws IOException {
		 File f=new File(nameFile);
		 if (!f.exists()) f.createNewFile();
        PrintWriter pw=new PrintWriter(new FileWriter(nameFile));
        pw.print(world.getDimension()+" ");
        for(int i=0;i<players.size();i++) {
       	 pw.print("<"+players.get(i).getX()+","+players.get(i).getY()+"> ");
       	 pw.print(players.get(i).getColor()+" ");
        }
        pw.println();
        Block[][] blockMatrix=world.getBlockMatrix();
        for(int i=0;i<world.getDimension();i++) {
       	 for(int j=0;j<world.getDimension();j++) {
       		 Color c=blockMatrix[i][j].getColor();
       		 int cont=blockMatrix[i][j].getBreakCont();
       		 pw.write(c+"|"+cont+" ");
       	 }
       	 pw.println();
        }
        pw.close();
	}
	
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public World getWorld() {
		return world;
	}
	
}
