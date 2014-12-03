package co.edu.uniajc.vtf.content.controller;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.location.Location;
import android.support.v4.app.Fragment;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.interfaces.IListSites;
import co.edu.uniajc.vtf.content.model.ListSitesModel;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;
import co.edu.uniajc.vtf.utils.AlertDialogManager;
import co.edu.uniajc.vtf.utils.ExtendedApplicationContext;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.OptionsEntity;
import co.edu.uniajc.vtf.utils.OptionsManager;
import co.edu.uniajc.vtf.utils.ResourcesManager;
import co.edu.uniajc.vtf.utils.SessionManager;

public class ListSitesController implements ModelListener{

	private IListSites coView;
	private ListSitesModel coModel;
	ProgressDialog coProgressDialog;
	private Location coLastKnownLocation;
	
	public ListSitesController(IListSites pView) {
		this.coView = pView;
		ResourcesManager loResource = new ResourcesManager(((Fragment)this.coView).getActivity());
		String lsBaseUrl = loResource.getStringResource(R.string.general_web_service_base_url);
		this.coModel = new ListSitesModel(lsBaseUrl);
		this.coModel.addModelListener(this);
		this.coProgressDialog = new ProgressDialog(((Fragment)this.coView).getActivity());
		this.coProgressDialog.setMessage(loResource.getStringResource(R.string.general_progress_message));
	}

	public void getSiteListAsync(boolean pForceLoad){
		SessionManager loSession = new SessionManager(((Fragment)this.coView).getActivity());  
		OptionsManager loOptions = new OptionsManager(((Fragment)this.coView).getActivity()); 		
		String lsUserName = loSession.getUserName();		
		OptionsEntity loOptionsData =  loOptions.getOptions();
		
		int liLanguage =loOptionsData.getLanguageId();
		boolean lboMonument = loOptionsData.isFilterMonument();
		boolean lboMuseum = loOptionsData.isFilterMuseum();
		boolean lboBuilding = loOptionsData.isFilterBuilding();
		boolean lboEvent = loOptionsData.isFilterEvent();
		boolean lboHotel = loOptionsData.isFilterHotel();
		boolean lboInterest = loOptionsData.isFilterInterest();
		boolean lboRestaurant = loOptionsData.isFilterRestaurant();
		boolean lboTransport = loOptionsData.isFilterTransport();
		int liCityId = loOptionsData.getCityId();
		int liArea = loOptionsData.getArea();
		String lsSearch = loOptionsData.getSearch();
		
		Location loCurrentLocation = this.coView.getCurrentLocation();
		if(pForceLoad){
			this.coModel.getSiteListAsync(lsUserName, liCityId, lboMonument, lboMuseum, lboHotel, lboRestaurant, lboInterest, lboBuilding, lboTransport, lboEvent, liLanguage, loCurrentLocation.getLatitude(), loCurrentLocation.getLongitude(), liArea, lsSearch);
		}
		else{
			ExtendedApplicationContext loContext = (ExtendedApplicationContext)((Fragment)this.coView).getActivity().getApplication();
			ArrayList<PointOfInterestEntity> loPoints = loContext.getBufferPoints();	
			if(loPoints != null){
				this.coView.setAdapterData(loPoints);		
			}
			else if(loCurrentLocation != null){
				this.coModel.getSiteListAsync(lsUserName, liCityId, lboMonument, lboMuseum, lboHotel, lboRestaurant, lboInterest, lboBuilding, lboTransport, lboEvent, liLanguage, loCurrentLocation.getLatitude(), loCurrentLocation.getLongitude(), liArea, lsSearch);
			}			
		}
		
		
		
		/*if(!pForceLoad){					
			if(this.coLastKnownLocation	== null){
				this.coLastKnownLocation = loCurrentLocation;
				this.coModel.getSiteListAsync(lsUserName, liCityId, lboMonument, lboMuseum, lboHotel, lboRestaurant, lboInterest, lboBuilding, lboTransport, lboEvent, liLanguage, loCurrentLocation.getLatitude(), loCurrentLocation.getLongitude(), liArea, lsSearch);
			}
			else{
				float lfDistance = loCurrentLocation.distanceTo(this.coLastKnownLocation);
				if(lfDistance >= 50f){
					this.coLastKnownLocation = loCurrentLocation;	
					this.coModel.getSiteListAsync(lsUserName, liCityId, lboMonument, lboMuseum, lboHotel, lboRestaurant, lboInterest, lboBuilding, lboTransport, lboEvent, liLanguage, loCurrentLocation.getLatitude(), loCurrentLocation.getLongitude(), liArea, lsSearch);
				}
				else{
					ExtendedApplicationContext loContext = (ExtendedApplicationContext)((Fragment)this.coView).getActivity().getApplication();
					ArrayList<PointOfInterestEntity> loPoints = loContext.getBufferPoints();
					if(loPoints != null){
						this.coView.setAdapterData(loPoints);
					}
					else{
						this.coModel.getSiteListAsync(lsUserName, liCityId, lboMonument, lboMuseum, lboHotel, lboRestaurant, lboInterest, lboBuilding, lboTransport, lboEvent, liLanguage, loCurrentLocation.getLatitude(), loCurrentLocation.getLongitude(), liArea, lsSearch);
					}
				}
			}
		}
		else{
			this.coModel.getSiteListAsync(lsUserName, liCityId, lboMonument, lboMuseum, lboHotel, lboRestaurant, lboInterest, lboBuilding, lboTransport, lboEvent, liLanguage, loCurrentLocation.getLatitude(), loCurrentLocation.getLongitude(), liArea, lsSearch);
		}*/
	
	}
	
	public void getSiteListAsync(){
		this.getSiteListAsync(false);
	}
	
	private void getSiteList(ArrayList<PointOfInterestEntity> pPoints){	
		ExtendedApplicationContext loContext = (ExtendedApplicationContext)((Fragment)this.coView).getActivity().getApplication();
		loContext.setBufferPoints(pPoints);		
		this.coView.setAdapterData(pPoints);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onGetData(Object pData, int type) {
		if(type == 0){
			this.getSiteList((ArrayList<PointOfInterestEntity>)pData);
		}		
	}

	@Override
	public void onError(Object pData, int type) {
		ResourcesManager loResource = new ResourcesManager(((Fragment)this.coView).getActivity());
		AlertDialogManager.showAlertDialog(((Fragment)this.coView).getActivity(), loResource.getStringResource(R.string.general_message_error), loResource.getStringResource(R.string.general_db_error), AlertDialogManager.ERROR);			
		this.coProgressDialog.dismiss();
	}	
	
}
