package Artbomberman.Network;

import java.io.*;
import java.net.*;

import Artbomberman.logic.World;

public class Client implements Runnable{
	
	static Socket client;
	static DataOutputStream out;
	static DataInputStream  in;
	String message="";
	int NumWorld;
	boolean connected=false;
	boolean serverNotFounded=false;
	
	
	
	public void run() {
		
		try {
			client=new Socket("localHost",63789);
			in=new DataInputStream(client.getInputStream());
			out=new DataOutputStream(client.getOutputStream());
			connected=true;
			NumWorld=in.readInt();
		} catch (IOException e) {
			serverNotFounded=true;
		}
	}
	
	public void readMessage() {
	 try {
		message=in.readUTF();
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public int getNumWorld() {
		return this.NumWorld;
	}
	
	public void sendMessage(String messages) {
		try {
			out.writeUTF(messages);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean isConnected() {
		return connected;
	}


}
