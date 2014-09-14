package co.edu.uniajc.vtf.utils;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
		HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
		HttpConnectionParams.setSoTimeout(httpParameters, 10000);		
		this.coClient = new DefaultHttpClient(httpParameters);
		this.cbohasError = false;
		this.coAsyncTaskListener = new ArrayList<RestAsyncTaskListener>();
	}
	
	@Override
	protected String doInBackground(String... params) {
		String lsResult = "";
		try {
			HttpGet loRequest = new HttpGet(params[0].toString());			
			HttpResponse loResponse = coClient.execute(loRequest);
			lsResult = EntityUtils.toString(loResponse.getEntity());					
		} catch (Exception ex) {
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
