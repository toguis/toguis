package co.edu.uniajc.vtf.content;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.controller.ListSitesController;
import co.edu.uniajc.vtf.content.interfaces.IListSites;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;
import co.edu.uniajc.vtf.utils.OptionsEntity;
import co.edu.uniajc.vtf.utils.OptionsManager;

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
    private boolean cboForceUpdate;
    
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
    	this.setHasOptionsMenu(true);    

    	
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
			
			ImageView loVisited = (ImageView)loView.findViewById(R.id.imgPointVisited);
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
		//unknown exception here, I don't know what happen but
		//I insert this ex block for protect the app against this bug.
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
				this.coController.getSiteListAsync(this.cboForceUpdate);
				ListSitesFragment.this.cboForceUpdate = false; 
				LocationServices.FusedLocationApi.removeLocationUpdates(coGoogleApiClient, this);				
			}
		}
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		this.createLocationRequest();
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
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {		
	    inflater.inflate(R.menu.menu_fragment_sites_list, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_search:
				Dialog loDialog = this.createDialog();
				loDialog.show();
		    	OptionsManager loOptions = new OptionsManager(this.getActivity()); 
		    	OptionsEntity loOptionsData = loOptions.getOptions();
		    	EditText loSearchControl = (EditText)loDialog.findViewById(R.id.txtSearch);
		    	loSearchControl.setText(loOptionsData.getSearch());				
				break;
			case R.id.action_refresh:
				this.createLocationRequest();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public Dialog createDialog(){
    	LayoutInflater loInflater = this.getActivity().getLayoutInflater(); 	
    	
    	AlertDialog.Builder loAlert = new AlertDialog.Builder(getActivity());
    	loAlert.setView(loInflater.inflate(R.layout.dialog_search, null));
    	loAlert.setPositiveButton(R.string.general_menus_search, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            	OptionsManager loOptions = new OptionsManager(ListSitesFragment.this.getActivity()); 
            	OptionsEntity loOptionsData = loOptions.getOptions();
            	EditText loSearchControl = (EditText)((AlertDialog)dialog).findViewById(R.id.txtSearch);
            	loOptionsData.setSearch(loSearchControl.getText().toString());
            	loOptions.createOptions(loOptionsData);
            	ListSitesFragment.this.cboForceUpdate = true;
            	ListSitesFragment.this.createLocationRequest();
            }
        });
    	
    	loAlert.setNegativeButton(R.string.general_menus_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            	dialog.cancel();
            }
        });
    	
    	loAlert.setNeutralButton(R.string.general_menus_clear, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            	OptionsManager loOptions = new OptionsManager(ListSitesFragment.this.getActivity()); 
            	OptionsEntity loOptionsData = loOptions.getOptions();
            	loOptionsData.setSearch("");
            	loOptions.createOptions(loOptionsData);
            	EditText loSearchControl = (EditText)((AlertDialog)dialog).findViewById(R.id.txtSearch);
            	loSearchControl.setText("");
            	ListSitesFragment.this.cboForceUpdate = true;
            	ListSitesFragment.this.createLocationRequest();            	
            }
        });	
    	return loAlert.create();		
    }
	
	
	//here we create and call the location callback to get the current location
	public void createLocationRequest(){
		coLocationRequest = LocationRequest.create();
        coLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        coLocationRequest.setInterval(1000); // Update location every second
        LocationServices.FusedLocationApi.requestLocationUpdates(coGoogleApiClient, coLocationRequest, this);
        this.coController.showProgressDialog();		
	}
	
}
