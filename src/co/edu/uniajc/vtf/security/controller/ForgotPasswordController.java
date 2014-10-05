package co.edu.uniajc.vtf.security.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.Toast;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.security.ConfigLoginActivity;
import co.edu.uniajc.vtf.security.interfaces.IForgotPassword;
import co.edu.uniajc.vtf.security.model.ForgotPasswordModel;
import co.edu.uniajc.vtf.utils.AlertDialogManager;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.ResourcesManager;

public class ForgotPasswordController implements ModelListener{
	private IForgotPassword coView;
	private ForgotPasswordModel coModel;
	
	public ForgotPasswordController(IForgotPassword pView) {	
		this.coView = pView;
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		String lsBaseUrl = loResource.getStringResource(R.string.general_web_service_base_url);
		this.coModel = new ForgotPasswordModel(lsBaseUrl);
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
		if(!lboResult)
			AlertDialogManager.showAlertDialog((Activity)coView, loResource.getStringResource(R.string.general_message_error), lsMessage, AlertDialogManager.ERROR);
		
		return lboResult;
	}
	
	public void sendRecoveryMailAsync(){
		
		if(validate()){
			String loMail = this.coView.getEmail();	
			this.coModel.recoverPasswordAsync(loMail);
		}
	}
	
	public void sendRecoveryMailResult(int pResult){
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);	
		String lsMessage =  "";
		
		if(pResult == 0){
			lsMessage = loResource.getStringResource(R.string.forgot_password_message);					
		}
		else{
			lsMessage = loResource.getStringResource(R.string.general_message_error);
		}
		
		Toast toast = Toast.makeText((Activity)this.coView, lsMessage, Toast.LENGTH_LONG);
		toast.show();	
		this.coView.setEmail("");
	}
	
	public void navigateHome(MenuItem item){
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent loUpIntent = new Intent((Activity)this.coView, ConfigLoginActivity.class);
			NavUtils.navigateUpTo((Activity)this.coView, loUpIntent);
		}
	}

	@Override
	public void onGetData(Object pData, int type) {
		if(type == 0){
			this.sendRecoveryMailResult((Integer)pData);
		}		
		
	}

	@Override
	public void onError(Object pData, int type) {
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		AlertDialogManager.showAlertDialog((Activity)coView, loResource.getStringResource(R.string.general_message_error), loResource.getStringResource(R.string.general_db_error), AlertDialogManager.ERROR);			
	}
}
