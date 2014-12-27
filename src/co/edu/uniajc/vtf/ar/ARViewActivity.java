package co.edu.uniajc.vtf.ar;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.wikitude.WikitudeLicence;

import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectUrlListener;

public class ARViewActivity extends Activity {
	protected ArchitectView	architectView;
	protected ArchitectUrlListener urlListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arview);
		this.architectView = (ArchitectView)this.findViewById( R.id.architectView );
		try {
			this.architectView.onCreate( WikitudeLicence.LICENCE_KEY );
			this.urlListener = new ArchitectUrlListener() {

				@Override
				public boolean urlWasInvoked(String uriString) {
					return false;
				}
			};
			this.architectView.registerUrlListener(this.urlListener);
			
		} catch (RuntimeException rex) {
			this.architectView = null;
			Toast.makeText(getApplicationContext(), "can't create Architect View", Toast.LENGTH_SHORT).show();
			Log.e(this.getClass().getName(), "Exception in ArchitectView.onCreate()", rex);
		}	
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {		
		super.onPostCreate(savedInstanceState);
		if ( this.architectView != null ) {
			this.architectView.onPostCreate();
			try {
				this.architectView.load("ARPage/ARView.html");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	@Override
	protected void onResume() {
		super.onResume();
		if ( this.architectView != null ) {
			this.architectView.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if ( this.architectView != null ) {
			this.architectView.onPause();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		if ( this.architectView != null ) {
			this.architectView.onDestroy();
		}
	}

}
