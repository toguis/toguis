package co.edu.uniajc.vtf.security;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.security.controller.ConfigLoginController;
import co.edu.uniajc.vtf.security.interfaces.IConfigLogin;
import co.edu.uniajc.vtf.utils.SessionManager;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class ConfigLoginActivity extends Activity implements IConfigLogin {
	private ConfigLoginController coController;
	private GraphUser coUser;
	private UiLifecycleHelper coUiHelper;
    private Session.StatusCallback coCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onFacebookSessionStateChange(session, state, exception);
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config_login);
		this.coController = new ConfigLoginController(this);
		this.configureFacebookSession(savedInstanceState);
	}

	private void configureFacebookSession(Bundle savedInstanceState){
        coUiHelper = new UiLifecycleHelper(this, coCallback);
        coUiHelper.onCreate(savedInstanceState);
		LoginButton loLoginButton =  (LoginButton) findViewById(R.id.btnFacebookLogin);
		loLoginButton.setReadPermissions(Arrays.asList("public_profile","email"));
		loLoginButton.setUserInfoChangedCallback(
			new LoginButton.UserInfoChangedCallback(){
				@Override
				public void onUserInfoFetched(GraphUser user) {
					if(user != null){
						ConfigLoginActivity.this.coUser = user;	
						ConfigLoginActivity.this.coController.createSession(SessionManager.FACEBOOK_SESSION);
						ConfigLoginActivity.this.coController.navigateContent();
						try {
							ConfigLoginActivity.this.finish();;
						} catch (Throwable e) {

						}
					}										
				}				
			}
		);       
	}
	
	protected void onFacebookSessionStateChange(Session session, SessionState state,Exception exception) {
	    if (state.isOpened()) {	
    	
	    } else if (state.isClosed()) {
	    	
	    }	
	}

	public void onClick_GoLogin(View view){
		this.coController.openLoginView();
	}
	
	public void onClick_NavigateCreateAccount(View view){
		this.coController.navigateCreateAccount();;
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    coUiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    coUiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    coUiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    coUiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    coUiHelper.onSaveInstanceState(outState);
	}
	
	@Override
	public String getEmail(){
		String lsResult = "";
		if(Session.getActiveSession().isOpened() && this.coUser != null){		
			lsResult = this.coUser.asMap().get("email").toString();
		}
		return lsResult;
	}
	
	@Override
	public String getNames(){
		String lsResult = "";
		if(Session.getActiveSession().isOpened() && this.coUser != null){
			lsResult = this.coUser.getFirstName() + " "  + this.coUser.getLastName();
		}
		return lsResult;		
	}
}
