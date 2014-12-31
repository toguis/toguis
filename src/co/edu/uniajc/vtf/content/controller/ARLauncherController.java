package co.edu.uniajc.vtf.content.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import co.edu.uniajc.vtf.ar.ARViewActivity;
import co.edu.uniajc.vtf.content.interfaces.IARLauncher;

public class ARLauncherController {
	private IARLauncher coView;
	
	
	public ARLauncherController(IARLauncher pView) {
		super();
		this.coView = pView;
	}

	public void navigateToARView(){
		Activity loActivity = ((Fragment)this.coView).getActivity();
		Intent loIntent = new Intent(loActivity, ARViewActivity.class);	
		loActivity.startActivity(loIntent);				
	}
}
