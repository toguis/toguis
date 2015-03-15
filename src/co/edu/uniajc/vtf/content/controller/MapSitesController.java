/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.content.controller;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.Fragment;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.PoiDetailActivity;
import co.edu.uniajc.vtf.content.interfaces.IMapSites;
import co.edu.uniajc.vtf.content.model.MapSitesModel;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;
import co.edu.uniajc.vtf.utils.AlertDialogManager;
import co.edu.uniajc.vtf.utils.ExtendedApplicationContext;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.OptionsEntity;
import co.edu.uniajc.vtf.utils.OptionsManager;
import co.edu.uniajc.vtf.utils.ResourcesManager;
import co.edu.uniajc.vtf.utils.SessionManager;

public class MapSitesController implements ModelListener{

	private IMapSites coView;
	private MapSitesModel coModel;
	
	public MapSitesController(IMapSites pView){
		this.coView = pView;
		ResourcesManager loResource = new ResourcesManager(((Fragment)this.coView).getActivity());
		String lsBaseUrl = loResource.getStringResource(R.string.general_web_service_base_url);
		this.coModel = new MapSitesModel(lsBaseUrl,((Fragment)this.coView).getActivity());
		this.coModel.addModelListener(this);		
	}
	
	//get all sites from web service
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
			if(loCurrentLocation != null){
				this.coModel.getSiteListAsync(lsUserName, liCityId, lboMonument, lboMuseum, lboHotel, lboRestaurant, lboInterest, lboBuilding, lboTransport, lboEvent, liLanguage, loCurrentLocation.getLatitude(), loCurrentLocation.getLongitude(), liArea, lsSearch);	
			}				
		}
		else {
			ExtendedApplicationContext loContext = (ExtendedApplicationContext)((Fragment)this.coView).getActivity().getApplication();
			ArrayList<PointOfInterestEntity> loPoints = loContext.getBufferPoints();	
			if(loPoints != null){
				this.coView.setAdapterData(loPoints);		
			}
			else if(loCurrentLocation != null){
				this.coModel.getSiteListAsync(lsUserName, liCityId, lboMonument, lboMuseum, lboHotel, lboRestaurant, lboInterest, lboBuilding, lboTransport, lboEvent, liLanguage, loCurrentLocation.getLatitude(), loCurrentLocation.getLongitude(), liArea, lsSearch);
			}			
		}				
	}
	
	public void getSiteListAsync(){
		this.getSiteListAsync(false);
	}
	
	private void getSiteList(ArrayList<PointOfInterestEntity> pData){	
		ExtendedApplicationContext loContext = (ExtendedApplicationContext)((Fragment)this.coView).getActivity().getApplication();
		loContext.setBufferPoints(pData);		
		this.coView.setAdapterData(pData);
	}
	
	public void navigatePoiDetail(int pPoiId){
		Activity loActivity = ((Fragment)this.coView).getActivity();
		Intent loIntent = new Intent(loActivity, PoiDetailActivity.class);
		loIntent.putExtra("id", pPoiId);		
		loActivity.startActivity(loIntent);			
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
		this.coView.hideProgressDialog();
	}	

}
