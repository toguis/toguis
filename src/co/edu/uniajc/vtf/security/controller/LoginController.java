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
import co.edu.uniajc.vtf.security.model.UserEntity;
import co.edu.uniajc.vtf.utils.AlertDialogManager;
import co.edu.uniajc.vtf.utils.DigestManager;
import co.edu.uniajc.vtf.utils.ResourcesManager;
import co.edu.uniajc.vtf.utils.SessionManager;


public class LoginController {
	private ILogin coView;
	private LoginModel coModel;
	
	public LoginController(ILogin pView) {	
		this.coView = pView;
		this.coModel = new LoginModel();
	}
	
	public void CheckCredentials(){
		String loEmail = this.coView.getEmail();
		String loPassword = this.coView.getPassword();
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		
		if(!loEmail.equals("")){
			if(!loPassword.equals("")){
				try {
					String lsPasswordEncrypt = DigestManager.digestSHA1(loPassword);
					UserEntity loUser = new UserEntity();
					loUser.setEmail(loEmail);
					loUser.setPassword(lsPasswordEncrypt);
					if(this.coModel.CheckCredentials(loUser)){		
						loUser = this.coModel.getUser(loEmail);
						AlertDialogManager.showAlertDialog((Activity)this.coView, 
															 loResource.getStringResource(R.string.general_login_successfully_title), 
															 loResource.getStringResource(R.string.general_login_successfully_message), 
															 AlertDialogManager.SUCCESS);
						SessionManager loSession = new SessionManager((Activity)this.coView);
						loSession.createSession(loUser,SessionManager.SIMPLE_SESSION);	
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
		else{
			AlertDialogManager.showAlertDialog((Activity)this.coView, 
					 loResource.getStringResource(R.string.general_login_error_title), 
					 loResource.getStringResource(R.string.general_login_error_message), 
					 AlertDialogManager.ERROR);				
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
	
}
