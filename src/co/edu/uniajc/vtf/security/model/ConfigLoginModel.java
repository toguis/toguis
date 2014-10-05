package co.edu.uniajc.vtf.security.model;

import java.util.ArrayList;
import java.util.Calendar;

import co.edu.uniajc.vtf.utils.DateUtilities;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.RestAsyncTask;
import co.edu.uniajc.vtf.utils.RestAsyncTaskListener;

public class ConfigLoginModel implements RestAsyncTaskListener  {
	private String csMethod;
	private String csBaseUrl;
	private ArrayList<ModelListener> coModelListener;
	private int ciAuthId;
	private UserEntity coUser;
	
	public ConfigLoginModel(String pBaseUrl) {
		this.csBaseUrl = pBaseUrl;
		this.csMethod = "";
		this.coModelListener = new ArrayList<ModelListener>();		
	}
	
	public void updateAccountAsync(UserEntity pUser, int pAuthId){

		String lsData = this.getFomattedData(pUser, pAuthId);
		String lsQueryUrl = this.csBaseUrl + "ToguisSecurity.svc/update_user";		
		this.csMethod = "updateAccountAsync";
		RestAsyncTask loTask = new RestAsyncTask();
		loTask.addAsyncTaskListener(this);		
		loTask.execute("1", lsQueryUrl, lsData);
	}	
	
	private void updateAccount(String pResult){		
		if(!pResult.equals("") && pResult.equals("0")){
			for (ModelListener item : this.coModelListener){
				item.onGetData(0, 1);
			}
		}	
		else if(!pResult.equals("") && pResult.equals("1")){
			for (ModelListener item : this.coModelListener){
				item.onGetData(1, 1);
			}				
		}
		else{
			for (ModelListener item : this.coModelListener){
				item.onGetData(2, 1);
			}							
		}			
	}	
	
	public void createAccountAsync(UserEntity pUser, int pAuthId){	
		this.coUser = pUser;
		this.ciAuthId = pAuthId;
		String lsData = this.getFomattedData(pUser, pAuthId);
		String lsQueryUrl = this.csBaseUrl + "ToguisSecurity.svc/create_user";		
		this.csMethod = "createAccountAsync";
		RestAsyncTask loTask = new RestAsyncTask();
		loTask.addAsyncTaskListener(this);		
		loTask.execute("1", lsQueryUrl, lsData);
	}	
	
	private void createAccount(String pResult){
		if(!pResult.equals("") && pResult.equals("0")){
			for (ModelListener item : this.coModelListener){
				item.onGetData(0, 0);
			}			
		}
		else{
			this.updateAccountAsync(this.coUser, this.ciAuthId);
		}
				
	}	
	
	private String getFomattedData(UserEntity pUser, int pAuthId){
		StringBuilder loData = new StringBuilder();
		String loDate = DateUtilities.getFormattedDate(Calendar.getInstance());
		loData.append("{");
		loData.append("\"AUTH_ID\":").append(pAuthId).append(",");
		loData.append("\"GND_ID\":\"").append(pUser.getGender()).append("\",");
		loData.append("\"ROL_ID\":2,");
		loData.append("\"USR_EMAIL\":\"").append(pUser.getEmail()).append("\",");
		loData.append("\"USR_ID\":\"").append(pUser.getEmail()).append("\",");
		loData.append("\"USR_IMAGE\":null,");
		loData.append("\"USR_LAST_LOGIN\":\"" + loDate +  "\",");
		loData.append("\"USR_NAME\":\"").append(pUser.getNames()).append("\",");
		loData.append("\"USR_PASWORD\":").append(pUser.getPassword() == null ? "null" : "\" "  + pUser.getPassword() + " \"").append(",");
		loData.append("\"USR_PHONE_NUMBER\":null");
		loData.append("}");
		return loData.toString();
	}
	
	public void addModelListener(ModelListener pModelListener){
		this.coModelListener.add(pModelListener);
	}
	
	@Override
	public void onQuerySuccessful(String pResult) {
		if(this.csMethod.equals("updateAccountAsync")){
			this.updateAccount(pResult);
		}	
		else if(this.csMethod.equals("createAccountAsync")){
			this.createAccount(pResult);
		}
	}

	@Override
	public void onQueryError(String error) {
		for (ModelListener item : this.coModelListener){
			if(this.csMethod.equals("updateAccountAsync")){
				item.onError(error, 0);
			}	
			else if(this.csMethod.equals("createAccountAsync")){
				item.onError(error, 1);
			}
		}	
	}

}
