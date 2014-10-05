package co.edu.uniajc.vtf.security.model;

import java.util.ArrayList;

import co.edu.uniajc.vtf.utils.BaseModel;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.RestAsyncTask;

public class ForgotPasswordModel  extends BaseModel {

	public ForgotPasswordModel(String pBaseUrl) {
		super.csBaseUrl = pBaseUrl;
		super.csMethod = "";
		super.coModelListener = new ArrayList<ModelListener>();		
	}

	public void recoverPasswordAsync(String pEmail){
		String lsQueryUrl = super.csBaseUrl + "ToguisSecurity.svc/recover_user/" + pEmail;		
		super.csMethod = "recoverPasswordAsync";
		RestAsyncTask loTask = new RestAsyncTask();
		loTask.addAsyncTaskListener(this);		
		loTask.execute("1", lsQueryUrl, "");		
	}
	
	private void recoverPassword(String pResult){

		if(!pResult.equals("") && pResult.equals("0")){
			for (ModelListener item : this.coModelListener){
				item.onGetData(0, 0);
			}
		}	
		else{
			for (ModelListener item : this.coModelListener){
				item.onGetData(1, 0);
			}							
		}			
	}
	
	@Override
	public void onQuerySuccessful(String result) {
		if(super.csMethod.equals("recoverPasswordAsync")){
			this.recoverPassword(result);
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
