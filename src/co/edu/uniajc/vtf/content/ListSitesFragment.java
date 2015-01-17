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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.controller.ListSitesController;
import co.edu.uniajc.vtf.content.interfaces.IListSites;
import co.edu.uniajc.vtf.content.interfaces.ILoadExtenalDataFromList;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;
import co.edu.uniajc.vtf.utils.OptionsEntity;
import co.edu.uniajc.vtf.utils.OptionsManager;
import co.edu.uniajc.vtf.utils.ResourcesManager;

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
    private ProgressDialog coProgressDialog;
    private ArrayList<ILoadExtenalDataFromList> coLoadTargets;
    
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sites_list, container, false);        
    }
	
    @Override
    public void onActivityCreated(Bundle state) {
    	super.onActivityCreated(state);
    	ListView loList = (ListView)this.getView().findViewById(R.id.lstPoints);
    	loList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	ListSitesFragment.this.onClickListItem(parent,view,position,id);
            }
          });    	
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
        
    	this.coController = new ListSitesController(this);	
    	this.coLastLocation = null;    	
    	this.cboForceUpdate = true;
    	this.loadList(LoadActions.CONNECT_AND_LOAD);
    	 
    }
    
    public void onClickListItem(AdapterView<?> parent, View view, int position, long id){
    	int liPoiId = ((PointOfInterestEntity)this.getAdapter().getItem(position)).getId();
    	this.coController.navigatePoiDetail(liPoiId);
    }
    
    public void addLoadListeners(ILoadExtenalDataFromList pLoadListener){
    	if(this.coLoadTargets == null){
    		this.coLoadTargets = new ArrayList<ILoadExtenalDataFromList>();
    	}
    	this.coLoadTargets.add(pLoadListener);
    }
    
    @Override
    public void onStart() {
    	super.onStart();
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
    	try{
    		LocationServices.FusedLocationApi.removeLocationUpdates(coGoogleApiClient, this);	
    	}catch(Exception ex){}
    	this.hideProgressDialog();
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
       	ResourcesManager loResource = new ResourcesManager(this.getActivity()); 
        Toast.makeText(this.getActivity().getApplicationContext(), pPoints.size() + " " + loResource.getStringResource(R.string.ar_added_data_text), Toast.LENGTH_LONG).show();       
		this.setAdapterData(pPoints, true);
	}
	
	@Override
	public void setAdapterData(ArrayList<PointOfInterestEntity> pPoints, boolean triggerEvent) {		
		if(triggerEvent){
			for(ILoadExtenalDataFromList loadTarget : this.coLoadTargets){
				loadTarget.loadDataFromList(pPoints, this.coLastLocation);
			}			
		}
		this.hideProgressDialog();
    	ListPointsAdapter loAdapter = new ListPointsAdapter(this.getActivity(), pPoints);
    	this.setAdapter(loAdapter);		
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
		Location loLastLocation = LocationServices.FusedLocationApi.getLastLocation(coGoogleApiClient);	
		if(loLastLocation != null){
			this.coLastLocation = loLastLocation;
		}	
		return this.coLastLocation;
	}

	public boolean googlePlusIsInstalled(){
		int liErrorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
		return liErrorCode == ConnectionResult.SUCCESS ? true : false;
	}
	
	//load the list when we connect with google play services
	public void loadList(LoadActions pLoadActions){
		if(googlePlusIsInstalled()){
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
		else{
           	ResourcesManager loResource = new ResourcesManager(this.getActivity());
            Toast.makeText(this.getActivity().getApplicationContext(), loResource.getStringResource(R.string.general_google_play_not_installed), Toast.LENGTH_SHORT).show();
		}

	}
	
	public void unloadList(){
		this.coGoogleApiClient.disconnect();
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
				loDialog.setCanceledOnTouchOutside(false);
				loDialog.show();
		    	OptionsManager loOptions = new OptionsManager(this.getActivity()); 
		    	OptionsEntity loOptionsData = loOptions.getOptions();
		    	EditText loSearchControl = (EditText)loDialog.findViewById(R.id.txtSearch);
		    	loSearchControl.setText(loOptionsData.getSearch());				
				break;
			case R.id.action_refresh:
				ListSitesFragment.this.cboForceUpdate = true;
				this.loadList(LoadActions.LOAD_DATA);
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
            	loOptions.createOrUpdateOptions(loOptionsData);
            	ListSitesFragment.this.cboForceUpdate = true;
            	ListSitesFragment.this.loadList(LoadActions.LOAD_DATA);
            	dialog.dismiss();
            }
        });
    	
    	loAlert.setNegativeButton(R.string.general_menus_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            	dialog.cancel();
            	dialog.dismiss();
            }
        });
    	
    	loAlert.setNeutralButton(R.string.general_menus_clear, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            	OptionsManager loOptions = new OptionsManager(ListSitesFragment.this.getActivity()); 
            	OptionsEntity loOptionsData = loOptions.getOptions();
            	loOptionsData.setSearch("");
            	loOptions.createOrUpdateOptions(loOptionsData);
            	EditText loSearchControl = (EditText)((AlertDialog)dialog).findViewById(R.id.txtSearch);
            	loSearchControl.setText("");
            	ListSitesFragment.this.cboForceUpdate = true;
            	ListSitesFragment.this.loadList(LoadActions.LOAD_DATA);   
            	dialog.dismiss();
            }
        });	
    	loAlert.setCancelable(false);
    	return loAlert.create();		
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

	public Location getLastLocation(){
		return this.coLastLocation;
	}
	
	public void setLastLocation(Location pLastLocation){
		this.coLastLocation = pLastLocation;
	}
	
	public enum LoadActions{
		CONNECT_AND_LOAD,
		LOAD_DATA,
		LOAD_CACHE
	}
}
