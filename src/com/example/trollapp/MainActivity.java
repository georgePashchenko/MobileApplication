package com.example.trollapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	public static int ID = 0;

	final String LOG_TAG = "myLogsMain";

	Button btnLog, btnReg, btnRead;
	EditText etLogin,etPassword;

	DBHelper dbh = new DBHelper(this);

	/** Called when the activity is first created. */
	@Override public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);


		btnLog = (Button) findViewById(R.id.login);    
		btnLog.setOnClickListener(this);
		btnReg = (Button) findViewById(R.id.register);    
		btnReg.setOnClickListener(this);
		//btnRead = (Button) findViewById(R.id.btnRead);
		//btnRead.setOnClickListener(this);

		etLogin = (EditText) findViewById(R.id.username);  
		etPassword = (EditText) findViewById(R.id.password);  

		// Подключаемся к БД
		SQLiteDatabase db = dbh.getWritableDatabase();  
		//заполняем
		dbh.setFill(db);

		// Описание курсора
		Cursor c;

		// выводим в лог пользователей
		Log.d(LOG_TAG, "--- Table users ---");
		c = db.query("people", null, null, null, null, null, null);
		logCursor(c);
		c.close();
		Log.d(LOG_TAG, "--- ---");

		// выводим в лог данные по людям
		Log.d(LOG_TAG, "--- Table users data ---");
		c = db.query("peopledata", null, null, null, null, null, null);
		logCursor(c);
		c.close();
		Log.d(LOG_TAG, "--- ---");

		// выводим результат объединения
		// используем query
		Log.d(LOG_TAG, "---a INNER JOIN with query---");
		String table = "people as PL inner join peopledata as PD on PD.posid = PL.id";
		String columns[] = { "PL.name as Name", "PL.password as Password", "PD.data as UserData" };
		c = db.query(table, columns, null, null, null, null, null);
		logCursor(c);
		c.close();
		Log.d(LOG_TAG, "--- ---");

		// закрываем БД
		dbh.close();
	}
	//OnClickListener onBtnClick = new OnClickListener() {

	@Override
	public void onClick(View v) {
      

		// получаем данные из полей ввода    
		String name = etLogin.getText().toString();    
		String password = etPassword.getText().toString();

		//подключаемся к БД    
		SQLiteDatabase db = dbh.getWritableDatabase();        

		switch (v.getId()) {    
		case R.id.register:      
			Log.d(LOG_TAG, "--> btnRegister");      
			if (dbh.AddUser(db, name, password)){
			//переход

			Intent intent = new Intent(this, Camera.class);
			startActivity(intent);
			finish();
			}else {
				Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
			}
			break;    

		case R.id.login:      
			Log.d(LOG_TAG, "--> btnLogin"); 
			if (dbh.getCheck(db, name, password)){
				Intent intent = new Intent(this, ImageActivity.class);
				startActivity(intent);
				finish();
				Toast.makeText(getApplicationContext(), "Access granted", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), "Access denied", Toast.LENGTH_SHORT).show();
			}
			break;

		/**case R.id.btnRead:
			Log.d(LOG_TAG, "--> btnRead");
			Cursor c = db.query("people", null, null, null, null, null, null);
			logCursor(c);
			c.close();
			Intent intent = new Intent(this, ImageActivity.class);
			startActivity(intent);
			finish();
		}  */
		}
		// закрываем подключение к БД    
		dbh.close();
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

}






