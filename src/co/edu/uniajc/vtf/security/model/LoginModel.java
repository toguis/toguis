/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.security.model;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import co.edu.uniajc.vtf.utils.BaseModel;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.RestAsyncTask;

public class LoginModel extends BaseModel {
	
	public LoginModel(String pBaseUrl){
		super.csBaseUrl = pBaseUrl;
		super.csMethod = "";
		super.coModelListener = new ArrayList<ModelListener>();
		
	}
	
	public void getUserAsync(String pId){		
		String lsQueryUrl = String.format(this.csBaseUrl + "ToguisSecurity.svc/get_user?login=%s", pId);
		super.csMethod = "getUserAsync";
		RestAsyncTask loTask = new RestAsyncTask();
		loTask.addAsyncTaskListener(this);		
		loTask.execute("0",lsQueryUrl);
	}
	
	private void getUser(String pData){
		try {
			UserEntity loUser = null;
			
			if(!pData.equals("")){
				JSONObject loJsonObj = new JSONObject(pData);
				String lsId = loJsonObj.getString("USR_ID");
				String lsPassword = loJsonObj.getString("USR_PASWORD");
				String lsNames = loJsonObj.getString("USR_NAME");
				int liGender = loJsonObj.getInt("GND_ID");
				loUser = new UserEntity();
				loUser.setEmail(lsId);
				loUser.setPassword(lsPassword);
				loUser.setNames(lsNames);
				loUser.setGender(liGender);
			}		
			for (ModelListener item : this.coModelListener){
				item.onGetData(loUser, 0);
			}			
		} catch (JSONException ex) {
			for (ModelListener item : this.coModelListener){
				item.onError(ex.getMessage(), 0);
			}	
		}		
	}
	
		
	@Override
	public void onQuerySuccessful(String pResult) {
		if(super.csMethod.equals("getUserAsync")){
			this.getUser(pResult);
		}
	}

	@Override
	public void onQueryError(String error) {
		for (ModelListener item : this.coModelListener){
			item.onError(error, 0);
		}			
	}

}
