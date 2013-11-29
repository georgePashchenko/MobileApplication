package com.example.trollapp;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class Camera extends Activity{
	public static String LastUri = null;
	
	final int TYPE_PHOTO = 1;
	final int REQUEST_CODE_PHOTO = 1;
	final String TAG = "myCameraLogs";

	String[] names = {"Dadd"};
	
	EditText etText;
	File directory;
	//LinearLayout imageGallery ;
	ImageView ivPhoto;
	ListView lvMain;
	
	DBHelper dbh = new DBHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);


		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    // находим список
	    lvMain = (ListView) findViewById(R.id.lstView);
	    
		Log.d(TAG, "Get ID = " + MainActivity.ID);
		createDirectory();
		//ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
		ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
		//mageGallery = (LinearLayout)findViewById(R.id.linearImage);
		SQLiteDatabase db = dbh.getWritableDatabase(); 
		
		List<String> names1 =  dbh.getUserData(MainActivity.ID, db);
		for (int i = 0; i< names1.size(); i++){
			names [i] = names1.get(i);
		}
		
	    // создаем адаптер
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, names);

	    // присваиваем адаптер списку
	    lvMain.setAdapter(adapter);
	}
	public void onClickPhoto(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri(TYPE_PHOTO));
		startActivityForResult(intent, REQUEST_CODE_PHOTO);
		
		SQLiteDatabase db = dbh.getWritableDatabase(); 
		List<String> names1 =  dbh.getUserData(MainActivity.ID, db);
		for (int i = 0; i< names1.size(); i++){
			names [i] = names1.get(i);
		}
	    // создаем адаптер
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, names);

	    // присваиваем адаптер списку
	    lvMain.setAdapter(adapter);
	}

	private void LoadImage(String Uri){
		ImageView image = new ImageView(this);		
		LayoutParams params = (LayoutParams)image.getLayoutParams();
		params.width = 150;
		params.height = 150;;
		image.setLayoutParams(params);
		Bitmap bitmap = BitmapFactory.decodeFile(Uri);
		image.setImageBitmap(bitmap);
		//imageGallery.addView(image);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		//подключаемся к БД    
		SQLiteDatabase db = dbh.getWritableDatabase(); 
		if (requestCode == REQUEST_CODE_PHOTO) {
			if (resultCode == RESULT_OK) {
				if (intent == null) {
					Log.d(TAG, "Intent is null");
				} else {
					Log.d(TAG, "Photo uri: " + intent.getData());
					dbh.SaveUserData(db, MainActivity.ID, LastUri);
					Log.d(TAG, "Photo uri: " + intent.getData());
					Bundle bndl = intent.getExtras();
					if (bndl != null) {
						Object obj = intent.getExtras().get("data");
						if (obj instanceof Bitmap) {
							Bitmap bitmap = (Bitmap) obj;
							Log.d(TAG, "bitmap " + bitmap.getWidth() + " x "
									+ bitmap.getHeight());
							ivPhoto.setImageBitmap(bitmap);
						}
					}
				}
			} else if (resultCode == RESULT_CANCELED) {
				Log.d(TAG, "Canceled");
			}
		}

	}


	/**


		if (requestCode == REQUEST_CODE_PHOTO) {
			if (resultCode == RESULT_OK) {
				if (intent == null) {
					Log.d(TAG, "Intent is null");
				} else {
					Log.d(TAG, "Photo uri: " + intent.getData());
					Bundle bndl = intent.getExtras();
					if (bndl != null) {
						Object obj = intent.getExtras().get("data");
						if (obj instanceof Bitmap) {
							Bitmap bitmap = (Bitmap) obj;
							Log.d(TAG, "bitmap " + bitmap.getWidth() + " x "
									+ bitmap.getHeight());
							ivPhoto.setImageBitmap(bitmap);
						}
					}
				}
			} else if (resultCode == RESULT_CANCELED) {
				Log.d(TAG, "Canceled");
			}
		}
	}*/
	private Uri generateFileUri(int type) {

		File file = null;

		LastUri = directory.getPath() + "/" + "photo_"+ System.currentTimeMillis() + ".jpg";
		file = new File(LastUri);
		//Toast.makeText(getApplicationContext(), picUri, Toast.LENGTH_SHORT).show();


		Log.d(TAG, "fileName = " + file);
		return Uri.fromFile(file);
	}
	private void createDirectory() {
		directory = new File(
				Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyFolder");
		if (!directory.exists())
			directory.mkdirs();
	}
}
