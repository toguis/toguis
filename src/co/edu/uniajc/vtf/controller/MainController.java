package co.edu.uniajc.vtf.controller;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import co.edu.uniajc.vtf.content.SwipeContentActivity;
import co.edu.uniajc.vtf.interfaces.IMain;
import co.edu.uniajc.vtf.security.ConfigLoginActivity;
import co.edu.uniajc.vtf.utils.SessionManager;

public class MainController {
	private static final long SPLASH_SCREEN_DELAY = 3000;	
	private Timer loTimer;
	private IMain loView;
	
	public MainController(IMain pView){
		this.loView = pView;
	}
	
	public void StartTimer(){
		TimerTask loTask = new TimerTask(){		
			@Override
			public void run() {		
				Activity loActivity = (Activity)loView;
				SessionManager loSession = new SessionManager(loActivity);
				Intent loIntent = null;
				if(loSession.isLogin()){
					loIntent = new Intent(loActivity, SwipeContentActivity.class);
				}
				else {
					loIntent = new Intent(loActivity, ConfigLoginActivity.class);
				}
				
				loActivity.startActivity(loIntent);	
				loActivity.finish();
			}
		};
		loTimer = new Timer();
		loTimer.schedule(loTask, SPLASH_SCREEN_DELAY);		
	}
	
	

}
