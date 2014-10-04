package co.edu.uniajc.vtf.security.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.Toast;
import co.edu.uniajc.vtf.ContentActivity;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.security.ConfigLoginActivity;
import co.edu.uniajc.vtf.security.interfaces.ICreateAccount;
import co.edu.uniajc.vtf.security.model.CreateAccountModel;
import co.edu.uniajc.vtf.security.model.UserEntity;
import co.edu.uniajc.vtf.utils.AlertDialogManager;
import co.edu.uniajc.vtf.utils.DigestManager;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.ResourcesManager;
import co.edu.uniajc.vtf.utils.SessionManager;

public class CreateAccountController implements ModelListener {
	private ICreateAccount coView;
	private CreateAccountModel coModel;
	
	public CreateAccountController(ICreateAccount pView) {
		this.coView = pView;
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		String lsBaseUrl = loResource.getStringResource(R.string.general_web_service_base_url);
		this.coModel = new CreateAccountModel(lsBaseUrl);
		this.coModel.addModelListener(this);		
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
	
	public void createAccountAsync(){
		if(this.validate()){
			try {
				UserEntity loUser = new UserEntity();
				loUser.setEmail(this.coView.getEmail());
				loUser.setNames(this.coView.getNames());
				String lsPasswordEncrypt = DigestManager.digestSHA1(this.coView.getPassword());
				loUser.setPassword(lsPasswordEncrypt);
				loUser.setGender(this.coView.getMale() ? 1 : 2);
				this.coModel.createAccountAsync(loUser);
			} catch (Exception e) {
				ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
				AlertDialogManager.showAlertDialog((Activity)coView, loResource.getStringResource(R.string.general_message_error), loResource.getStringResource(R.string.general_db_error), AlertDialogManager.ERROR);			
			} 			
		}
	}
	
	public void createAccountResult(int pResult){
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);	
		String lsMessage =  "";
		
		if(pResult == 0){
			lsMessage = loResource.getStringResource(R.string.create_account_successfully_message);	
			SessionManager loSession = new SessionManager((Activity)this.coView);			
			
			try{
				UserEntity loUser = new UserEntity();
				String lsPasswordEncrypt = DigestManager.digestSHA1(this.coView.getPassword());		
				loUser.setEmail(this.coView.getEmail());
				loUser.setPassword(lsPasswordEncrypt);
				loUser.setNames(this.coView.getNames());
				loUser.setGender(this.coView.getMale() ? 1 : 2);
				loSession.createSession(loUser , SessionManager.SIMPLE_SESSION);
			}
			catch(Exception e){
				lsMessage = loResource.getStringResource(R.string.general_message_error);
			}
				
		}
		else if(pResult == 1){
			lsMessage = loResource.getStringResource(R.string.create_account_error_message);		
		}
		else{
			lsMessage = loResource.getStringResource(R.string.general_message_error);
		}
		
		Toast toast = Toast.makeText((Activity)this.coView, lsMessage, Toast.LENGTH_LONG);
		toast.show();	
		
		if(pResult == 0){
			this.navigateContent();
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

	@Override
	public void onGetData(Object pData, int type) {
		if(type == 0){
			this.createAccountResult((Integer)pData);
		}		
	}

	@Override
	public void onError(Object pData, int type) {
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		AlertDialogManager.showAlertDialog((Activity)coView, loResource.getStringResource(R.string.general_message_error), loResource.getStringResource(R.string.general_db_error), AlertDialogManager.ERROR);			
	}
}
