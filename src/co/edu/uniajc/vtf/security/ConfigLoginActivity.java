/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.security;

import java.util.Arrays;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.receivers.NetworkStatusReceiver;
import co.edu.uniajc.vtf.security.controller.ConfigLoginController;
import co.edu.uniajc.vtf.security.interfaces.IConfigLogin;
import co.edu.uniajc.vtf.utils.AlertDialogManager;
import co.edu.uniajc.vtf.utils.ResourcesManager;
import co.edu.uniajc.vtf.utils.SessionManager;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class ConfigLoginActivity extends Activity implements IConfigLogin,
															 LoginButton.UserInfoChangedCallback,
															 GoogleApiClient.ConnectionCallbacks, 
															 GoogleApiClient.OnConnectionFailedListener, 
															 View.OnClickListener,
															 Session.StatusCallback{
	private ConfigLoginController coController;
	private int ciSignInType = -1;
	
	//Facebook Utilities
	private GraphUser coFacebookUser;
	private UiLifecycleHelper coFacebookSignInHelper;
	private Session.StatusCallback coCallback = this;
	 
	//Google+ Utilities
	private static final int RC_SIGN_IN = 0;
	private Person coGoogleUser;
    private GoogleApiClient coGoogleSignInHelper;
    private boolean cboIntentInProgress;
    private ConnectionResult coConnectionResult;
    private boolean cboSignInClicked;
    private boolean cboCancelFBAuth;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config_login);
		this.coController = new ConfigLoginController(this);	
		
		if(this.getIntent().hasExtra("no_fb_auth")){
			this.cboCancelFBAuth = this.getIntent().getIntExtra("no_fb_auth", 0) == 0 ? false : true;
		}
		
		
		this.configureGoogleSession();
		this.configureFacebookSession(savedInstanceState);
	}
	
	//Facebook configuration
	private void configureFacebookSession(Bundle savedInstanceState){
        coFacebookSignInHelper = new UiLifecycleHelper(this, coCallback);
        coFacebookSignInHelper.onCreate(savedInstanceState);
		LoginButton loLoginButton =  (LoginButton) findViewById(R.id.btnFacebookLogin);
		loLoginButton.setReadPermissions(Arrays.asList("public_profile","email"));
		loLoginButton.setUserInfoChangedCallback(this);       
	}
	
	protected void onFacebookSessionStateChange(Session session, SessionState state,Exception exception) {
	    if (state.isOpened()) {	
    	
	    } else if (state.isClosed()) {
	    	
	    }	
	}
	
	@Override
	public void onUserInfoFetched(GraphUser user) {		
		if(this.cboCancelFBAuth){
			try{
				this.cboCancelFBAuth = false;
				if(Session.getActiveSession() != null)
					Session.getActiveSession().closeAndClearTokenInformation();
			}
			catch(Exception ex){
				
			}
		}
		else if(user != null){
			ConfigLoginActivity.this.coFacebookUser = user;	
			this.ciSignInType = SessionManager.FACEBOOK_SESSION;
			ConfigLoginActivity.this.coController.createSession(SessionManager.FACEBOOK_SESSION);
			ConfigLoginActivity.this.coController.navigateContent();
			try {
				ConfigLoginActivity.this.finish();
			} catch (Throwable e) {

			}
		}	
	}
	
	@Override
	public void call(Session session, SessionState state, Exception exception) {
		onFacebookSessionStateChange(session, state, exception);
	}	
	
	//Google+ configuration
	private void configureGoogleSession(){
		this.coGoogleSignInHelper = new GoogleApiClient.Builder(this)
										.addConnectionCallbacks(this)
										.addOnConnectionFailedListener(this)		
										.addApi(Plus.API)
										.addScope(Plus.SCOPE_PLUS_PROFILE)
										.build();
		this.findViewById(R.id.btnGoogleLogin).setOnClickListener(this);
		this.setGooglePlusButtonText((SignInButton)findViewById(R.id.btnGoogleLogin), com.google.android.gms.R.string.common_signin_button_text_long);		
	}
	
	public void onClick_GoogleLogin(View view){
		if (view.getId() == R.id.btnGoogleLogin  && !this.coGoogleSignInHelper.isConnecting()) {
			cboSignInClicked = true;
			resolveSignInError();
		}	
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!cboIntentInProgress) {
			coConnectionResult = result;		
			if (cboSignInClicked) {
				resolveSignInError();
			}
		}		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		if (Plus.PeopleApi.getCurrentPerson(this.coGoogleSignInHelper) != null) {
			this.ciSignInType = SessionManager.GOOGLE_SESSION;
			this.coGoogleUser = Plus.PeopleApi.getCurrentPerson(this.coGoogleSignInHelper);		
			this.coController.createSession(SessionManager.GOOGLE_SESSION);
			this.coController.navigateContent();						
		}
	
	}

	@Override
	public void onConnectionSuspended(int cause) {
		this.coGoogleSignInHelper.connect();	
	}

	private void resolveSignInError() {
		if (coConnectionResult.hasResolution()) {
			try {
				cboIntentInProgress = true;
				startIntentSenderForResult(coConnectionResult.getResolution().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
			} 
			catch (SendIntentException e) {
				cboIntentInProgress = false;
				this.coGoogleSignInHelper.connect();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(this.googlePlusIsInstalled()){
			this.onClick_GoogleLogin(v);
		}
		else{
			ResourcesManager loResource = new ResourcesManager(this);	
			AlertDialogManager.showAlertDialog(this, loResource.getStringResource(R.string.general_message_warning), loResource.getStringResource(R.string.general_google_play_not_installed), AlertDialogManager.WARNING);
		}			
	}

	public boolean googlePlusIsInstalled(){
		int liErrorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		return liErrorCode == ConnectionResult.SUCCESS ? true : false;
	}
	
	protected void setGooglePlusButtonText(SignInButton pSignInButton, int pButtonText) {
	    // Find the TextView that is inside of the SignInButton and set its text
		ResourcesManager loResource = new ResourcesManager(this);	
		
	    for (int i = 0; i < pSignInButton.getChildCount(); i++) {
	        View loView = pSignInButton.getChildAt(i);

	        if (loView instanceof TextView) {
	            TextView loTextView = (TextView) loView;
	            loTextView.setText(loResource.getStringResource(pButtonText));
	            return;
	        }
	    }
	}
	
	public void onClick_GoLogin(View view){
		this.coController.navigateLoginView();
	}
	
	public void onClick_NavigateCreateAccount(View view){
		this.coController.navigateCreateAccount();;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		this.coGoogleSignInHelper.connect();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	    if (this.coGoogleSignInHelper.isConnected()) {
	    	this.coGoogleSignInHelper.disconnect();
	     }
	}	
	
	@Override
	public void onResume() {
	    super.onResume();
		ComponentName loComponent = new ComponentName(this, NetworkStatusReceiver.class);
		this.getPackageManager().setComponentEnabledSetting(loComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED  , PackageManager.DONT_KILL_APP);	
	    this.coFacebookSignInHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    this.coFacebookSignInHelper.onActivityResult(requestCode, resultCode, data);
	    
		if (requestCode == RC_SIGN_IN) {
		    if (resultCode != RESULT_OK) {
		        cboSignInClicked = false;
		    }			
			cboIntentInProgress = false;
			if (!this.coGoogleSignInHelper.isConnecting()) {
				this.coGoogleSignInHelper.connect();
			}
		}	    
	}

	@Override
	public void onPause() {
	    super.onPause();
		ComponentName loComponent = new ComponentName(this, NetworkStatusReceiver.class);
		this.getPackageManager().setComponentEnabledSetting(loComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED , PackageManager.DONT_KILL_APP);
	    this.coFacebookSignInHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    this.coFacebookSignInHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    this.coFacebookSignInHelper.onSaveInstanceState(outState);
	}
	
	@Override
	public String getEmail(){
		String lsResult = "";			
		if(this.ciSignInType == SessionManager.FACEBOOK_SESSION){
			if(Session.getActiveSession().isOpened() && this.coFacebookUser != null){		
				lsResult = this.coFacebookUser.asMap().get("email").toString();
			}					
		}
		else if(this.ciSignInType == SessionManager.GOOGLE_SESSION){
			if(this.coGoogleSignInHelper.isConnected() && this.coGoogleUser != null){
				lsResult = Plus.AccountApi.getAccountName(this.coGoogleSignInHelper);
			}
		}
					
		return lsResult;
	}
	
	@Override
	public String getNames(){
		String lsResult = "";				
		if(this.ciSignInType == SessionManager.FACEBOOK_SESSION){
			if(Session.getActiveSession().isOpened() && this.coFacebookUser != null){		
				lsResult = this.coFacebookUser.getFirstName() + " "  + this.coFacebookUser.getLastName();
			}					
		}
		else if(this.ciSignInType == SessionManager.GOOGLE_SESSION){
			if(this.coGoogleSignInHelper.isConnected() && this.coGoogleUser != null){
				lsResult = this.coGoogleUser.getDisplayName();
			}
		}				
		return lsResult;			
	}
	
	public int getGender(){
		int liResult = 0;			
		if(this.ciSignInType == SessionManager.FACEBOOK_SESSION){
			if(Session.getActiveSession().isOpened() && this.coFacebookUser != null){	
				String lsTempResult = this.coFacebookUser.asMap().get("gender").toString();
				if(lsTempResult.equals("male")){
					liResult = 1;
				}
				else{
					liResult = 2;
				}
				
			}					
		}
		else if(this.ciSignInType == SessionManager.GOOGLE_SESSION){
			if(this.coGoogleSignInHelper.isConnected() && this.coGoogleUser != null){
				int liTempResult = this.coGoogleUser.getGender();
				if(liTempResult == 0){
					liResult = 1;
				}
				else{
					liResult = 2;
				}				
			}
		}	
					
		return liResult;
	}

}
