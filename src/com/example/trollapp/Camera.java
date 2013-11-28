package com.example.trollapp;

import java.io.File;

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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Camera extends Activity{

	//--------------------

	EditText etText;
	ImageView ivPhoto;

	File directory;
	final int TYPE_PHOTO = 1;

	final int REQUEST_CODE_PHOTO = 1;

	final String TAG = "myCameraLogs";
	
	DBHelper dbh = new DBHelper(this);
	//-----------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//Main appState = ((Main)getApplicationContext());
		//aint id = appState.getId();
		Log.d(TAG, "Get ID = " + MainActivity.ID);
		createDirectory();
		ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
		etText = (EditText)findViewById(R.id.etText);
	}
	public void onClickPhoto(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri(TYPE_PHOTO));
		startActivityForResult(intent, REQUEST_CODE_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		//подключаемся к БД    
		SQLiteDatabase db = dbh.getWritableDatabase(); 
		
					String url = etText.getText().toString();
					
					dbh.SaveUserData(db, MainActivity.ID, url);
					dbh.getReadUserData(db, MainActivity.ID);
					Bitmap bitmap = BitmapFactory.decodeFile(url);
					ivPhoto.setImageBitmap(bitmap);
					Toast.makeText(getApplicationContext(), "FindPhoto", Toast.LENGTH_LONG);

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

		String picUri = directory.getPath() + "/" + "photo_"+ System.currentTimeMillis() + ".jpg";
		etText.setText(picUri);
		file = new File(picUri);
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
