package com.example.trollapp;

public class User {

	private int ID;
	private String UserName;
	private String Password;
	
	public User (){}
	
	public User(String UserName, String Password){
		this.UserName = UserName;
		this.Password = Password;
	}
	
	public int getID(){
		return ID;
	}
	
	public String getName(){
		return UserName;
	}
	
	public String getPassword(){
		return this.Password;
	}
	
	public void setID(int ID){
		this.ID = ID;
	}
	
	public void setName(String UserName){
		this.UserName = UserName;
	}
	
	public void setPassword( String Password){
		this.Password = Password;
	}
	

}
