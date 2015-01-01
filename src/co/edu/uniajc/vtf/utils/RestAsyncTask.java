/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.utils;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class RestAsyncTask extends AsyncTask<String, Long, String> {
	private final HttpClient coClient;
	private ArrayList<RestAsyncTaskListener> coAsyncTaskListener;
	private boolean cbohasError;
	
	public RestAsyncTask(){		
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
		HttpConnectionParams.setSoTimeout(httpParameters, 10000);		
		this.coClient = new DefaultHttpClient(httpParameters);
		this.cbohasError = false;
		this.coAsyncTaskListener = new ArrayList<RestAsyncTaskListener>();
	}
	
	@Override
	protected String doInBackground(String... params) {
		String lsResult = "";
		try {
			if(params[0].equals("0")){
				HttpGet loRequest = new HttpGet(params[1].toString());			
				HttpResponse loResponse = coClient.execute(loRequest);
				lsResult = EntityUtils.toString(loResponse.getEntity());					
			}
			else if(params[0].equals("1")){
				HttpPost loRequest = new HttpPost(params[1].toString());	
				StringEntity lsData = new StringEntity(params[2].toString());
				loRequest.setEntity(lsData);
				loRequest.setHeader("Accept", "application/json");
				loRequest.setHeader("Content-type", "application/json");
	            
				HttpResponse loResponse = coClient.execute(loRequest);
				lsResult = EntityUtils.toString(loResponse.getEntity());				
			}
				
		} catch (SocketTimeoutException ex) {
			this.cbohasError = true;
			lsResult = "Timeout exception";
		} catch (Exception ex){
			this.cbohasError = true;
			lsResult = ex.getMessage();			
		}
		 
		return lsResult;
	}

	 @Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(!this.cbohasError){
			for (RestAsyncTaskListener item : this.coAsyncTaskListener){
				item.onQuerySuccessful(result);
			}				
		}
		else{
			for (RestAsyncTaskListener item : this.coAsyncTaskListener){
				item.onQueryError(result);
			}	
		}
	}
	
	public final void addAsyncTaskListener(RestAsyncTaskListener pAsyncListener) {
		this.coAsyncTaskListener.add(pAsyncListener);
	}	
	
}
