package co.edu.uniajc.vtf.security.model;

import java.util.ArrayList;
import java.util.Calendar;

import co.edu.uniajc.vtf.utils.BaseModel;
import co.edu.uniajc.vtf.utils.DateUtilities;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.RestAsyncTask;

public class CreateAccountModel extends BaseModel  {
		
	public CreateAccountModel(String pBaseUrl) {
		super.csBaseUrl = pBaseUrl;
		super.csMethod = "";
		super.coModelListener = new ArrayList<ModelListener>();
		
	}

	public void createAccountAsync(UserEntity pUser){
		StringBuilder loData = new StringBuilder();
		String loDate = DateUtilities.getFormattedDate(Calendar.getInstance());
		loData.append("{");
		loData.append("\"AUTH_ID\":3,");
		loData.append("\"GND_ID\":\"").append(pUser.getGender()).append("\",");
		loData.append("\"ROL_ID\":2,");
		loData.append("\"USR_EMAIL\":\"").append(pUser.getEmail()).append("\",");
		loData.append("\"USR_ID\":\"").append(pUser.getEmail()).append("\",");
		loData.append("\"USR_IMAGE\":null,");
		loData.append("\"USR_LAST_LOGIN\":\"" + loDate +  "\",");
		loData.append("\"USR_NAME\":\"").append(pUser.getNames()).append("\",");
		loData.append("\"USR_PASWORD\":\"").append(pUser.getPassword()).append("\",");
		loData.append("\"USR_PHONE_NUMBER\":null");
		loData.append("}");
		
		String lsQueryUrl = super.csBaseUrl + "ToguisSecurity.svc/create_user";		
		super.csMethod = "createAccountAsync";
		RestAsyncTask loTask = new RestAsyncTask();
		loTask.addAsyncTaskListener(this);		
		loTask.execute("1",lsQueryUrl,loData.toString());
	}
	
	private void createAccount(String pResult){

		if(!pResult.equals("") && pResult.equals("0")){
			for (ModelListener item : this.coModelListener){
				item.onGetData(0, 0);
			}
		}	
		else if(!pResult.equals("") && pResult.equals("1")){
			for (ModelListener item : this.coModelListener){
				item.onGetData(1, 0);
			}				
		}
		else{
			for (ModelListener item : this.coModelListener){
				item.onGetData(2, 0);
			}							
		}			
	}
	
	@Override
	public void onQuerySuccessful(String pResult) {
		if(super.csMethod.equals("createAccountAsync")){
			this.createAccount(pResult);
		}
		
	}

	@Override
	public void onQueryError(String error) {
		for (ModelListener item : this.coModelListener){
			if(super.csMethod.equals("createAccountAsync")){
				item.onError(error, 0);
			}			
		}	
		
	}
}
