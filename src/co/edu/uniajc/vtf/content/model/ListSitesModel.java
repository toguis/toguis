package co.edu.uniajc.vtf.content.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.edu.uniajc.vtf.utils.BaseModel;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.RestAsyncTask;

public class ListSitesModel extends BaseModel {

	public ListSitesModel(String pBaseUrl) {
		super.csBaseUrl = pBaseUrl;
		super.csMethod = "";
		super.coModelListener = new ArrayList<ModelListener>();		
	}
	
	public void getSiteListAsync(String pUserName, 
								 int pCityId,  
								 boolean pGetMonument, 
								 boolean pGetMuseum, 
								 boolean pGetHotel, 
								 boolean pGetRestaurant, 
								 boolean pGetInterest, 
								 boolean pGetBuilding, 
								 boolean pGetTransport, 
								 boolean pGetEvent, 
								 int pLanguage,
								 double pLatitude,
								 double pLongitude,
								 int pMaxDistance,
								 String pSearch)
	{
		
		String lsQueryUrl = super.csBaseUrl + "ToguisPoints.svc/" + (pSearch.equals("") ? "get_pointsDistance?" : "search_points?");
		StringBuilder lsParams = new StringBuilder();
		lsParams.append("login=").append(pUserName).append("&");
		lsParams.append("cityid=").append(pCityId).append("&");
		lsParams.append("getmonument=").append(pGetMonument).append("&");
		lsParams.append("getmuseum=").append(pGetMuseum).append("&");	
		lsParams.append("gethotel=").append(pGetHotel).append("&");
		lsParams.append("getrestaurant=").append(pGetRestaurant).append("&");
		lsParams.append("getinterest=").append(pGetInterest).append("&");
		lsParams.append("getbuilding=").append(pGetBuilding).append("&");
		lsParams.append("gettransport=").append(pGetTransport).append("&");
		lsParams.append("getevent=").append(pGetEvent).append("&");
		lsParams.append("language=").append(pLanguage).append("&");
		lsParams.append("userlatitude=").append(pLatitude).append("&");
		lsParams.append("userlongitude=").append(pLongitude).append("&");
		lsParams.append("maxdistance=").append(pMaxDistance);
		if(!pSearch.equals("")){
			lsParams.append("&search=").append(pSearch);
		}
		lsQueryUrl += lsParams.toString();
		super.csMethod = "getSiteListAsync";
		RestAsyncTask loTask = new RestAsyncTask();
		loTask.addAsyncTaskListener(this);	
		loTask.execute("0", lsQueryUrl);
	}
	
	
	private void getSiteList(String pData){
		ArrayList<PointOfInterestEntity> loPoints = new ArrayList<PointOfInterestEntity>();
		if(!pData.equals("")){
			try {
				JSONArray loArrayData = new JSONArray(pData);
		        for (int i = 0; i < loArrayData.length(); i++) {
		            JSONObject loPointData = loArrayData.getJSONObject(i);
		            PointOfInterestEntity loPoint = new PointOfInterestEntity();
		            loPoint.setId(loPointData.getInt("POI_ID"));
		            loPoint.setAddress(loPointData.getString("POI_ADDRESS"));
		            loPoint.setLatitude(loPointData.getDouble("POI_LATITUDE"));
		            loPoint.setLongitude(loPointData.getDouble("POI_LONGITUDE"));
		            loPoint.setAltitude(loPointData.getDouble("POI_ALTITUDE"));
		            loPoint.setImage(loPointData.getString("POI_IMAGE_PATH"));
		            loPoint.setSiteType(loPointData.getInt("PTYP_ID"));
		            loPoint.setAvgRating(loPointData.getDouble("RATING"));
		            
		            JSONArray loPoiDataDescArray = loPointData.getJSONArray("TG_POI_DESCRIPTION");		            
		            if(loPoiDataDescArray.length() > 0){
			            JSONObject loPoiDataDesc =  loPoiDataDescArray.getJSONObject(0);
			            loPoint.setTitle(loPoiDataDesc.getString("POID_NAME"));
			            loPoint.setDescription(loPoiDataDesc.getString("POID_DESCRIPTION"));		            	
		            }
		            else{
		            	loPoint.setTitle("");
		            	loPoint.setDescription("");
		            }

		            JSONArray loPoiDataUserArray = loPointData.getJSONArray("TG_POI_USER_DATA");		            
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

		            loPoints.add(loPoint);
		        }
				for (ModelListener item : this.coModelListener){
					item.onGetData(loPoints, 0);
				}					
			} catch (JSONException ex) {
				for (ModelListener item : this.coModelListener){
					item.onError(ex.getMessage(), 0);
				}	
			}
			
		}
	}
	
	
	@Override
	public void onQuerySuccessful(String result) {
		if(super.csMethod.equals("getSiteListAsync")){
			this.getSiteList(result);
		}
		
	}

	@Override
	public void onQueryError(String error) {
		for (ModelListener item : this.coModelListener){
			item.onError(error, 0);
		}	
	}	
	
}
