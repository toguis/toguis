/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.content.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import co.edu.uniajc.vtf.utils.BaseModel;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.RestAsyncTask;

public class PoiDetailModel extends BaseModel {

	public PoiDetailModel(String pBaseUrl, Context pContext) {
		super.csBaseUrl = pBaseUrl;
		super.csMethod = "";
		super.coModelListener = new ArrayList<ModelListener>();
		super.coContext = pContext;
	}	
	
	public void getPoiDetailAsync(String pUserName, int pPoiId, int pLanguageId ){
		String lsQueryUrl = super.csBaseUrl + "ToguisPoints.svc/get_point?";
		StringBuilder lsParams = new StringBuilder();
		lsParams.append("login=").append(pUserName).append("&");
		lsParams.append("poiid=").append(pPoiId).append("&");
		lsParams.append("language=").append(pLanguageId);	
		lsQueryUrl += lsParams.toString();
		super.csMethod = "getPoiDetailAsync";
		RestAsyncTask loTask = new RestAsyncTask(super.coContext);
		loTask.addAsyncTaskListener(this);	
		loTask.execute("0", lsQueryUrl);
	}
	
	private void getPoiDetail(String pData){
		if(!pData.equals("")){
			try {
				JSONObject loJsonObj = new JSONObject(pData);
				
				PointOfInterestEntity loPoint = new PointOfInterestEntity();
				
	            loPoint.setId(loJsonObj.getInt("POI_ID"));
	            loPoint.setAddress(loJsonObj.getString("POI_ADDRESS"));
	            loPoint.setLatitude(loJsonObj.getDouble("POI_LATITUDE"));
	            loPoint.setLongitude(loJsonObj.getDouble("POI_LONGITUDE"));
	            loPoint.setAltitude(loJsonObj.getDouble("POI_ALTITUDE"));
	            loPoint.setImage(loJsonObj.getString("POI_IMAGE_PATH"));
	            loPoint.setSiteType(loJsonObj.getInt("PTYP_ID"));
	            loPoint.setAvgRating(loJsonObj.getDouble("RATING"));
	            
	            JSONArray loPoiDataDescArray = loJsonObj.getJSONArray("TG_POI_DESCRIPTION");	
	            if(loPoiDataDescArray.length() > 0){
		            JSONObject loPoiDataDesc =  loPoiDataDescArray.getJSONObject(0);
		            loPoint.setTitle(loPoiDataDesc.getString("POID_NAME"));
		            loPoint.setDescription(loPoiDataDesc.getString("POID_DESCRIPTION"));		            	
	            }
	            else{
	            	loPoint.setTitle("");
	            	loPoint.setDescription("");
	            }
	            
	            JSONArray loPoiDataUserArray = loJsonObj.getJSONArray("TG_POI_USER_DATA");		            
	            if(loPoiDataUserArray.length() > 0){
		            JSONObject loPoiDataUser =  loPoiDataUserArray.getJSONObject(0);
		            loPoint.setFavorite(loPoiDataUser.getBoolean("UDAT_FAVORITE"));
		            loPoint.setVisited(loPoiDataUser.getBoolean("UDAT_VISITED"));
		            loPoint.setRating(loPoiDataUser.getDouble("UDAT_RATING"));		            	
	            }
	            else{
	            	loPoint.setFavorite(false);
	            	loPoint.setVisited(false);
	            	loPoint.setRating(0);
	            }
	            
				for (ModelListener item : this.coModelListener){
					item.onGetData(loPoint, 0);
				}	
			} catch (JSONException ex) {
				for (ModelListener item : this.coModelListener){
					item.onError(ex.getMessage(), 0);
				}	
			}
		}
	}
	
	public void setFavoriteAsync(String pUserName, int pPoiId, boolean pValue){
		String lsQueryUrl = super.csBaseUrl + "ToguisPoints.svc/set_favorite/";
		StringBuilder lsParams = new StringBuilder();
		lsParams.append(pUserName).append("/");
		lsParams.append(pPoiId).append("/");		
		lsParams.append(pValue);	
		super.csMethod = "setFavoriteAsync";
		RestAsyncTask loTask = new RestAsyncTask(super.coContext);
		loTask.addAsyncTaskListener(this);		
		loTask.execute("1",lsQueryUrl +  lsParams.toString(),"");
	}
	
	private void setFavorite(int pData){
		try {
			for (ModelListener item : this.coModelListener){
				item.onGetData(pData, 1);
			}				
		}
		catch(Exception ex){
			for (ModelListener item : this.coModelListener){
				item.onError(ex.getMessage(), 0);
			}			
		}
	}
	
	public void setVisitedAsync(String pUserName, int pPoiId, boolean pValue){
		String lsQueryUrl = super.csBaseUrl + "ToguisPoints.svc/set_visited/";
		StringBuilder lsParams = new StringBuilder();
		lsParams.append(pUserName).append("/");
		lsParams.append(pPoiId).append("/");		
		lsParams.append(pValue);	
		super.csMethod = "setFavoriteAsync";
		RestAsyncTask loTask = new RestAsyncTask(super.coContext);
		loTask.addAsyncTaskListener(this);		
		loTask.execute("1",lsQueryUrl +  lsParams.toString(),"");
	}
	
	private void setVisited(int pData){
		try {
			for (ModelListener item : this.coModelListener){
				item.onGetData(pData, 2);
			}				
		}
		catch(Exception ex){
			for (ModelListener item : this.coModelListener){
				item.onError(ex.getMessage(), 0);
			}			
		}
	}
	
	public void setRatingAsync(String pUserName, int pPoiId, double pRating){
		String lsQueryUrl = super.csBaseUrl + "ToguisPoints.svc/set_rating/";
		StringBuilder lsParams = new StringBuilder();
		lsParams.append(pUserName).append("/");
		lsParams.append(pPoiId).append("/");		
		lsParams.append(pRating);	
		super.csMethod = "setRatingAsync";
		RestAsyncTask loTask = new RestAsyncTask(super.coContext);
		loTask.addAsyncTaskListener(this);		
		loTask.execute("1",lsQueryUrl +  lsParams.toString(),"");		
	}
	
	private void setRating(int pData){
		try {
			for (ModelListener item : this.coModelListener){
				item.onGetData(pData, 3);
			}				
		}
		catch(Exception ex){
			for (ModelListener item : this.coModelListener){
				item.onError(ex.getMessage(), 0);
			}			
		}		
	}
	
	@Override
	public void onQuerySuccessful(String result) {
		if(super.csMethod.equals("getPoiDetailAsync")){
			this.getPoiDetail(result);
		}
		else if(super.csMethod.equals("setFavoriteAsync")){
			this.setFavorite(Integer.parseInt(result));
		}
		else if(super.csMethod.equals("setVisitedAsync")){
			this.setVisited(Integer.parseInt(result));
		}		
		else if(super.csMethod.equals("setRatingAsync")){
			this.setRating(Integer.parseInt(result));
		}			
	}
	
	@Override
	public void onQueryError(String error) {
		for (ModelListener item : this.coModelListener){
			item.onError(error, 0);
		}	
	}

}
