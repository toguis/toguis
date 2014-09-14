package co.edu.uniajc.vtf.utils;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class RestAsyncTask extends AsyncTask<String, Long, String> {
	private final HttpClient Client = new DefaultHttpClient();
	private ArrayList<RestAsyncTaskListener> coAsyncTaskListener;
	private boolean cbohasError;
	
	public RestAsyncTask(){
		this.cbohasError = false;
		this.coAsyncTaskListener = new ArrayList<RestAsyncTaskListener>();
	}
	
	@Override
	protected String doInBackground(String... params) {
		String csResult = "";
		try {
			HttpGet loRequest = new HttpGet(params[0].toString());	
			HttpResponse loResponse = Client.execute(loRequest);
			csResult = EntityUtils.toString(loResponse.getEntity());			
		} catch (Exception ex) {
			this.cbohasError = true;
			for (RestAsyncTaskListener item : this.coAsyncTaskListener){
				item.onQueryError(ex.getMessage());
			}
		} 
		return csResult;
	}

	 @Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(!this.cbohasError){
			for (RestAsyncTaskListener item : this.coAsyncTaskListener){
				item.onQuerySuccessful(result);
			}				
		}
	}
	
	public final void addAsyncTaskListener(RestAsyncTaskListener pAsyncListener) {
		this.coAsyncTaskListener.add(pAsyncListener);
	}	
	
}
