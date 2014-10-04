package co.edu.uniajc.vtf.security.model;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.RestAsyncTask;
import co.edu.uniajc.vtf.utils.RestAsyncTaskListener;

public class LoginModel implements RestAsyncTaskListener {

	private String csBaseUrl;
	private String csMethod;
	private ArrayList<ModelListener> coModelListener;
	
	public LoginModel(String pBaseUrl){
		this.csBaseUrl = pBaseUrl;
		this.csMethod = "";
		this.coModelListener = new ArrayList<ModelListener>();
		
	}
	
	public void getUserAsync(String pId){		
		String lsQueryUrl = String.format(this.csBaseUrl + "ToguisSecurity.svc/get_user?login=%s", pId);
		this.csMethod = "getUserAsync";
		RestAsyncTask loTask = new RestAsyncTask();
		loTask.addAsyncTaskListener(this);		
		loTask.execute("0",lsQueryUrl);
	}
	
	private void getUser(String pData){
		try {
			UserEntity loUser = null;
			
			if(!pData.equals("")){
				JSONObject jsonObj = new JSONObject(pData);
				String lsId = jsonObj.getString("USR_ID");
				String lsPassword = jsonObj.getString("USR_PASWORD");
				String lsNames = jsonObj.getString("USR_NAME");
				int liGender = jsonObj.getInt("GND_ID");
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
	
	public void addModelListener(ModelListener pModelListener){
		this.coModelListener.add(pModelListener);
	}
		
	@Override
	public void onQuerySuccessful(String pResult) {
		if(this.csMethod.equals("getUserAsync")){
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
