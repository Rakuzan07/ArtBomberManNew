package Artbomberman.Network;


import java.io.*;
import java.net.*;

import Artbomberman.logic.Player;
import Artbomberman.logic.World;

public class Server implements Runnable{

    static ServerSocket server;
	static Socket ClientSocket;
	static DataOutputStream out;
	static DataInputStream  in;
	String message="";
	boolean connected=false;
	
	

	
	static int port=63789;
	static boolean done=false;
	
	public Server()
	{
	}
	
	public void run() {
		try {
			server=new ServerSocket(port);
			ClientSocket=server.accept();
			out=new  DataOutputStream(ClientSocket.getOutputStream());
			in=new DataInputStream(ClientSocket.getInputStream());
			connected=true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readMessage() {
		 try {
			message=in.readUTF();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getMessage (){
		return this.message;
	}
	
	public void sendMessage(String messages) {
		try {
			out.writeUTF(messages);
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	
	public void sendNumWorld(int NumWorld) {
		try {
			out.writeInt(NumWorld);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		return connected;
	}
}
