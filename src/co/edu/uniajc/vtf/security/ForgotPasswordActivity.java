package co.edu.uniajc.vtf.security;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.security.controller.ForgotPasswordController;
import co.edu.uniajc.vtf.security.interfaces.IForgotPassword;

public class ForgotPasswordActivity extends Activity implements IForgotPassword {

	private ForgotPasswordController coController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		this.coController = new ForgotPasswordController(this);	
		final ActionBar coActionBar = getActionBar();
		coActionBar.setDisplayHomeAsUpEnabled(true);	
	}

	@Override
	public String getEmail() {
		TextView loControl = (TextView) this.findViewById(R.id.txtVerifyEmail);	
		return loControl.getText().toString();
	}

	@Override
	public void setEmail(String pMail) {
		TextView loControl = (TextView) this.findViewById(R.id.txtVerifyEmail);	
		loControl.setText(pMail);
	}
	
	public void onClick_SendRecoveryMail(View view){
		this.coController.sendRecoveryMail();	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.coController.navigateHome(item);
		return super.onOptionsItemSelected(item);
	}	
	
}
