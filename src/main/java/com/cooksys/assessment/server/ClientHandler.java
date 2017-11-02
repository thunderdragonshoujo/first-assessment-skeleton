package com.cooksys.assessment.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.assessment.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler implements Runnable {
	private Logger log = LoggerFactory.getLogger(ClientHandler.class);

	private Socket socket;
	private static ArrayList<String> connectedUsers = new ArrayList<String>();
	 private static Map<String, Socket> connectedSocketMap = new HashMap<String, Socket>();

	public ClientHandler(Socket socket) {
		super();
		this.socket = socket;
	}
	public void printConnectedUsers(){
		System.out.println("berfoe the loop" + connectedUsers.size());
		for(int i = 0; i < connectedUsers.size(); i++) {   
		    System.out.println(connectedUsers.get(i));
		} 
	}
	public ArrayList<String> listConnectedUsers(){
		return connectedUsers; 
	}
	public String listConnectedUsersAsString(){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < connectedUsers.size(); i++) {   
		    System.out.println(connectedUsers.get(i));
		    sb.append(connectedUsers.get(i) + " ");
		}
		return sb.toString();
	}
	public void broadcastMessage(Message message)throws Exception {
		ObjectMapper mapper = null;
		PrintWriter writer = null;
		Socket socket = null;
		for(Map.Entry<String, Socket> connectedSocketMap : connectedSocketMap.entrySet()) {   
		    socket = connectedSocketMap.getValue();
		     mapper = new ObjectMapper();
			 writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			 String broadcastMesg = mapper.writeValueAsString(message);
				writer.write(broadcastMesg);
				writer.flush();
		}
	}
	public void directMessage(Message message)throws Exception {
		ObjectMapper mapper = null;
		PrintWriter writer = null;
		Socket socket = null;
		String username = null;
		for(Map.Entry<String, Socket> connectedSocketMap : connectedSocketMap.entrySet()) {
			username = connectedSocketMap.getKey();
			if(username == message.getUsername()) { 
		    socket = connectedSocketMap.getValue();
		     mapper = new ObjectMapper();
			 writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			 String directMsg = mapper.writeValueAsString(message);
				writer.write(directMsg);
				writer.flush();
			}
		}
	}

	public void run() {
		try {

			ObjectMapper mapper = new ObjectMapper();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			while (!socket.isClosed()) {
				String raw = reader.readLine();
				Message message = mapper.readValue(raw, Message.class);

				switch (message.getCommand()) {
					case "connect":
						log.info("user <{}> connected", message.getUsername());
						System.out.println("before" + connectedUsers.size());
						connectedUsers.add (message.getUsername());
						connectedSocketMap.put(message.getUsername(),socket);
						System.out.println("after" + connectedUsers.size());
						message.setContents(message.getUsername() + " has connected");
						broadcastMessage(message);
						break;
					case "disconnect":
						log.info("user <{}> disconnected", message.getUsername());
						this.socket.close();
						break;
					case "echo":
						log.info("user <{}> echoed message <{}>", message.getUsername(), message.getContents());
						String response = mapper.writeValueAsString(message);
						writer.write(response);
						writer.flush();
						break;
					case "broadcast":
						log.info("user <{}> broadcasted echoed message <{}>", message.getUsername(), message.getContents());
						broadcastMessage(message);
						break;
					case "users":
						log.info("user <{}> Users Listed <{}>", message.getUsername());
						message.setContents(listConnectedUsersAsString());
						String userContent = mapper.writeValueAsString(message);
						writer.write(userContent);
						writer.flush();
						break;
					case "@username":
						log.info("user <{}> direct message to user <{}>", message.getUsername(), message.getContents());
						log.info("raw is " + raw);
						directMessage(message);
						break;
				}
			}

		} catch (Exception e) {
			log.error("Something went wrong :/", e);
		}
	}

}
