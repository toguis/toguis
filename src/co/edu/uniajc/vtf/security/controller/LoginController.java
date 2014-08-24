package co.edu.uniajc.vtf.security.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import co.edu.uniajc.vtf.ContentActivity;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.security.ConfigLoginActivity;
import co.edu.uniajc.vtf.security.ForgotPasswordActivity;
import co.edu.uniajc.vtf.security.interfaces.ILogin;
import co.edu.uniajc.vtf.security.model.LoginModel;
import co.edu.uniajc.vtf.security.model.LoginModelListener;
import co.edu.uniajc.vtf.security.model.UserEntity;
import co.edu.uniajc.vtf.utils.AlertDialogManager;
import co.edu.uniajc.vtf.utils.DigestManager;
import co.edu.uniajc.vtf.utils.ResourcesManager;
import co.edu.uniajc.vtf.utils.SessionManager;


public class LoginController implements LoginModelListener {
	private ILogin coView;
	private LoginModel coModel;
	
	public LoginController(ILogin pView) {	
		this.coView = pView;
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		String lsBaseUrl = loResource.getStringResource(R.string.general_web_service_base_url);
		this.coModel = new LoginModel(lsBaseUrl);
		this.coModel.addModelListener(this);
	}
	
	private void CheckCredentials(UserEntity pUser){
		String loEmail = this.coView.getEmail();
		String loPassword = this.coView.getPassword();
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		if(pUser != null){
			try {
				String lsPasswordEncrypt = DigestManager.digestSHA1(loPassword);
				UserEntity loUser = new UserEntity();
				loUser.setEmail(loEmail);
				loUser.setPassword(lsPasswordEncrypt);
				if(pUser.getPassword().equals(loUser.getPassword())){
					SessionManager loSession = new SessionManager((Activity)this.coView);
					loSession.createSession(pUser,SessionManager.SIMPLE_SESSION);	
					this.navigateContent();
				}
				else{
					AlertDialogManager.showAlertDialog((Activity)this.coView, 
														 loResource.getStringResource(R.string.general_login_error_title), 
														 loResource.getStringResource(R.string.general_login_error_message), 
														 AlertDialogManager.ERROR);						
				}
			} catch (Exception e) {
				
				AlertDialogManager.showAlertDialog((Activity)this.coView, 
						 loResource.getStringResource(R.string.general_login_error_title), 
						 loResource.getStringResource(R.string.general_login_error_message2), 
						 AlertDialogManager.ERROR);		
			} 				
		}
		else{
			AlertDialogManager.showAlertDialog((Activity)this.coView, 
					 loResource.getStringResource(R.string.general_login_error_title), 
					 loResource.getStringResource(R.string.general_login_error_message), 
					 AlertDialogManager.ERROR);			
		}
		
		
	}
	
	public void checkCredentialsAsync(){
		if(this.validate()){
			String loEmail = this.coView.getEmail();
			this.coModel.getUserAsync(loEmail);			
		}	
	}
	
	public void navigateForgotPassword(){
		Activity loActivity = (Activity)this.coView;
		Intent loIntent = new Intent((Activity)this.coView, ForgotPasswordActivity.class);
		loActivity.startActivity(loIntent);	
	}
	
	public void navigateHome(MenuItem item){
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent loUpIntent = new Intent((Activity)this.coView, ConfigLoginActivity.class);
			NavUtils.navigateUpTo((Activity)this.coView, loUpIntent);
		}
	}
	
	public void navigateContent(){
		Activity loActivity = (Activity)coView;
		Intent loIntent = new Intent(loActivity, ContentActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
		loActivity.startActivity(loIntent);			
		((Activity)coView).finish();
	}
	
	private boolean validate(){
		String lsMessage = "";
		boolean lboResult = true;
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		
		if(this.coView.getEmail().equals("")){
			lsMessage = loResource.getStringResource(R.string.general_login_error_message);	
			lboResult = false;
		}
		else if (this.coView.getPassword().equals("")){
			lsMessage = loResource.getStringResource(R.string.general_login_error_message);
			lboResult = false;
		}	
		
		if(!lboResult)
			AlertDialogManager.showAlertDialog((Activity)coView, loResource.getStringResource(R.string.general_message_error), lsMessage, AlertDialogManager.ERROR);
		return lboResult;
	}
	
	@Override
	public void onGetUser(UserEntity pUser) {
		this.CheckCredentials(pUser);
	}

	@Override
	public void onError(String pData) {
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		AlertDialogManager.showAlertDialog((Activity)coView, loResource.getStringResource(R.string.general_message_error), loResource.getStringResource(R.string.general_db_error), AlertDialogManager.ERROR);			
	}	
	
}
