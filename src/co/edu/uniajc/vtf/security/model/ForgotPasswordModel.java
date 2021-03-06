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

import android.content.Context;
import co.edu.uniajc.vtf.utils.BaseModel;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.RestAsyncTask;

public class ForgotPasswordModel  extends BaseModel {

	public ForgotPasswordModel(String pBaseUrl, Context pContext) {
		super.csBaseUrl = pBaseUrl;
		super.csMethod = "";
		super.coModelListener = new ArrayList<ModelListener>();		
		super.coContext = pContext;
	}

	public void recoverPasswordAsync(String pEmail){
		String lsQueryUrl = super.csBaseUrl + "ToguisSecurity.svc/recover_user/" + pEmail;		
		super.csMethod = "recoverPasswordAsync";
		RestAsyncTask loTask = new RestAsyncTask(super.coContext);
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
