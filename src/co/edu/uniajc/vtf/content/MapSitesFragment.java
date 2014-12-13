package co.edu.uniajc.vtf.content;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.ListSitesFragment.LoadActions;
import co.edu.uniajc.vtf.content.controller.MapSitesController;
import co.edu.uniajc.vtf.content.interfaces.IMapSites;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;
import co.edu.uniajc.vtf.utils.ResourcesManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapSitesFragment extends Fragment implements 
	   GoogleApiClient.ConnectionCallbacks,
	   GoogleApiClient.OnConnectionFailedListener,
	   LocationListener,
	   IMapSites
{
	private MapSitesController coController;
	private Location coLastLocation;
    private GoogleApiClient coGoogleApiClient;
    private LocationRequest coLocationRequest;
    private boolean cboForceUpdate;
    private ProgressDialog coProgressDialog;
    private GoogleMap coMap;
    private static View coView;
    HashMap<Marker, PointOfInterestEntity> coData = new HashMap<Marker, PointOfInterestEntity>();
    
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
    	if (coView != null) {
    	    ViewGroup parent = (ViewGroup) coView.getParent();
    	    if (parent != null)
    	        parent.removeView(coView);
    	}
    	try {
    	    coView = inflater.inflate(R.layout.fragment_sites_map, container, false);
    	} catch (InflateException e) {
    	    /* map is already there, just return view as it is */
    	}
    	return coView;
    }
    
    @Override
    public void onActivityCreated(Bundle state) {
    	super.onActivityCreated(state);	
    	this.setUpMap();
    	this.loadList(LoadActions.CONNECT_AND_LOAD);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.setHasOptionsMenu(true);    
		ResourcesManager loResource = new ResourcesManager(this.getActivity());
		this.coProgressDialog = new ProgressDialog(this.getActivity());
		this.coProgressDialog.setMessage(loResource.getStringResource(R.string.general_progress_message));
		this.coProgressDialog.setCanceledOnTouchOutside(false);
        coGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
            	
    	this.coController = new MapSitesController(this);	
    	this.coLastLocation = null;    	
    	this.cboForceUpdate = true;    	 
    	
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	this.hideProgressDialog();
    }
    
       @Override
    public void onPause() {
    	super.onPause();
    	this.hideProgressDialog();
    }

    private void setUpMap()
    {
        // Do a null check to confirm that we have not already instantiated the map.
        if (this.coMap == null)
        {
        	
            // Try to obtain the map from the SupportMapFragment.
        	this.coMap = ((MapFragment) this.getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
        	
            // Check if we were successful in obtaining the map.
            if (this.coMap != null)
            {
            	this.coMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);             	        
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
            	this.coMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                    	PointOfInterestEntity loPoint = MapSitesFragment.this.coData.get(marker);
                    	MapSitesFragment.this.coController.navigatePoiDetail(loPoint.getId());                    

                    }
                });
            }
            else{
            	ResourcesManager loResource = new ResourcesManager(this.getActivity());
                Toast.makeText(this.getActivity().getApplicationContext(), loResource.getStringResource(R.string.general_load_map_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
    
	public class MapMarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
		private HashMap<Marker, PointOfInterestEntity> coData;
		
		public MapMarkerInfoWindowAdapter(HashMap<Marker, PointOfInterestEntity> pData){
			this.coData = pData;
		}
		
		@Override
		public View getInfoContents(Marker marker) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public View getInfoWindow(Marker marker) {
            View loView  = MapSitesFragment.this.getActivity().getLayoutInflater().inflate(R.layout.map_info_data, null);

            PointOfInterestEntity loPoint = this.coData.get(marker);
            ImageView loPointIcon = (ImageView) loView.findViewById(R.id.imgMapPointIcon);
			switch(loPoint.getSiteType()){
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
            loPointName.setText(loPoint.getTitle());

            return loView;
		}
		
	}
    
	@Override
	public Location getCurrentLocation() {
		this.coLastLocation = LocationServices.FusedLocationApi.getLastLocation(coGoogleApiClient);		
		return this.coLastLocation;
	}

	@Override
	public void setAdapterData(ArrayList<PointOfInterestEntity> pData) {
		this.hideProgressDialog();
        
		
		for (PointOfInterestEntity point:  pData){
	        MarkerOptions loMarkerOption = new MarkerOptions().position(new LatLng(point.getLatitude(),point.getLongitude()));
	        BitmapDescriptor loBitmapDescriptor = null; 
			switch(point.getSiteType()){
				case 1:
					loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.monument24);
					break;
				case 2:
					loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.museum24);
					break;
				case 3:
					loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.hotel24);
					break;
				case 4:
					loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.restaurant24);
					break;
				case 5:
					loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.interest24);
					break;
				case 6:
					loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.building24);
					break;
				case 7:
					loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.transport24);
					break;
				case 8:
					loBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.events24);
					break;									
			}	        
	        loMarkerOption.icon(loBitmapDescriptor);		
	        Marker loMarker = this.coMap.addMarker(loMarkerOption);
	        coData.put(loMarker, point);
		}
		
		Location loLocation = this.getCurrentLocation();
		LatLng loPosition = new LatLng(loLocation.getLatitude(),loLocation.getLongitude());
		CameraPosition cameraPosition = new CameraPosition.Builder() 
			.target(loPosition)			// Sets location
		    .zoom(17)                   // Sets the zoom
		    .bearing(90)                // Sets the orientation of the camera to east
		    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
		    .build();                   // Creates a CameraPosition from the builder
		this.coMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		
        MarkerOptions loMarkerMyPos = new MarkerOptions().position(loPosition).title("My position");
        this.coMap.addMarker(loMarkerMyPos);
        
		MapMarkerInfoWindowAdapter loAdpater = new MapMarkerInfoWindowAdapter(coData);
		this.coMap.setInfoWindowAdapter(loAdpater);
	}

	
	@Override
	public void onLocationChanged(Location location) {
		this.coLastLocation = location;
		if(location.hasAccuracy()){
			if(location.getAccuracy() <= 40){
				this.coController.getSiteListAsync(this.cboForceUpdate);
				LocationServices.FusedLocationApi.removeLocationUpdates(coGoogleApiClient, this);	
				this.cboForceUpdate = false;
			}
		}		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
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

	//load the list when we connect with google play services
	public void loadList(LoadActions pLoadActions){
		if(pLoadActions == LoadActions.CONNECT_AND_LOAD){
			this.coGoogleApiClient.connect();
		}
		else if(pLoadActions == LoadActions.LOAD_DATA){
			if(this.coGoogleApiClient.isConnected()){
				this.createLocationRequest();				
			}
			else{
				this.coGoogleApiClient.connect();
			}
		}
		else if(pLoadActions == LoadActions.LOAD_CACHE){
			this.coController.getSiteListAsync(this.cboForceUpdate);
		}
	}
	
	public void unloadList(){
		this.coGoogleApiClient.disconnect();
	}

	//here we create and call the location callback to get the current location
	private void createLocationRequest(){
		coLocationRequest = LocationRequest.create();
        coLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        coLocationRequest.setInterval(1000); // Update location every second
        LocationServices.FusedLocationApi.requestLocationUpdates(coGoogleApiClient, coLocationRequest, this);
        this.showProgressDialog();		
	}
	
	@Override
	public void showProgressDialog(){
		this.coProgressDialog.show();
	}

	@Override
	public void hideProgressDialog(){
		this.coProgressDialog.dismiss();
	}


}
