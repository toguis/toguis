/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.ar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.Toast;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.PoiDetailActivity;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;
import co.edu.uniajc.vtf.receivers.NetworkStatusReceiver;
import co.edu.uniajc.vtf.utils.ExtendedApplicationContext;
import co.edu.uniajc.vtf.utils.ResourcesManager;
import co.edu.uniajc.vtf.wikitude.WikitudeLicence;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectUrlListener;

//AR Activity
public class ARViewActivity extends Activity implements 
	GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener,
	LocationListener{
	protected ArchitectView	architectView;
	protected ArchitectUrlListener urlListener;
	private GoogleApiClient coGoogleApiClient;
	private LocationRequest coLocationRequest;
	private Location coLastLocation;
	private String coPois;
	private boolean cboDataLoaded;
	protected PowerManager.WakeLock coWakelock;
	
	final Runnable coLoadData = new Runnable() {	
		@Override
		public void run() {
			if(ARViewActivity.this.coLastLocation != null && !ARViewActivity.this.isFinishing()){
				
				//get current location
				double ldLatitude = ARViewActivity.this.coLastLocation.getLatitude();
				double ldLongitude = ARViewActivity.this.coLastLocation.getLongitude();
				double ldAltitude = ARViewActivity.this.coLastLocation.getAltitude();
				
				//load text message resources to show in AR view 
				ResourcesManager loResource = new ResourcesManager(ARViewActivity.this);
				String lsARAddedMessage = "'" + loResource.getStringResource(R.string.ar_added_data_text) + "'";
				String lsARErrorMessage = "'" + loResource.getStringResource(R.string.ar_error_data_text) + "'";				
				String lsARSearchMessage = "'" + loResource.getStringResource(R.string.ar_search_text) + "'";
						
				//call AR View 
				String lsScript = ARViewActivity.this.callJavaScript("World.loadPoisFromJsonData", 
						new String[] { 
							ARViewActivity.this.coPois.toString(),
							String.valueOf(ldLatitude),
							String.valueOf(ldLongitude),
							String.valueOf(ldAltitude),
							lsARAddedMessage,
							lsARErrorMessage,
							lsARSearchMessage
						}
				);
				if(!ARViewActivity.this.cboDataLoaded){
					ARViewActivity.this.architectView.callJavascript(lsScript);
					ARViewActivity.this.cboDataLoaded = true;
				}		
				
				//Set the location 
				ARViewActivity.this.architectView.setLocation(ldLatitude, ldLongitude, (float)ldAltitude );		
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arview);
		this.architectView = (ArchitectView)this.findViewById( R.id.architectView );
		try {
			this.architectView.onCreate( WikitudeLicence.LICENCE_KEY );
			this.urlListener = new ArchitectUrlListener() {

				@Override
				public boolean urlWasInvoked(String uriString) {
					return ARViewActivity.this.listenURL(uriString);
				}
			};
			this.architectView.registerUrlListener(this.urlListener);
			
		} catch (RuntimeException rex) {
			this.architectView = null;
			ResourcesManager loResource = new ResourcesManager(this);
			Toast.makeText(getApplicationContext(), loResource.getStringResource(R.string.general_ar_error), Toast.LENGTH_SHORT).show();
		}	
		
        this.coGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();    
        
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);      
        this.loadPosition();
       
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {		
		super.onPostCreate(savedInstanceState);
		if ( this.architectView != null ) {
			this.architectView.onPostCreate();
			try {
				//here we load the html AR view
				this.architectView.load("ARPage/ARView.html");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	@Override
	protected void onResume() {
		super.onResume();
		ComponentName loComponent = new ComponentName(this, NetworkStatusReceiver.class);
		this.getPackageManager().setComponentEnabledSetting(loComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED  , PackageManager.DONT_KILL_APP);	
		this.loadPosition();
		if ( this.architectView != null ) {
			this.architectView.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		ComponentName loComponent = new ComponentName(this, NetworkStatusReceiver.class);
		this.getPackageManager().setComponentEnabledSetting(loComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED , PackageManager.DONT_KILL_APP);	
		LocationServices.FusedLocationApi.removeLocationUpdates(coGoogleApiClient, this);	
		if ( this.architectView != null ) {
			this.architectView.onPause();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocationServices.FusedLocationApi.removeLocationUpdates(coGoogleApiClient, this);	
		if ( this.architectView != null ) {
			this.architectView.onDestroy();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		this.coLastLocation = location;
		if(location.hasAccuracy()){
			//if acccuracy is lower o equals than 40 call AR View
			if(location.getAccuracy() <= 40){			
				this.callARView();				
			}
		}	
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {

	}

	@Override
	public void onConnected(Bundle arg0) {
		this.createLocationRequest();
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {

	}
	
	public void loadPosition(){
		if(googlePlusIsInstalled()){
			if(this.coGoogleApiClient.isConnected()){
				this.createLocationRequest();
			}
			else{
				this.coGoogleApiClient.connect();
			}
		}
		else{
           	ResourcesManager loResource = new ResourcesManager(this);
            Toast.makeText(this.getApplicationContext(), loResource.getStringResource(R.string.general_google_play_not_installed), Toast.LENGTH_SHORT).show();
		}
	}
	
	public boolean googlePlusIsInstalled(){
		int liErrorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		return liErrorCode == ConnectionResult.SUCCESS ? true : false;
	}

	private void createLocationRequest(){
		coLocationRequest = LocationRequest.create();
        coLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        coLocationRequest.setInterval(1000); // Update location every second
        LocationServices.FusedLocationApi.requestLocationUpdates(coGoogleApiClient, coLocationRequest, this);	
	}
	
	//call the AR view to inject json poi data
	public void callARView(){
		ExtendedApplicationContext loContext = (ExtendedApplicationContext)this.getApplication();
		ArrayList<PointOfInterestEntity> loPoints = loContext.getBufferPoints();	
		if(loPoints != null){
			this.coPois = this.createJsonArray(loPoints).toString();
			//here we create a thread to call the AR View
			final Thread loThread = new Thread(this.coLoadData);
			loThread.run();
		}
	}
	
	//Method that create json poi data
	private JSONArray createJsonArray(ArrayList<PointOfInterestEntity> loPoints){
		
		final JSONArray loPois = new JSONArray();	
		for(PointOfInterestEntity point : loPoints){
			final HashMap<String, String> loPoiInformation = new HashMap<String, String>();
			loPoiInformation.put("id", String.valueOf(point.getId()));
			loPoiInformation.put("longitude", String.valueOf(point.getLongitude()));
			loPoiInformation.put("latitude", String.valueOf(point.getLatitude()));
			loPoiInformation.put("altitude", String.valueOf(point.getAltitude()));
			loPoiInformation.put("distance", "0");
			loPoiInformation.put("type", String.valueOf(point.getSiteType()));
			loPoiInformation.put("title", point.getTitle());
			loPoiInformation.put("description", point.getDescription());
			loPois.put(new JSONObject(loPoiInformation));
		}
		
		return loPois;
	}

	//Method that build javascript AR view call
	private String callJavaScript(final String methodName, final String[] arguments) {
		final StringBuilder argumentsString = new StringBuilder("");
		for (int i= 0; i<arguments.length; i++) {
			argumentsString.append(arguments[i]);
			if (i<arguments.length-1) {
				argumentsString.append(", ");
			}
		}	
		return methodName + "( " + argumentsString.toString() + " );" ;		
	}

	//Open detail activity through this method
	public boolean listenURL(String uriString) {
		Uri invokedUri = Uri.parse(uriString);
		if ("markerselected".equalsIgnoreCase(invokedUri.getHost())) {
			final Intent poiDetailIntent = new Intent(ARViewActivity.this, PoiDetailActivity.class);
			String lsId = invokedUri.getQueryParameter("id");
			poiDetailIntent.putExtra("id", Integer.parseInt(lsId));
			ARViewActivity.this.startActivity(poiDetailIntent);
			return true;
		}	
		else{
			return false;
		}
	}
}
