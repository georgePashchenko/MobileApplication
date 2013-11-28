package com.example.trollapp;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

class DBHelper extends SQLiteOpenHelper {

	final String LOG_TAG = "myLogsSQL";
	ContentValues cv = new ContentValues();


	// данные для таблицы авторизации
	int[] user_id = { 1, 2, 3};
	String[] user_name = { "user1", "user2", "user3"};
	String[] user_password = { "pass1", "pass2", "pass3"};

	// данные для таблицы данных пользователей
	String[] user_data = { "user1_data1", "user2_data1", "user2_data2", "user3_data1", "user3_data2",
			"user3_data3", "user1_data2", "user1_data3" };
	int[] user_pos = { 1, 2, 2, 3, 3, 3, 1, 1 };

	//конструктор
	public DBHelper(Context context) {
		super(context, "myDB", null, 1);
	}

	public void onCreate(SQLiteDatabase db) {
		Log.d(LOG_TAG, "--- onCreate database ---");



		// создаем таблицу авторизации
		db.execSQL("create table people (" 
				+ "id integer primary key,"
				+ "name text," + "password text" 
				+ ");");



		// создаем таблицу данных пользователей
		db.execSQL("create table peopledata ("
				+ "id integer primary key autoincrement," 
				+ "data text,"
				+ "posid integer" 
				+ ");");


	}

	//инициализация начальных значений БД
	public void setFill(SQLiteDatabase db){


		//очищаем таблицы
		setClean(db);

		// заполняем таблицу авторизации
		for (int i = 0; i < user_name.length; i++) {
			cv.clear();
			cv.put("name", user_name[i]);
			cv.put("password", user_password[i]);
			db.insert("people", null, cv);
		}

		// заполняем таблицу данных пользователей
		for (int i = 0; i < user_data.length; i++) {

			cv.clear();
			cv.put("data", user_data[i]);
			cv.put("posid", user_pos[i]);
			db.insert("peopledata", null, cv);
		}
	}

	//очистка таблиц
	public void setClean(SQLiteDatabase db){
		int clearCount = db.delete("people", null, null);
		Log.d(LOG_TAG, "deleted rows = "+ clearCount);

		int clearCount2 = db.delete("peopledata", null, null);
		Log.d(LOG_TAG, "deleted rows = "+ clearCount2);
	}
	//Проверка наличия пользователя с указанным именем
	public boolean getCheck(SQLiteDatabase db, String UserName){
		String selection = null;
		String[] selectionArgs = null;
		String[] colums = null;
		boolean result = false;

		Log.d(LOG_TAG, "--- Checking for user with NAME = " + UserName);
		selection = "name = ?";
		selectionArgs  = new String[] {UserName};

		Cursor c = db.query("people", null, selection, selectionArgs, null, null, null);
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
	//Верификация пользователя
	public boolean getCheck(SQLiteDatabase db, String UserName, String Password){
		String selection = null;
		String[] selectionArgs = null;
		String[] colums = null;
		boolean result = false;

		Log.d(LOG_TAG, "--- Checking for user with NAME = " + UserName);
		selection = "name = ? and password = ?";
		selectionArgs  = new String[] {UserName, Password};

		Cursor c = db.query("people", null, selection, selectionArgs, null, null, null);
		if (c != null){
			if (c.moveToFirst()) {
				int id = c.getColumnIndex("id");
				Log.d(LOG_TAG, "Access granted. User ID= " + c.getString(id));
				MainActivity.ID = id;

				result = true;
			}
			else{     
				Log.d(LOG_TAG, "Access denied");
				result = false;
			}
		}

		c.close();
		return result;
	}
	//Добавление пользователя
	public boolean AddUser(SQLiteDatabase db,  String UserName, String Password){

		cv.clear();
		cv.put("name", UserName);      
		cv.put("password", Password);

		if (!getCheck(db, UserName)){
			long rowID = db.insert("people", null, cv); 
			MainActivity.ID = (int) rowID;
			Log.d(LOG_TAG, "name = " + UserName + " password = " + Password + " row inserted, ID = " + rowID);      
			return true;
		}
		else {
			return false;
		}
	}
	//Сохранение данных пользователя
	public void SaveUserData(SQLiteDatabase db, int UserId, String Data){

		cv.clear();
		cv.put("data", Data);
		cv.put("posid", UserId);
		db.insert("peopledata", null, cv);
		Log.d(LOG_TAG, "Data saved: ID = " + UserId + " Data = " + Data);
	}

	public void getReadUserData(SQLiteDatabase db, int UserId){
		String selection = null;
		String[] selectionArgs = null;
		String[] colums = null;
		
		Log.d(LOG_TAG, "--- Reading user data ---");
		selection = "posid = ?";
		selectionArgs  = new String[] { Integer.toString(UserId)};
		String columns[] = { "posid"};
		Cursor c = db.query("peopledata", null, selection, selectionArgs, null, null, null);
		logCursor(c);
		c.close();

	}
	
	// вывод в лог данных из курсора
		void logCursor(Cursor c) {
			if (c != null) {
				if (c.moveToFirst()) {
					String str;
					do {
						str = "";
						for (String cn : c.getColumnNames()) {
							str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
						}
						Log.d(LOG_TAG, str);
					} while (c.moveToNext());
				}
			} else
				Log.d(LOG_TAG, "Cursor is null");
		}
		
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}