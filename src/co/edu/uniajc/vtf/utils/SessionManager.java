/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import co.edu.uniajc.vtf.security.model.UserEntity;

public class SessionManager {
	
	private SharedPreferences coShared;
	private Context coContext;
	private Editor coEditor;
	private static final String PREF_NAME = "toguisSession";
	private static final String KEY_NAMES = "names";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_IS_LOGIN = "isLogin";
	private static final String KEY_SESSION_TYPE = "sessionType";
	
	public static final int FACEBOOK_SESSION = 0;
	public static final int GOOGLE_SESSION = 1;
	public static final int SIMPLE_SESSION = 2;
	
	public SessionManager(Context pContext){
		this.coContext = pContext;
		this.coShared = this.coContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		this.coEditor = this.coShared.edit();
	}
	
	public String getNames(){
		return this.coShared.getString(KEY_NAMES, "");
	}
	
	public String getEmail(){
		return this.coShared.getString(KEY_EMAIL, "");
	}
	
	public String getUserName(){
		return this.coShared.getString(KEY_EMAIL, "");
	}
	
	public boolean isLogin(){
		boolean lboResult = false;
		if(this.coEditor != null){		
			lboResult = this.coShared.getBoolean(KEY_IS_LOGIN, false);
		}
		return lboResult;
	}
	
	public int getSessionType(){
		int liResult = -1;
		if(this.coEditor != null){		
			liResult = this.coShared.getInt(KEY_SESSION_TYPE, -1);
		}
		return liResult;
	}
	
	public void createSession(UserEntity pUser, int sessionType){
		if(this.coEditor != null){
			this.coEditor.putString(KEY_NAMES, pUser.getNames());
			this.coEditor.putString(KEY_EMAIL, pUser.getEmail());
			this.coEditor.putBoolean(KEY_IS_LOGIN, true);
			this.coEditor.putInt(KEY_SESSION_TYPE, sessionType);
			this.coEditor.commit();
		}
	}
	
	public void endSession(){
		if(this.coEditor != null){
			this.coEditor.clear();
			this.coEditor.commit();			
		}
	}
	
}
