package co.edu.uniajc.vtf.security.controller;

import android.app.Activity;
import android.content.Intent;
import co.edu.uniajc.vtf.ContentActivity;
import co.edu.uniajc.vtf.security.CreateAccountActivity;
import co.edu.uniajc.vtf.security.LoginActivity;
import co.edu.uniajc.vtf.security.interfaces.IConfigLogin;
import co.edu.uniajc.vtf.security.model.UserEntity;
import co.edu.uniajc.vtf.utils.SessionManager;

public class ConfigLoginController {
	private IConfigLogin coView;

	public ConfigLoginController(IConfigLogin pView) {
		this.coView = pView;
	}
	
	public void navigateLoginView(){
		Activity loActivity = (Activity)coView;
		Intent loIntent = new Intent(loActivity, LoginActivity.class);
		loActivity.startActivity(loIntent);	
	}
	 
	public void navigateCreateAccount(){
		Activity loActivity = (Activity)coView;
		Intent loIntent = new Intent(loActivity, CreateAccountActivity.class);
		loActivity.startActivity(loIntent);			
	}
	
	public void createSession(int sessionType){
		UserEntity loUser = new UserEntity();
		loUser.setEmail(this.coView.getEmail());
		loUser.setNames(this.coView.getNames());
		SessionManager loSession = new SessionManager((Activity)this.coView);
		loSession.createSession(loUser, sessionType);
	}
	
	public void endSession(){
		SessionManager loSession = new SessionManager((Activity)this.coView);
		loSession.endSession();		
	}
	
	public void navigateContent(){
		Activity loActivity = (Activity)coView;
		Intent loIntent = new Intent(loActivity, ContentActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
		loActivity.startActivity(loIntent);	
		((Activity)coView).finish();
	}
	
	
	
}
