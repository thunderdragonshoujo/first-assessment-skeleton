package com.cooksys.assessment.model;

import com.cooksys.assessment.server.ClientHandler;

public class Message {

	private String timeStamp;
	private String username;
	private String command;
	private String contents;
	
	
	
	/*
		LocalDateTime now = LocalDateTime.now();
        System.out.println("Before : " + now);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);
        System.out.println("After : " + formatDateTime);
        return formatDateTime;
        */


	public String getTimeStamp() {
		timeStamp = ClientHandler.printTimeStamp();
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getContents() {
		return getTimeStamp() + " " + getUsername() + " "  + ClientHandler.getCommandNameToDisplay(getCommand()) + " " + contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

}
