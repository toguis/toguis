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
import java.util.Locale;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.widget.Toast;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.interfaces.ISettings;
import co.edu.uniajc.vtf.content.model.CityEntity;
import co.edu.uniajc.vtf.content.model.LanguageEntity;
import co.edu.uniajc.vtf.content.model.SettingsModel;
import co.edu.uniajc.vtf.utils.AlertDialogManager;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.OptionsEntity;
import co.edu.uniajc.vtf.utils.OptionsManager;
import co.edu.uniajc.vtf.utils.ResourcesManager;

public class SettingsController implements ModelListener{
	private ISettings coView;
	private SettingsModel coModel;
	Handler coHandler = new Handler(); 
	
	public SettingsController(ISettings pView) {
		this.coView = pView;
		ResourcesManager loResource = new ResourcesManager(((Fragment)this.coView).getActivity());
		String lsBaseUrl = loResource.getStringResource(R.string.general_web_service_base_url);
		this.coModel = new SettingsModel(lsBaseUrl, ((Fragment)this.coView).getActivity());
		this.coModel.addModelListener(this);
		this.coHandler = new Handler(); 
	}

	public void getLanguagesAsync(){
		this.coModel.getLanguagesAsync();
	}
	
	private void getLanguages(ArrayList<LanguageEntity> pData){
		this.coView.setLanguages(pData);
	}
	
	public void getCitiesAsync(){
		this.coModel.getCitiesAsync();
	}
	
	private void getCities(ArrayList<CityEntity> pData){
		this.coView.setCities(pData);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onGetData(Object pData, int type) {
		if(type == 0){
			this.getLanguages((ArrayList<LanguageEntity>)pData);
		}		
		else if(type == 1){
			this.getCities((ArrayList<CityEntity>)pData);
		}	
	}

	@Override
	public void onError(Object pData, int type) {
		ResourcesManager loResource = new ResourcesManager(((Fragment)this.coView).getActivity());
		AlertDialogManager.showAlertDialog(((Fragment)this.coView).getActivity(), loResource.getStringResource(R.string.general_message_error), loResource.getStringResource(R.string.general_db_error), AlertDialogManager.ERROR);			
	}
	
	public void saveSettings(){
       	OptionsManager loOptions = new OptionsManager(((Fragment)this.coView).getActivity()); 
    	OptionsEntity loOptionsData = loOptions.getOptions();	
    	loOptionsData.setFilterMonument(this.coView.getMonumentValue());
    	loOptionsData.setFilterMuseum(this.coView.getMuseumValue());
    	loOptionsData.setFilterHotel(this.coView.getHotelValue());
    	loOptionsData.setFilterRestaurant(this.coView.getRestaurantValue());
    	loOptionsData.setFilterInterest(this.coView.getInterestValue());
    	loOptionsData.setFilterTransport(this.coView.getTransportValue());
    	loOptionsData.setFilterBuilding(this.coView.getBuildingValue());
    	loOptionsData.setFilterEvent(this.coView.getEventValue());
    	loOptionsData.setLanguageId(this.coView.getLanguageValue());
    	loOptionsData.setLanguageIsoId(this.coView.getLanguageISOValue());
    	loOptionsData.setArea(this.coView.getDistanceValue());
    	loOptionsData.setCityId(this.coView.getCityValue());
    	loOptions.createOrUpdateOptions(loOptionsData);
    	this.setLocal(this.coView.getLanguageISOValue());
    	ResourcesManager loResource = new ResourcesManager(((Fragment)this.coView).getActivity());
    	Toast.makeText(((Fragment)this.coView).getActivity().getApplicationContext(), loResource.getStringResource(R.string.general_setting_save_data), Toast.LENGTH_SHORT).show();
	}
	
	public void loadSettings(){
	    coHandler.postDelayed(
			new Runnable() { 
		         public void run() { 
		        	 loadSettings(SettingsController.this.coView);
		         } 
		    }, 500);
	}
	
	private void setLocal(String pISOLang) {
		Locale loLocale = new Locale(pISOLang);
		Resources loResource = ((Fragment)this.coView).getResources();
		DisplayMetrics loDm = loResource.getDisplayMetrics();
		Configuration loConfig = loResource.getConfiguration();
		loConfig.locale = loLocale;
		loResource.updateConfiguration(loConfig, loDm);        
	}
	
	private void loadSettings(ISettings pView){
		try{
	       	OptionsManager loOptions = new OptionsManager(((Fragment)this.coView).getActivity()); 
	    	OptionsEntity loOptionsData = loOptions.getOptions();	
	    	this.coView.setMonumentValue(loOptionsData.isFilterMonument());
	    	this.coView.setMuseumValue(loOptionsData.isFilterMuseum());
	    	this.coView.setHotelValue(loOptionsData.isFilterHotel());
	    	this.coView.setRestaurantValue(loOptionsData.isFilterRestaurant());
	    	this.coView.setInterestValue(loOptionsData.isFilterInterest());
	    	this.coView.setTransportValue(loOptionsData.isFilterTransport());
	    	this.coView.setBuildingValue(loOptionsData.isFilterBuilding());
	    	this.coView.setEventValue(loOptionsData.isFilterEvent());
	    	this.coView.setDistanceValue(loOptionsData.getArea());
	    	this.coView.setCityValue(loOptionsData.getCityId());
	    	this.coView.setLanguageValue(loOptionsData.getLanguageId());			
		}
		catch(Exception ex){
			
		}
		
	}
}
