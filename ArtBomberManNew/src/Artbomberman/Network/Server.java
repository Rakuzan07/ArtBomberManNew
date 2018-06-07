package Artbomberman.Network;


import java.io.*;
import java.net.*;

import Artbomberman.logic.Player;
import Artbomberman.logic.World;

public class Server implements Runnable{

    static ServerSocket server;
	static Socket ClientSocket;
	static ObjectOutputStream out;
	static ObjectInputStream  in;
	boolean connected=false;
	
	

	World world;
	Player playerOne;
	Player playerTwo;
	
	static int port=63789;
	static boolean done=false;
	
	public Server(World world)
	{
		this.world=world;
		
	}
	
	public void run() {
		try {
			server=new ServerSocket(port);
			ClientSocket=server.accept();
			out=new  ObjectOutputStream(ClientSocket.getOutputStream());
			in=new ObjectInputStream(ClientSocket.getInputStream());
			out.writeObject(world);
			connected=true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(true) {
			try {
				world=(World) in.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public World getWorld () {
		return this.world;
	}
	
	public void setWorld(World world) {
		this.world=world;
	}
	
	public void sendWorld(World world) {
		try {
			out.writeObject(world);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
