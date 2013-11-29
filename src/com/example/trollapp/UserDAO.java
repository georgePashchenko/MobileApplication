package com.example.trollapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDAO {
	
	String UserName = null;
	String Password = null;
	String TableUsers = "people";
    String TableUsersData = "peopledata";

    final String LOG_TAG = "myLogsUserDAO";
    
    public UserDAO (){
    }
    

    public User addNewUser(User user){
    	SQLiteDatabase db = DBHolder.getDBHelper().getWritableDatabase(); 
    	ContentValues cv = new ContentValues();
    	cv.clear();

		cv.put("name", user.getName());      
		cv.put("password", user.getPassword());

		if (!isExist(user)){
			long rowID = db.insert(TableUsers, null, cv); 
			MainActivity.ID = (int) rowID;
			Log.d(LOG_TAG, "name = " + UserName + " password = " + Password + " row inserted, ID = " + rowID);      
			user.setID((int)rowID);
			return user;
		}
		else {
			return user;
		}
    }    
    
    public boolean isExist(User user){
    	SQLiteDatabase db = DBHolder.getDBHelper().getWritableDatabase(); 
    	ContentValues cv = new ContentValues();
    	cv.clear();
    	String selection = null;
		String[] selectionArgs = null;
		boolean result = false;

		Log.d(LOG_TAG, "--- Checking for user with NAME = " + UserName);
		selection = "name = ?";
		selectionArgs  = new String[] {user.getName()};

		Cursor c = db.query(TableUsers, null, selection, selectionArgs, null, null, null);
		if (c != null){
			if (c.moveToFirst()) {
				int id = c.getColumnIndex("id");
				Log.d(LOG_TAG, "User already exist ID = " + c.getString(id));
				result = true;
			}
			else{      
				result = false;
			}
		}
		c.close();
		return result;
    }

    public List<User> getAllUsers(){
    	List <User> users= new ArrayList();
    	
    	SQLiteDatabase db = DBHolder.getDBHelper().getWritableDatabase(); 
    	ContentValues cv = new ContentValues();
		
		Log.d(LOG_TAG, "--- Reading users data ---");
		Cursor c = db.query(TableUsers, null, null, null, null, null, null);
		if (c != null) {
			if (c.moveToFirst()) {
				int idColIndex = c.getColumnIndex("id");        
				int nameColIndex = c.getColumnIndex("name");        
				int passColIndex = c.getColumnIndex("password");  
								
				do {
					User user = new User();
					user.setID( Integer.parseInt(c.getString(idColIndex)));
					user.setName(c.getString(nameColIndex));
					user.setPassword(c.getString(passColIndex));
					users.add(user);
					Log.d(LOG_TAG, "User ID = " + c.getString(idColIndex) + 
							" UserName = " + c.getString(nameColIndex));
				} while (c.moveToNext());
			}
		} else
			Log.d(LOG_TAG, "Cursor is null");
		c.close();
		
		return users;
    }
   
	public List<String> getUserData (int UserId){
		List <String> UserData= new ArrayList();
    	
    	SQLiteDatabase db = DBHolder.getDBHelper().getWritableDatabase(); 
    	ContentValues cv = new ContentValues();
		String selection = null;
		String[] selectionArgs = null;
		
		Log.d(LOG_TAG, "--- Reading user (" +UserId+  ") data --- ");
		selection = "posid = ?";
		selectionArgs  = new String[] { Integer.toString(UserId)};
		String columns[] = { "posid"};
		Cursor c = db.query(TableUsersData, null, selection, selectionArgs, null, null, null);
		if (c != null) {
			if (c.moveToFirst()) {
				int idColData = c.getColumnIndex("data");        
				//int nameColIndex = c.getColumnIndex("name");        
				//int passColIndex = c.getColumnIndex("password");  
								
				do {
					String data = c.getString(idColData);
					UserData.add(data);
					Log.d(LOG_TAG, "UserFile = " + data);
				} while (c.moveToNext());
			}
		} else
			Log.d(LOG_TAG, "Cursor is null");
		c.close();
		return UserData;
	}
	
	public void clearDB(){
		SQLiteDatabase db = DBHolder.getDBHelper().getWritableDatabase();
		int clearCount = db.delete("people", null, null);
		Log.d(LOG_TAG, "deleted rows = "+ clearCount);

		int clearCount2 = db.delete("peopledata", null, null);
		Log.d(LOG_TAG, "deleted rows = "+ clearCount2);
	}
	
}
