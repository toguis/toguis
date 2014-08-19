package co.edu.uniajc.vtf.security.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Patterns;
import android.view.MenuItem;
import co.edu.uniajc.vtf.ContentActivity;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.security.ConfigLoginActivity;
import co.edu.uniajc.vtf.security.interfaces.ICreateAccount;
import co.edu.uniajc.vtf.security.model.CreateAccountModel;
import co.edu.uniajc.vtf.security.model.UserEntity;
import co.edu.uniajc.vtf.utils.AlertDialogManager;
import co.edu.uniajc.vtf.utils.DigestManager;
import co.edu.uniajc.vtf.utils.ResourcesManager;
import co.edu.uniajc.vtf.utils.SessionManager;

public class CreateAccountController {
	private ICreateAccount coView;
	private CreateAccountModel coModel;
	
	public CreateAccountController(ICreateAccount pView) {
		this.coView = pView;
		this.coModel = new CreateAccountModel();
	}
	
	private boolean validate(){
		String lsMessage = "";
		boolean lboResult = true;
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);	
		
		if(this.coView.getEmail().equals("")){
			lsMessage = loResource.getStringResource(R.string.general_email_invalid_message);	
			lboResult = false;
		}
		else if (!Patterns.EMAIL_ADDRESS.matcher(this.coView.getEmail()).matches()){
			lsMessage = loResource.getStringResource(R.string.general_email_invalid_message2);
			lboResult = false;
		}		
		else if(this.coView.getPassword().equals("")){
			lsMessage = loResource.getStringResource(R.string.general_password_invalid_message);	
			lboResult = false;
		}
		else if (this.coView.getPassword().length() < 6){
			lsMessage = loResource.getStringResource(R.string.general_password_invalid_message2);
			lboResult = false;
		}
		else if(!this.coView.getPassword().equals(this.coView.getRepeatPassword())){
			lsMessage = loResource.getStringResource(R.string.general_password_invalid_message3);
			lboResult = false;
		}	
		else if(this.coView.getNames().equals("")){
			lsMessage = loResource.getStringResource(R.string.general_names_invalid_message);
			lboResult = false;
		}
		
		if(!lboResult)
			AlertDialogManager.showAlertDialog((Activity)coView, loResource.getStringResource(R.string.general_message_error), lsMessage, AlertDialogManager.ERROR);
		return lboResult;
	}
	
	public void createAccount(){
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);	
		if(this.validate()){
			if(!this.coModel.userExists(this.coView.getEmail())){	
					
				UserEntity loUser = new UserEntity();
				String lsPasswordEncrypt;
				try {
					lsPasswordEncrypt = DigestManager.digestSHA1(this.coView.getPassword());
					loUser.setEmail(this.coView.getEmail());
					loUser.setPassword(lsPasswordEncrypt);
					loUser.setNames(this.coView.getNames());
					this.coModel.createAccount(loUser);
					SessionManager loSession = new SessionManager((Activity)this.coView);
					loSession.createSession(loUser , SessionManager.SIMPLE_SESSION);
					this.navigateContent();
				} 
				catch (Exception e) {
					AlertDialogManager.showAlertDialog((Activity)coView, 
							loResource.getStringResource(R.string.general_message_error), 
							loResource.getStringResource(R.string.general_save_db_error), 
							AlertDialogManager.ERROR);
				} 	
			}
			else{
				String lsMessage = loResource.getStringResource(R.string.general_email_invalid_message3);		
				AlertDialogManager.showAlertDialog((Activity)coView, loResource.getStringResource(R.string.general_message_error), lsMessage, AlertDialogManager.ERROR);
			}
		}	
	}
	
	public void navigateHome(MenuItem item){
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent upIntent = new Intent((Activity)this.coView, ConfigLoginActivity.class);
			NavUtils.navigateUpTo((Activity)this.coView, upIntent);
		}
	}
	
	public void navigateContent(){
		Activity loActivity = (Activity)coView;
		Intent loIntent = new Intent(loActivity, ContentActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
		loActivity.startActivity(loIntent);	
		((Activity)coView).finish();
	}
}
