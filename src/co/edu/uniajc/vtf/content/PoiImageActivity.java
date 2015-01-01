/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.content;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.utils.ExtendedApplicationContext;
import co.edu.uniajc.vtf.utils.SystemUiHider;

public class PoiImageActivity extends Activity {

	private static final boolean AUTO_HIDE = true;
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	private static final boolean TOGGLE_ON_CLICK = true;
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private SystemUiHider coSystemUiHider;
	private Handler coHideHandler = new Handler();
	Runnable coHideRunnable = new Runnable() {
		@Override
		public void run() {
			coSystemUiHider.hide();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_image);
		final View contentView = findViewById(R.id.relView);
		
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		coSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		coSystemUiHider.setup();

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					coSystemUiHider.toggle();
				} else {
					coSystemUiHider.show();
				}
			}
		});
		ExtendedApplicationContext loContext = (ExtendedApplicationContext)this.getApplication();
		this.loadData(loContext.getData().toString());
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		delayedHide(100);
	}
	
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};
	
	private void delayedHide(int delayMillis) {
		coHideHandler.removeCallbacks(coHideRunnable);
		coHideHandler.postDelayed(coHideRunnable, delayMillis);
	}
	
	private void loadData(String pData){
        byte[] loData = Base64.decode(pData, Base64.DEFAULT);
        Bitmap loBitmap = BitmapFactory.decodeByteArray(loData, 0, loData.length);	
		ImageView loImage = (ImageView)findViewById(R.id.imgPoiImageView);
		loImage.setImageBitmap(loBitmap);
	}
}
