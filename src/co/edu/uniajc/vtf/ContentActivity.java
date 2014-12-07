package co.edu.uniajc.vtf;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import co.edu.uniajc.vtf.security.ConfigLoginActivity;
import co.edu.uniajc.vtf.utils.SessionManager;

import com.facebook.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

public class ContentActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	private static final int RC_SIGN_IN = 0;
    private GoogleApiClient coApiClient;
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;
    private boolean mSignInClicked;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		TextView loControl = (TextView) this.findViewById(R.id.lblGender);
		SessionManager se = new SessionManager(this);
		loControl.setText("Hola:"  + se.getNames());
		this.coApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)		
		.addApi(Plus.API)
		.addScope(Plus.SCOPE_PLUS_PROFILE)
		.build();		
	}
	
	public void borrarSesion(View view){
		SessionManager se = new SessionManager(this);
		switch(se.getSessionType()){
			case SessionManager.FACEBOOK_SESSION:
				if(Session.getActiveSession() != null)
					Session.getActiveSession().closeAndClearTokenInformation();
				break;
			case SessionManager.GOOGLE_SESSION:	
				if(coApiClient.isConnected()){
					Plus.AccountApi.clearDefaultAccount(coApiClient);
					//Plus.AccountApi.revokeAccessAndDisconnect(coApiClient);
					coApiClient.disconnect();					
				}
				
		}
		
		se.endSession();
				
    	Intent intt = new Intent(this, ConfigLoginActivity.class);
    	this.startActivity(intt);
    	this.finish();		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		this.coApiClient.connect();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	    if (this.coApiClient.isConnected()) {
	    	this.coApiClient.disconnect();
	     }
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    
		if (requestCode == RC_SIGN_IN) {
		    if (resultCode != RESULT_OK) {
		        mSignInClicked = false;
		    }			
			mIntentInProgress = false;
			if (!this.coApiClient.isConnecting()) {
				this.coApiClient.connect();
			}
		}	    
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress) {
			// Store the ConnectionResult so that we can use it later when the user clicks
			// 'sign-in'.
			mConnectionResult = result;
		
			if (mSignInClicked) {
			// The user has already clicked 'sign-in' so we attempt to resolve all
			// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();	
	}

	@Override
	public void onConnectionSuspended(int cause) {
		this.coApiClient.connect();
		
	}

	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
			} 
			catch (SendIntentException e) {
			    // The intent was canceled before it was sent.  Return to the default
				// state and attempt to connect to get an updated ConnectionResult.
				mIntentInProgress = false;
				this.coApiClient.connect();
			}
		}
	}	

}
