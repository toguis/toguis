package co.edu.uniajc.vtf.content.controller;

import java.util.ArrayList;

import android.app.Activity;
import co.edu.uniajc.vtf.R;
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
		this.coModel = new CommentsModel(lsBaseUrl);
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
