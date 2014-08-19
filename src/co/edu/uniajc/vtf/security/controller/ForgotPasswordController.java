package co.edu.uniajc.vtf.security.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Patterns;
import android.view.MenuItem;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.security.ConfigLoginActivity;
import co.edu.uniajc.vtf.security.interfaces.IForgotPassword;
import co.edu.uniajc.vtf.utils.AlertDialogManager;
import co.edu.uniajc.vtf.utils.ResourcesManager;

public class ForgotPasswordController {
	private IForgotPassword coView;
	
	public ForgotPasswordController(IForgotPassword pView) {	
		this.coView = pView;
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
	
	public void sendRecoveryMail(){
		String loMail = this.coView.getEmail();	
		if(validate()){
			System.out.println("sending email: " + loMail);
		}
	}
	
	public void navigateHome(MenuItem item){
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent loUpIntent = new Intent((Activity)this.coView, ConfigLoginActivity.class);
			NavUtils.navigateUpTo((Activity)this.coView, loUpIntent);
		}
	}
}
