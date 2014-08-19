package co.edu.uniajc.vtf.security;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.security.controller.LoginController;
import co.edu.uniajc.vtf.security.interfaces.ILogin;


public class LoginActivity extends Activity implements ILogin {

	private LoginController coController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		this.coController = new LoginController(this);
		final ActionBar coActionBar = getActionBar();
		coActionBar.setDisplayHomeAsUpEnabled(true);		
	}

	@Override
	public String getEmail() {
		TextView loControl = (TextView) this.findViewById(R.id.txtEmail);	
		return loControl.getText().toString();
	}

	@Override
	public void setEmail(String pMail) {
		TextView loControl = (TextView) this.findViewById(R.id.txtEmail);	
		loControl.setText(pMail);
	}

	@Override
	public String getPassword() {
		TextView loControl = (TextView) this.findViewById(R.id.txtPassword);	
		return loControl.getText().toString();
	}

	@Override
	public void setPassword(String pPassword) {
		TextView loControl = (TextView) this.findViewById(R.id.txtPassword);	
		loControl.setText(pPassword);		
	}

	public void onClick_Login(View view){
		this.coController.CheckCredentials();
	}
	
	public void onClick_NavigateForgotPassword(View view){
		this.coController.navigateForgotPassword();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.coController.navigateHome(item);
		return super.onOptionsItemSelected(item);
	}	
	
	
}
