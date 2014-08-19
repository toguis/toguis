package co.edu.uniajc.vtf.security;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.security.controller.CreateAccountController;
import co.edu.uniajc.vtf.security.interfaces.ICreateAccount;

public class CreateAccountActivity extends Activity implements ICreateAccount {

	private CreateAccountController coController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		this.coController = new CreateAccountController(this);
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

	@Override
	public String getRepeatPassword() {
		TextView loControl = (TextView) this.findViewById(R.id.txtRepeatPassword);	
		return loControl.getText().toString();
	}

	@Override
	public void setRepeatPassword(String pPassword) {
		TextView loControl = (TextView) this.findViewById(R.id.txtRepeatPassword);	
		loControl.setText(pPassword);
	}

	@Override
	public String getNames() {
		TextView loControl = (TextView) this.findViewById(R.id.txtNames);	
		return loControl.getText().toString();
	}

	@Override
	public void setNames(String pNames) {
		TextView loControl = (TextView) this.findViewById(R.id.txtNames);	
		loControl.setText(pNames);
		
	}

	public void onClick_CreateAccount(View pview){		
		this.coController.createAccount();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.coController.navigateHome(item);
		return super.onOptionsItemSelected(item);
	}
}
