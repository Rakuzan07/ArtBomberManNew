package Artbomberman.Network;

import java.io.*;
import java.net.*;

import Artbomberman.logic.World;

public class Client implements Runnable{
	
	static Socket client;
	static ObjectInputStream in;
	static ObjectOutputStream out;
	boolean connected=false;

	boolean serverNotFounded=false;
	World world;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Connecting");
		
	}
	
	public void run() {
		
		try {
			client=new Socket("localHost",63789);
			in=new ObjectInputStream(client.getInputStream());
			out=new ObjectOutputStream(client.getOutputStream());
			world=(World)in.readObject();
			connected=true;
		} catch (IOException | ClassNotFoundException e) {
			serverNotFounded=true;
		}
		
		while(true)
		{
			try {
				world=(World)in.readObject();
			} catch (IOException | ClassNotFoundException e) {
				serverNotFounded=true;
			}
		}
	}
	
	World getWorld() {
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
