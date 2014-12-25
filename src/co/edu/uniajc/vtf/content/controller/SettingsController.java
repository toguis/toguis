package co.edu.uniajc.vtf.content.controller;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.interfaces.CityEntity;
import co.edu.uniajc.vtf.content.interfaces.ISettings;
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
	
	public SettingsController(ISettings pView) {
		this.coView = pView;
		ResourcesManager loResource = new ResourcesManager(((Fragment)this.coView).getActivity());
		String lsBaseUrl = loResource.getStringResource(R.string.general_web_service_base_url);
		this.coModel = new SettingsModel(lsBaseUrl);
		this.coModel.addModelListener(this);
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
	
	public void SaveSettings(){
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
    	
	}
}
