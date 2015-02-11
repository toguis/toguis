/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.interfaces.INavigation;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;
import co.edu.uniajc.vtf.receivers.NetworkStatusReceiver;
import co.edu.uniajc.vtf.utils.DirectionsEntity;
import co.edu.uniajc.vtf.utils.GMapV2Direction;
import co.edu.uniajc.vtf.utils.GetDirectionsAsyncTask;
import co.edu.uniajc.vtf.utils.OptionsManager;
import co.edu.uniajc.vtf.utils.ResourcesManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class NavigationActivity extends FragmentActivity implements 
	INavigation,
	GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener,
	LocationListener{

	private LatLng coCurrentPosition;
	private PointOfInterestEntity coDestiny;
	private GoogleMap coMap;
	private SupportMapFragment coFragment;
	private LatLngBounds coLatlngBounds;
	private Polyline coNewPolyline;
	private int ciWidth, ciHeight;
	private ProgressDialog coProgressDialog;
	private Location coLastLocation;
    private GoogleApiClient coGoogleApiClient;
    private LocationRequest coLocationRequest;
    private ArrayList<DirectionsEntity> coDirectionPoints;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		
		getSreenDimanstions();
	    coFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.routeMap));
		coMap = coFragment.getMap(); 	
		this.coDestiny = (PointOfInterestEntity)this.getIntent().getParcelableExtra("destiny");
		
		ResourcesManager loResource = new ResourcesManager(this);
		this.coProgressDialog = new ProgressDialog(this);
		this.coProgressDialog.setMessage(loResource.getStringResource(R.string.general_progress_message_loading));
		this.coProgressDialog.setCanceledOnTouchOutside(false);  
		
        this.coGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();           	
        this.setUpMap();
        this.loadPosition();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ComponentName loComponent = new ComponentName(this, NetworkStatusReceiver.class);
		this.getPackageManager().setComponentEnabledSetting(loComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED  , PackageManager.DONT_KILL_APP);					
		LatLng loDestiny = new LatLng(this.coDestiny.getLatitude(), this.coDestiny.getLongitude());
		
		if(this.coCurrentPosition != null){
	    	coLatlngBounds = createLatLngBoundsObject(this.coCurrentPosition, loDestiny);
	        coMap.moveCamera(CameraUpdateFactory.newLatLngBounds(coLatlngBounds, ciWidth, ciHeight, 150));
		}
	}

    @Override
    public void onStop() {
    	super.onStop();
    	try{
    		LocationServices.FusedLocationApi.removeLocationUpdates(coGoogleApiClient, this);	
    	}catch(Exception ex){}
    	this.hideProgressDialog();
    }
    
       @Override
    public void onPause() {
    	super.onPause();
   		ComponentName loComponent = new ComponentName(this, NetworkStatusReceiver.class);
   		this.getPackageManager().setComponentEnabledSetting(loComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED , PackageManager.DONT_KILL_APP);	  	
    	try{
    		LocationServices.FusedLocationApi.removeLocationUpdates(coGoogleApiClient, this);	
    	}catch(Exception ex){}
    	this.hideProgressDialog();
    }
       
	@Override
	public void handleGetDirectionsResult(ArrayList<DirectionsEntity> pDirectionPoints) {
		this.hideProgressDialog();
		this.coDirectionPoints = pDirectionPoints;
		PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.RED);

		for(DirectionsEntity direction : pDirectionPoints){			
			for(LatLng point : direction.getGeospoint()){
				rectLine.add(point);
			}
		}
		
		if (coNewPolyline != null)
		{
			coNewPolyline.remove();
		}
		coNewPolyline = coMap.addPolyline(rectLine);
		if (pDirectionPoints.size() > 0)
		{
			this.setAdapterData();
			LatLng loDestiny = new LatLng(this.coDestiny.getLatitude(), this.coDestiny.getLongitude());
	    	coLatlngBounds = createLatLngBoundsObject(this.coCurrentPosition, loDestiny);
	        coMap.animateCamera(CameraUpdateFactory.newLatLngBounds(coLatlngBounds, ciWidth, ciHeight, 150));
	        
		}
	
	}
	
	private void getSreenDimanstions()
	{
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		ciWidth = size.x;
		ciHeight = size.y;

	}
	
	private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation)
	{
		if (firstLocation != null && secondLocation != null)
		{
			LatLngBounds.Builder builder = new LatLngBounds.Builder();    
			builder.include(firstLocation).include(secondLocation);
			
			return builder.build();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode, String langIsoId)
	{	      	
		Map<String, String> loMap = new HashMap<String, String>();
		loMap.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
		loMap.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
		loMap.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
		loMap.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
		loMap.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);
		loMap.put(GetDirectionsAsyncTask.LANGUAGE, langIsoId);
		
		GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
		asyncTask.execute(loMap);	
	}

	@Override
	public void onError() {
		this.hideProgressDialog();
       	ResourcesManager loResource = new ResourcesManager(this);
        Toast.makeText(this.getApplicationContext(), loResource.getStringResource(R.string.general_db_error), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLocationChanged(Location location) {
		this.coLastLocation = location;
		if(location.hasAccuracy()){
			if(location.getAccuracy() <= 40){
				LocationServices.FusedLocationApi.removeLocationUpdates(coGoogleApiClient, this);	
				this.coCurrentPosition = new LatLng(this.coLastLocation.getLatitude(),this.coLastLocation.getLongitude());
				OptionsManager loOptions = new OptionsManager(this); 										
				this.findDirections(this.coCurrentPosition.latitude, this.coCurrentPosition.longitude, this.coDestiny .getLatitude(), this.coDestiny.getLongitude(), GMapV2Direction.MODE_WALKING, loOptions.getOptions().getLanguageIsoId());
			}
		}	
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		this.createLocationRequest();
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public void loadPosition(){
		this.showProgressDialog();
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
	
	public void showProgressDialog(){
		this.coProgressDialog.show();
	}

	public void hideProgressDialog(){
		this.coProgressDialog.dismiss();
	}
	
	private void createLocationRequest(){
		coLocationRequest = LocationRequest.create();
        coLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        coLocationRequest.setInterval(1000); // Update location every second
        LocationServices.FusedLocationApi.requestLocationUpdates(coGoogleApiClient, coLocationRequest, this);	
	}
	
    private void setUpMap()
    {
        // Do a null check to confirm that we have not already instantiated the map.
        if (this.coMap == null)
        {
        	
            // Try to obtain the map from the SupportMapFragment.
        	this.coMap = ((MapFragment) this.getFragmentManager().findFragmentById(R.id.routeMap)).getMap();
        	
            // Check if we were successful in obtaining the map.
            if (this.coMap != null)
            {
            	this.coMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);             	        
            	this.coMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                {
                    @Override
                    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker)
                    {
                        marker.showInfoWindow();
                        return true;
                    }
                });
            	this.coMap.setBuildingsEnabled(true);

            }
            else{
            	ResourcesManager loResource = new ResourcesManager(this);
                Toast.makeText(this.getApplicationContext(), loResource.getStringResource(R.string.general_load_map_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
    
	public void setAdapterData() {

		ResourcesManager loResource = new ResourcesManager(this);
		
        MarkerOptions loMarkerOption = new MarkerOptions().position(new LatLng(this.coDestiny.getLatitude(),this.coDestiny.getLongitude()));
        BitmapDescriptor loBitmapDescriptor = null; 
		switch(this.coDestiny.getSiteType()){
			case 1:
				loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.monument32);
				break;
			case 2:
				loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.museum32);
				break;
			case 3:
				loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.hotel32);
				break;
			case 4:
				loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.restaurant32);
				break;
			case 5:
				loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.interest32);
				break;
			case 6:
				loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.building32);
				break;
			case 7:
				loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.transport32);
				break;
			case 8:
				loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.events32);
				break;									
		}	        
        loMarkerOption.icon(loBitmapDescriptor);		
        Marker loMarker = this.coMap.addMarker(loMarkerOption);
		
		CameraPosition cameraPosition = new CameraPosition.Builder() 
			.target(this.coCurrentPosition)			// Sets location
		    .zoom(17)                   // Sets the zoom
		    .bearing(90)                // Sets the orientation of the camera to east
		    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
		    .build();                   // Creates a CameraPosition from the builder
		this.coMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		
        MarkerOptions loMarkerMyPos = new MarkerOptions().position(this.coCurrentPosition).title(loResource.getStringResource(R.string.general_map_current_position));
        this.coMap.addMarker(loMarkerMyPos);
        
		MapMarkerInfoWindowAdapter loAdpater = new MapMarkerInfoWindowAdapter(this.coDestiny, loMarker);
		this.coMap.setInfoWindowAdapter(loAdpater);
	}
	
	public class MapMarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
		private PointOfInterestEntity coData;
		private Marker coMarker;
		
		public MapMarkerInfoWindowAdapter(PointOfInterestEntity pData, Marker pMarker){
			this.coData = pData;
			this.coMarker = pMarker;
		}
		
		@Override
		public View getInfoContents(Marker marker) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public View getInfoWindow(Marker marker) {
            View loView  = NavigationActivity.this.getLayoutInflater().inflate(R.layout.map_info_data, null);

            ImageView loPointIcon = (ImageView) loView.findViewById(R.id.imgMapPointIcon);
			switch(coData.getSiteType()){
				case 1:
					loPointIcon.setImageResource(R.drawable.monument32);
					break;
				case 2:
					loPointIcon.setImageResource(R.drawable.museum32);
					break;
				case 3:
					loPointIcon.setImageResource(R.drawable.hotel32);
					break;
				case 4:
					loPointIcon.setImageResource(R.drawable.restaurant32);
					break;
				case 5:
					loPointIcon.setImageResource(R.drawable.interest32);
					break;
				case 6:
					loPointIcon.setImageResource(R.drawable.building32);
					break;
				case 7:
					loPointIcon.setImageResource(R.drawable.transport32);
					break;
				case 8:
					loPointIcon.setImageResource(R.drawable.events32);
					break;									
			}	     
	          
            TextView loPointName = (TextView)loView.findViewById(R.id.txtMapPointName);
            loPointName.setPaintFlags(loPointName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            loPointName.setText(coData.getTitle());

            return loView;
		}
		
	}
    
	public void onClick_Locate(View view){
		this.loadPosition();
	}
	
	public void onClick_Route(View view){
		Intent loIntent = new Intent(this, NavigationRouteActivity.class);		
		loIntent.putParcelableArrayListExtra("routemap", this.coDirectionPoints);
		this.startActivity(loIntent);		
	}
}
