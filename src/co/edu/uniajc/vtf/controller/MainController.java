/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.controller;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import co.edu.uniajc.vtf.NoNetworkActivity;
import co.edu.uniajc.vtf.content.SwipeContentActivity;
import co.edu.uniajc.vtf.interfaces.IMain;
import co.edu.uniajc.vtf.security.ConfigLoginActivity;
import co.edu.uniajc.vtf.utils.NetworkUtility;
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
				if(NetworkUtility.isOnline((Activity)loView)){				
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
				else{
					Intent loIntent = new Intent(loActivity, NoNetworkActivity.class);
					loActivity.startActivity(loIntent);	
					loActivity.finish();	
				}

			}
		};
		loTimer = new Timer();
		loTimer.schedule(loTask, SPLASH_SCREEN_DELAY);		
	}
	
	

}
