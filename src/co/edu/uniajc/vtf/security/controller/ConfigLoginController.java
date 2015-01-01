/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.security.controller;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.SwipeContentActivity;
import co.edu.uniajc.vtf.security.CreateAccountActivity;
import co.edu.uniajc.vtf.security.LoginActivity;
import co.edu.uniajc.vtf.security.interfaces.IConfigLogin;
import co.edu.uniajc.vtf.security.model.ConfigLoginModel;
import co.edu.uniajc.vtf.security.model.UserEntity;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.ResourcesManager;
import co.edu.uniajc.vtf.utils.SessionManager;

public class ConfigLoginController implements ModelListener {
	private IConfigLogin coView;
	private ConfigLoginModel coModel;
	
	public ConfigLoginController(IConfigLogin pView) {
		this.coView = pView;
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		String lsBaseUrl = loResource.getStringResource(R.string.general_web_service_base_url);
		this.coModel = new ConfigLoginModel(lsBaseUrl);
		this.coModel.addModelListener(this);			
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
		loUser.setGender(this.coView.getGender());
		SessionManager loSession = new SessionManager((Activity)this.coView);
		loSession.createSession(loUser, sessionType);	

		this.coModel.createAccountAsync(loUser,sessionType + 1);
		
	}
	
	public void showMessage(){
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		String lsMessage = loResource.getStringResource(R.string.config_login_error_message);
	
		Toast toast = Toast.makeText((Activity)this.coView, lsMessage, Toast.LENGTH_LONG);
		toast.show();	
	}
	
	public void endSession(){
		SessionManager loSession = new SessionManager((Activity)this.coView);
		loSession.endSession();		
	}
	
	public void navigateContent(){
		Activity loActivity = (Activity)coView;
		Intent loIntent = new Intent(loActivity, SwipeContentActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
		loActivity.startActivity(loIntent);	
		((Activity)coView).finish();
	}

	@Override
	public void onGetData(Object pData, int type) {
		if((Integer)pData != 0){
			this.showMessage();
		}		
	}

	@Override
	public void onError(Object pData, int type) {
		Toast toast = Toast.makeText((Activity)this.coView, pData.toString(), Toast.LENGTH_LONG);
		toast.show();	
	}	
	
}
