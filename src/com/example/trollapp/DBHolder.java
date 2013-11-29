package com.example.trollapp;

import java.util.concurrent.atomic.AtomicReference;

import android.content.Context;

public class DBHolder {
	  private static AtomicReference<DBHelper> dbHelper = new AtomicReference<DBHelper>();

	    public static void init(Context context) {
	        dbHelper.set(new DBHelper(context));
	    }

	    public static  DBHelper getDBHelper() {
	        return dbHelper.get();
	    }
}
