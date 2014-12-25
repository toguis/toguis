package co.edu.uniajc.vtf.content.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.edu.uniajc.vtf.content.interfaces.CityEntity;
import co.edu.uniajc.vtf.utils.BaseModel;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.RestAsyncTask;

public class SettingsModel extends BaseModel {

	public SettingsModel(String pBaseUrl) {
		super.csBaseUrl = pBaseUrl;
		super.csMethod = "";
		super.coModelListener = new ArrayList<ModelListener>();		
	}	
	
	public void getLanguagesAsync(){
		String lsQueryUrl = super.csBaseUrl + "ToguisPoints.svc/get_languages";
		super.csMethod = "getLanguagesAsync";
		RestAsyncTask loTask = new RestAsyncTask();
		loTask.addAsyncTaskListener(this);	
		loTask.execute("0", lsQueryUrl);
	}
	
	private void getLanguages(String pData){
		ArrayList<LanguageEntity> loItems = new ArrayList<LanguageEntity>();
		if(!pData.equals("")){
			try{
				
				JSONArray loArrayData = new JSONArray(pData);
				
				for (int i = 0; i < loArrayData.length(); i++) {
					LanguageEntity loItem = new LanguageEntity();
					JSONObject loPointData = loArrayData.getJSONObject(i);
					loItem.setId(loPointData.getInt("LNG_ID"));
					loItem.setName(loPointData.getString("LNG_NAME"));
					loItem.setISOCode(loPointData.getString("LNG_ISOCODE"));
					loItems.add(loItem);
				}
	
				for (ModelListener item : this.coModelListener){
					item.onGetData(loItems, 0);
				}				
			}
			catch(JSONException ex){
				for (ModelListener item : this.coModelListener){
					item.onError(ex.getMessage(), 0);
				}					
			}
		}
	}
	
	public void getCitiesAsync(){
		String lsQueryUrl = super.csBaseUrl + "ToguisPoints.svc/get_cities";
		super.csMethod = "getCitiesAsync";
		RestAsyncTask loTask = new RestAsyncTask();
		loTask.addAsyncTaskListener(this);	
		loTask.execute("0", lsQueryUrl);
	}
	
	private void getCities(String pData){
		ArrayList<CityEntity> loItems = new ArrayList<CityEntity>();
		if(!pData.equals("")){
			try{
				
				JSONArray loArrayData = new JSONArray(pData);
				
				for (int i = 0; i < loArrayData.length(); i++) {
					CityEntity loItem = new CityEntity();
					JSONObject loPointData = loArrayData.getJSONObject(i);
					loItem.setId(loPointData.getInt("CITY_ID"));
					loItem.setName(loPointData.getString("CITY_NAME"));
					loItem.setWOEId(loPointData.getInt("CITY_WOEID"));
					loItems.add(loItem);
				}
	
				for (ModelListener item : this.coModelListener){
					item.onGetData(loItems, 1);
				}				
			}
			catch(JSONException ex){
				for (ModelListener item : this.coModelListener){
					item.onError(ex.getMessage(), 0);
				}					
			}
		}
	}
	
	@Override
	public void onQuerySuccessful(String result) {
		if(super.csMethod.equals("getLanguagesAsync")){
			this.getLanguages(result);
		}
		else if(super.csMethod.equals("getCitiesAsync")){
			this.getCities(result);
		}
				
	}

	@Override
	public void onQueryError(String error) {
		for (ModelListener item : this.coModelListener){
			item.onError(error, 0);
		}	
	}
	


}
