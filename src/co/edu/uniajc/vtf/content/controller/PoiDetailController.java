/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.content.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.CommentsActivity;
import co.edu.uniajc.vtf.content.NavigationActivity;
import co.edu.uniajc.vtf.content.PoiImageActivity;
import co.edu.uniajc.vtf.content.SwipeContentActivity;
import co.edu.uniajc.vtf.content.interfaces.IPoiDetail;
import co.edu.uniajc.vtf.content.model.PoiDetailModel;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;
import co.edu.uniajc.vtf.utils.AlertDialogManager;
import co.edu.uniajc.vtf.utils.ExtendedApplicationContext;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.ResourcesManager;

public class PoiDetailController implements ModelListener{
	private IPoiDetail coView;
	private PoiDetailModel coModel;
	private double cdRating;
	
	public PoiDetailController(IPoiDetail pView) {
		this.coView = pView;
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		String lsBaseUrl = loResource.getStringResource(R.string.general_web_service_base_url);
		this.coModel = new PoiDetailModel(lsBaseUrl, (Activity)this.coView);
		this.coModel.addModelListener(this);	
	}

	public void getPoiDetailAsync(String pUserName, int pPoiId, int pLanguageId){
		this.coModel.getPoiDetailAsync(pUserName, pPoiId, pLanguageId);
	}
	
	private void getPoiDetail(PointOfInterestEntity pData){
		this.coView.setTitle(pData.getTitle());
		this.coView.setDescription(pData.getDescription());
		this.coView.setRating(pData.getAvgRating());			
		this.coView.setImage(pData.getBitmapImage());
		this.coView.setFavorite(pData.isFavorite());
		this.coView.setVisited(pData.isVisited());
		this.coView.setPersonalRating(pData.getRating());
		this.coView.setAddress(pData.getAddress());
		this.coView.setPoiData(pData);
	}
	
	public void setFavoriteAsync(String pUserName, int pPoiId){
		this.coModel.setFavoriteAsync(pUserName, pPoiId, this.coView.isFavorite());		
	}
	
	private void setFavorite(int pData){
		if(pData != 0){
			this.coView.setFavorite(!this.coView.isFavorite());
		}
	}
	
	public void setVisitedAsync(String pUserName, int pPoiId){
		this.coModel.setVisitedAsync(pUserName, pPoiId, this.coView.wasVisited());		
	}
	
	private void setVisited(int pData){
		if(pData != 0){
			this.coView.setVisited(!this.coView.wasVisited());
		}
	}
	
	public void setRatingAsync(String pUserName, int pPoiId, double pRating){
		this.cdRating = pRating;
		this.coModel.setRatingAsync(pUserName, pPoiId, pRating);	
		
	}
	
	private void setRating(int pData){
		if(pData == 0){
			this.coView.setPersonalRating(this.cdRating);
			this.coView.loadData();
		}
	}
	
	public void navigateHome(MenuItem item){
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent loUpIntent = new Intent((Activity)this.coView, SwipeContentActivity.class);
				NavUtils.navigateUpTo((Activity)this.coView, loUpIntent);
		}
	}
	
	public void navigateComments(int pPoiId){
		Activity loActivity = ((Activity)this.coView);
		Intent loIntent = new Intent(loActivity, CommentsActivity.class);
		loIntent.putExtra("id", pPoiId);		
		loActivity.startActivity(loIntent);		
	}
	
	public void navigateToRouteMap(){
		Activity loActivity = ((Activity)this.coView);
		Intent loIntent = new Intent(loActivity, NavigationActivity.class);
		Bundle loBundle = new Bundle();	
		loBundle.putParcelable("destiny", this.coView.getPoiData());
		loIntent.putExtras(loBundle);
		loActivity.startActivity(loIntent);		
	}
	
	public void navigateToImageView(PointOfInterestEntity pData){
		ExtendedApplicationContext loContext = (ExtendedApplicationContext)((Activity)this.coView).getApplication();
		loContext.setData(pData.getImage());
		Activity loActivity = ((Activity)this.coView);
		Intent loIntent = new Intent(loActivity, PoiImageActivity.class);
		loActivity.startActivity(loIntent);	
	}
	
	@Override
	public void onGetData(Object pData, int type) {
		if(type == 0){
			this.getPoiDetail((PointOfInterestEntity) pData);
		}		
		else if(type == 1){
			this.setFavorite((Integer) pData);
		}	
		else if(type == 2){
			this.setVisited((Integer) pData);
		}	
		else if(type == 3){
			this.setRating((Integer) pData);
		}	
		this.coView.hideProgressDialog();
	}

	@Override
	public void onError(Object pData, int type) {
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		AlertDialogManager.showAlertDialog((Activity)this.coView, loResource.getStringResource(R.string.general_message_error), loResource.getStringResource(R.string.general_db_error), AlertDialogManager.ERROR);			
		this.coView.setFavorite(!this.coView.isFavorite());
		this.coView.setVisited(!this.coView.wasVisited());
		this.coView.hideProgressDialog();
	}	
}
