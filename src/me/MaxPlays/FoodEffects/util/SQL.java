package me.MaxPlays.FoodEffects.util;

import me.MaxPlays.FoodEffects.main.FoodEffects;

import java.io.File;
import java.sql.*;


public class SQL {

	private String filename = "";
	
	private FoodEffects plugin;
	
	private static Connection con;

	public SQL(String filename, FoodEffects plugin){
		this.filename = filename;
		this.plugin = plugin;
	}
	
	public void connect(){
		if(connected())
			return;
		try{
				Class.forName("org.sqlite.JDBC");
				
				File dir = new File("plugins/" + plugin.getDescription().getName());
				if(!dir.exists()){
					dir.mkdir();
				}
				con = DriverManager.getConnection("jdbc:sqlite:plugins/" + plugin.getDescription().getName() + "/" + this.filename + ".db");
				System.out.println("[SQL] Connection established");
		}catch(Exception e){
			System.out.println("[SQL] Connection failed! Error: " + e.getMessage());
		}
	}
	
	public void disconnect(){
		try{
			if(connected()){
			con.close();
			System.out.println("[SQL] Disconnected");
			
			}
		
		}catch(SQLException e){
			System.out.println("[SQL] Error while disconnecting: " + e.getMessage());
		}
	}
	public boolean connected(){
		return con == null ? false : true;
	}
	public void update(String qry){
		try {
			Statement st = con.createStatement();
			st.executeUpdate(qry);
			st.close();
		} catch (SQLException e) {
			System.err.println(e);
		}
	}
	public ResultSet query(String qry){
		try {
			return con.createStatement().executeQuery(qry);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

