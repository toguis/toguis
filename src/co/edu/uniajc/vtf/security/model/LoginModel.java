package co.edu.uniajc.vtf.security.model;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import co.edu.uniajc.vtf.utils.RestAsyncTask;
import co.edu.uniajc.vtf.utils.RestAsyncTaskListener;

public class LoginModel implements RestAsyncTaskListener {

	private String csBaseUrl;
	private String csMethod;
	private ArrayList<LoginModelListener> coModelListener;
	
	public LoginModel(String pBaseUrl){
		this.csBaseUrl = pBaseUrl;
		this.csMethod = "";
		this.coModelListener = new ArrayList<LoginModelListener>();
		
	}
	
	public void getUserAsync(String pId){		
		String lsQueryUrl = String.format(this.csBaseUrl + "ToguisSecurity.svc/get_user?login=%s", pId);
		this.csMethod = "getAsyncUser1";
		RestAsyncTask loTask = new RestAsyncTask();
		loTask.addAsyncTaskListener(this);
		loTask.execute(lsQueryUrl);
	}
	
	private void getUser(String pData){
		try {
			UserEntity loUser = null;
			
			if(!pData.equals("")){
				JSONObject jsonObj = new JSONObject(pData);
				String lsId = jsonObj.getString("USR_ID");
				String lsPassword = jsonObj.getString("USR_PASWORD");
				String lsNames = jsonObj.getString("USR_NAME");
				loUser = new UserEntity();
				loUser.setEmail(lsId);
				loUser.setPassword(lsPassword);
				loUser.setNames(lsNames);				
			}		
			for (LoginModelListener item : this.coModelListener){
				item.onGetUser(loUser);
			}			
		} catch (JSONException ex) {
			for (LoginModelListener item : this.coModelListener){
				item.onError(ex.getMessage());
			}	
		}		
	}
	
	public void addModelListener(LoginModelListener pModelListener){
		this.coModelListener.add(pModelListener);
	}
		
	@Override
	public void onQuerySuccessful(String result) {
		if(this.csMethod.equals("getAsyncUser1")){
			this.getUser(result);
		}
	}

	@Override
	public void onQueryError(String error) {
		for (LoginModelListener item : this.coModelListener){
			item.onError(error);
		}			
	}

}
