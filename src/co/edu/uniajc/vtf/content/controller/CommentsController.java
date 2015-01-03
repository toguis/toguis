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
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.PoiDetailActivity;
import co.edu.uniajc.vtf.content.interfaces.IComments;
import co.edu.uniajc.vtf.content.model.CommentEntity;
import co.edu.uniajc.vtf.content.model.CommentsModel;
import co.edu.uniajc.vtf.utils.AlertDialogManager;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.ResourcesManager;

public class CommentsController implements ModelListener{
	private IComments coView;
	private CommentsModel coModel;
	
	public CommentsController(IComments pView) {
		this.coView = pView;
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		String lsBaseUrl = loResource.getStringResource(R.string.general_web_service_base_url);
		this.coModel = new CommentsModel(lsBaseUrl, (Activity)this.coView) ;
		this.coModel.addModelListener(this);
	}

	public void getCommentsAsync(int pPoiId){
		this.coModel.getCommentsAsync(pPoiId);
	}
	
	private void getComments(ArrayList<CommentEntity> pData){
		this.coView.setAdapterData(pData);
	}
	
	public void setCommentAsync(CommentEntity pComment){
		this.coModel.setCommentAsync(pComment);
	}
	
	private void setComment(int pData){
		if(pData == 0){
			this.coView.blankFields();
			this.coView.loadData();
		}
	}
	
	public void navigateHome(MenuItem item){
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent loUpIntent = new Intent((Activity)this.coView, PoiDetailActivity.class);
				loUpIntent.putExtra("id", this.coView.getPoiId());
				NavUtils.navigateUpTo((Activity)this.coView, loUpIntent);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onGetData(Object pData, int type) {
		if(type == 0){
			this.getComments((ArrayList<CommentEntity>)pData);
		}	
		else if(type == 1){
			this.setComment((Integer)pData);
		}
	}

	@Override
	public void onError(Object pData, int type) {
		ResourcesManager loResource = new ResourcesManager((Activity)this.coView);
		AlertDialogManager.showAlertDialog((Activity)this.coView, loResource.getStringResource(R.string.general_message_error), loResource.getStringResource(R.string.general_db_error), AlertDialogManager.ERROR);			
		this.coView.hideProgressDialog();
	}	
}
