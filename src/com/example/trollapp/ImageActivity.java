package com.example.trollapp;

import java.io.File;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ImageActivity extends Activity implements View.OnClickListener{
	public static int idChecked = 0;
	ImageView display;
	Button btnCamera;
	LinearLayout layout;File directory;

	final int TYPE_PHOTO = 1;
	final int REQUEST_CODE_PHOTO = 1;
	final String TAG = "myCameraLogs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picview); 
		
		createDirectory();
		
		layout = (LinearLayout) findViewById(R.id.layoutq);
		btnCamera = (Button) findViewById(R.id.btnCamera);
		display = (ImageView) findViewById(R.id.ivDisplay);
		ImageView image1 = (ImageView) findViewById(R.id.ivImage1);
		ImageView image2 = (ImageView) findViewById(R.id.ivImage2);
		ImageView image3 = (ImageView) findViewById(R.id.ivImage3);
		ImageView image4 = (ImageView) findViewById(R.id.ivImage4);
		ImageView image5 = (ImageView) findViewById(R.id.ivImage5);
		ImageView image6 = (ImageView) findViewById(R.id.ivImage6);
		ImageView image7 = (ImageView) findViewById(R.id.ivImage7);
		ImageView image8 = (ImageView) findViewById(R.id.ivImage8);
		ImageView image9 = (ImageView) findViewById(R.id.ivImage9);

		btnCamera.setOnClickListener(this);
		display.setOnClickListener(this);
		image1.setOnClickListener(this);
		image2.setOnClickListener(this);
		image3.setOnClickListener(this);
		image4.setOnClickListener(this);
		image5.setOnClickListener(this);
		image6.setOnClickListener(this);
		image7.setOnClickListener(this);
		image8.setOnClickListener(this);
		image9.setOnClickListener(this);

	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		menu.add("return");
		menu.add("save");

		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		int test = v.getId();
		switch (v.getId()){

		case R.id.ivImage1:
			display.setImageResource(R.drawable.a1);
			idChecked = R.drawable.a1;
			break;

		case R.id.ivImage2:
			display.setImageResource(R.drawable.a2);
			idChecked = R.drawable.a2;
			break;

		case R.id.ivImage3:
			display.setImageResource(R.drawable.a3);
			idChecked = R.drawable.a3;
			break;

		case R.id.ivImage4:
			display.setImageResource(R.drawable.a4);
			idChecked = R.drawable.a4;
			break;

		case R.id.ivImage5:
			display.setImageResource(R.drawable.a1);
			idChecked = R.drawable.a1;
			break;

		case R.id.ivImage6:
			display.setImageResource(R.drawable.a2);
			idChecked = R.drawable.a2;
			break;

		case R.id.ivImage7:
			display.setImageResource(R.drawable.a3);
			idChecked = R.drawable.a3;
			break;

		case R.id.ivImage8:
			display.setImageResource(R.drawable.a4);
			idChecked = R.drawable.a4;
			break;

		case R.id.ivImage9:
			display.setImageResource(R.drawable.a1);
			idChecked = R.drawable.a1;
			break;

		case R.id.ivDisplay:
			setContentView(new SampleView(this,idChecked,layout.getWidth(),layout.getHeight()) );
			break;

		case R.id.btnCamera:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri(TYPE_PHOTO));
			startActivityForResult(intent, REQUEST_CODE_PHOTO);

			break;

		}

	}

	private Uri generateFileUri(int type) {

		File file = null;

		String LastUri = directory.getPath() + "/" + "photo_"+ System.currentTimeMillis() + ".jpg";
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

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
		String clicked = item.getTitle().toString();
		if (clicked == "return"){
			Intent intent = new Intent(this, ImageActivity.class);
			startActivity(intent);
			finish();
		}
		else{
			setContentView(new SampleView(this,idChecked,layout.getWidth(),layout.getHeight()) );
		}
		return super.onOptionsItemSelected(item);
	}
}

