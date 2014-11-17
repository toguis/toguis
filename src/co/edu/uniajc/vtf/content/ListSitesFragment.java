package co.edu.uniajc.vtf.content;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.controller.ListSitesController;
import co.edu.uniajc.vtf.content.interfaces.IListSites;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class ListSitesFragment extends Fragment implements 
	   IListSites,
	   GoogleApiClient.ConnectionCallbacks,
	   GoogleApiClient.OnConnectionFailedListener,
       LocationListener
{
	private ListSitesController coController;
	private Location coLastLocation;
    private GoogleApiClient coGoogleApiClient;
    private LocationRequest coLocationRequest;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sites_list, container, false);                
    }
	
    @Override
    public void onActivityCreated(Bundle state) {
    	super.onActivityCreated(state);
    }
    	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
       	
        coGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
            	
    	this.coController = new ListSitesController(this);	
    	this.coLastLocation = null;    	
    	this.loadList();
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    }
    
    public static class ListPointsAdapter extends BaseAdapter{
    	private Context coContext;
    	private ArrayList<PointOfInterestEntity> coData; 
		public ListPointsAdapter(Context pContext, ArrayList<PointOfInterestEntity> pData) {
			this.coContext = pContext;
			this.coData = pData;
		}

		@Override
		public int getCount() {
			return coData.size();
		}

		@Override
		public Object getItem(int position) {
			return this.coData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View loView = null;
			if(convertView == null){
				LayoutInflater loInflater = (LayoutInflater)this.coContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				loView = loInflater.inflate(R.layout.list_points_item, null);				
			}
			else{
				loView = convertView;
			}
			PointOfInterestEntity loPoint = this.coData.get(position);
			
			TextView loTitle = (TextView)loView.findViewById(R.id.lblPointTitle);
			loTitle.setText(loPoint.getTitle());
			
			TextView loDescription = (TextView)loView.findViewById(R.id.lblPointDesc);
			loDescription.setText(loPoint.getDescription());
			int liImage = 0;
			
			switch(loPoint.getSiteType()){
				case 1:
					liImage = R.drawable.monument48;
					break;
				case 2:
					liImage = R.drawable.museum48;
					break;
				case 3:
					liImage = R.drawable.hotel48;
					break;
				case 4:
					liImage = R.drawable.restaurant48;
					break;
				case 5:
					liImage = R.drawable.interest48;
					break;
				case 6:
					liImage = R.drawable.building48;
					break;
				case 7:
					liImage = R.drawable.transport48;
					break;
				case 8:
					liImage = R.drawable.events48;
					break;									
			}
			
			ImageView loImage = (ImageView)loView.findViewById(R.id.imgPoint);
			loImage.setImageResource(liImage);
			
			ImageView loVisited =  (ImageView)loView.findViewById(R.id.imgPointVisited);
			if(loPoint.isVisited()){		
				loVisited.setImageResource(R.drawable.visited16);	
			}
			else{
				loVisited.setImageResource(R.drawable.empty16);	
				
			}
			
			ImageView loFavorite =  (ImageView)loView.findViewById(R.id.imgPointFavorite);
			if(loPoint.isFavorite()){			
				loFavorite.setImageResource(R.drawable.favorite16);									
			}
			else{
				loFavorite.setImageResource(R.drawable.empty16);
			}

			return loView;
		}

	
    }

	@Override
	public void setAdapter(ListPointsAdapter pAdapter) {
		//unknown exception here, I dont know waht happen but
		//I insert this ex block for protect the app over this bug.
		try{
	    	ListView loList = (ListView)this.getView().findViewById(R.id.lstPoints);
	    	loList.setAdapter(pAdapter);			
		}
		catch(Exception ex){
	
		}
	}

	@Override
	public ListPointsAdapter getAdapter() {
    	ListView loList = (ListView)this.getView().findViewById(R.id.lstPoints);
		return (ListPointsAdapter)loList.getAdapter();
	}

	@Override
	public void setAdapterData(ArrayList<PointOfInterestEntity> pPoints) {		
		this.coController.hideProgressDialog();
    	ListPointsAdapter loAdapter = new ListPointsAdapter(this.getActivity(), pPoints);
    	this.setAdapter(loAdapter);		
	}

	@Override
	public void onLocationChanged(Location location) {				
		this.coLastLocation = location;
		if(location.hasAccuracy()){
			if(location.getAccuracy() <= 40){
				this.coController.getSiteListAsync();
				LocationServices.FusedLocationApi.removeLocationUpdates(coGoogleApiClient, this);				
			}
		}
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		coLocationRequest = LocationRequest.create();
        coLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        coLocationRequest.setInterval(1000); // Update location every second
        LocationServices.FusedLocationApi.requestLocationUpdates(coGoogleApiClient, coLocationRequest, this);
        this.coController.showProgressDialog();
	}

	@Override
	public void onConnectionSuspended(int cause) {
		
	}

	@Override
	public Location getCurrentLocation() {
		this.coLastLocation = LocationServices.FusedLocationApi.getLastLocation(coGoogleApiClient);		
		return this.coLastLocation;
	}

	public boolean googlePlusIsInstalled(){
		int liErrorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
		return liErrorCode == ConnectionResult.SUCCESS ? true : false;
	}
	
	public void loadList(){		
		//load the list when we connect with google play services
    	coGoogleApiClient.connect();
	}
	
	public void unloadList(){
		coGoogleApiClient.disconnect();
	}
}
