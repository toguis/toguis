/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.content;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.controller.PoiDetailController;
import co.edu.uniajc.vtf.content.interfaces.IPoiDetail;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;
import co.edu.uniajc.vtf.receivers.NetworkStatusReceiver;
import co.edu.uniajc.vtf.utils.OptionsEntity;
import co.edu.uniajc.vtf.utils.OptionsManager;
import co.edu.uniajc.vtf.utils.ResourcesManager;
import co.edu.uniajc.vtf.utils.SessionManager;

public class PoiDetailActivity extends Activity implements IPoiDetail {

	private PoiDetailController coController; 
	private int ciPoiId;
	private double cdPersonalRating;
	private PointOfInterestEntity coPoint;
	
	@Override
	public double getPersonalRating() {
		return cdPersonalRating;
	}

	@Override
	public void setPersonalRating(double pRating) {
		this.cdPersonalRating = pRating;
	}

	private ProgressDialog coProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_detail);
		this.coController = new PoiDetailController(this);		
		this.ciPoiId = this.getIntent().getIntExtra("id", 0);
		this.loadData();
	}

	@Override
	public void loadData(){
		SessionManager loSession = new SessionManager(this); 
		OptionsManager loOptions = new OptionsManager(this); 	
		OptionsEntity loOptionsData =  loOptions.getOptions();	
		this.coController.getPoiDetailAsync(loSession.getUserName(), this.ciPoiId, loOptionsData.getLanguageId());
		final ActionBar coActionBar = getActionBar();
		coActionBar.setDisplayHomeAsUpEnabled(true);	
		ResourcesManager loResource = new ResourcesManager(this);
		this.coProgressDialog = new ProgressDialog(this);
		this.coProgressDialog.setMessage(loResource.getStringResource(R.string.general_progress_message));
		this.coProgressDialog.setCanceledOnTouchOutside(false);
		this.showProgressDialog();		
	}
	
	@Override
	public void setTitle(String pTitle) {
		TextView loControl = (TextView) this.findViewById(R.id.lblDetailTitle);
		loControl.setText(pTitle);
	}

	@Override
	public void setImage(Bitmap pImage) {
		ImageView loControl = (ImageView) this.findViewById(R.id.imgImagePoi);
		loControl.setImageBitmap(pImage);
	}

	@Override
	public void setRating(double pRating) {
		TextView loControl = (TextView) this.findViewById(R.id.lblRating);
		loControl.setText(String.format("%.2f",pRating));
		RatingBar loControl2 = (RatingBar) this.findViewById(R.id.rtbRating);
		loControl2.setRating((float)pRating);
	}

	@Override
	public void setDescription(String pDescription) {
		TextView loControl = (TextView) this.findViewById(R.id.txtDetailDescription);
		loControl.setText(pDescription);		
	}
	
	@Override
	public void setAddress(String pAddress) {
		TextView loControl = (TextView) this.findViewById(R.id.txtPoiAddress);
		loControl.setText(pAddress);
	}
	
	@Override
	public void setFavorite(boolean pIsFavorite) {
		ToggleButton loControl =  (ToggleButton) this.findViewById(R.id.tglFavorite);
		loControl.setChecked(pIsFavorite);
	}

	@Override
	public boolean isFavorite(){
		ToggleButton loControl =  (ToggleButton) this.findViewById(R.id.tglFavorite);
		return loControl.isChecked();
	}
	
	@Override
	public void setVisited(boolean pWasVisited) {
		ToggleButton loControl =  (ToggleButton) this.findViewById(R.id.tglVisited);
		loControl.setChecked(pWasVisited);		
	}
	
	@Override
	public boolean wasVisited(){
		ToggleButton loControl =  (ToggleButton) this.findViewById(R.id.tglVisited);
		return loControl.isChecked();
	}

	public void onClick_SetFavorite(View view){
		SessionManager loSession = new SessionManager(this); 				
		this.coController.setFavoriteAsync(loSession.getUserName(), this.ciPoiId);
	}
	
	public void onClick_SetVisited(View view){
		SessionManager loSession = new SessionManager(this); 
		this.coController.setVisitedAsync(loSession.getUserName(), this.ciPoiId);
	}
	
	public void onClick_OpenComments(View view){
		this.coController.navigateComments(this.ciPoiId);
	}
	
	public void onClick_SetRating(View view){
		Dialog loDialog = this.createDialog();
		loDialog.setCanceledOnTouchOutside(false);	
		loDialog.show();	
    	RatingBar loRatingBar = (RatingBar)loDialog.findViewById(R.id.rtbRatingPoi);  	
    	TextView loRatingValue = (TextView)loDialog.findViewById(R.id.txtRatingValue);  	
    	loRatingBar.setRating((float)PoiDetailActivity.this.cdPersonalRating);
    	loRatingValue.setText(String.format("%.2f",PoiDetailActivity.this.cdPersonalRating)); 		
	}
	
	public void onClick_OpenMap(View view){
		this.coController.navigateToRouteMap();
	}
	
	public void onCreateRoute(View view){
		this.coController.navigateComments(this.ciPoiId);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.coController.navigateHome(item);
		return super.onOptionsItemSelected(item);
	}	
	
	public void showProgressDialog(){
		this.coProgressDialog.show();
	}

	public void hideProgressDialog(){
		this.coProgressDialog.dismiss();
	}
	
	public Dialog createDialog(){
    	LayoutInflater loInflater = this.getLayoutInflater(); 	
    	
    	AlertDialog.Builder loAlert = new AlertDialog.Builder(this);
    	loAlert.setView(loInflater.inflate(R.layout.dialog_rating, null));
    	loAlert.setPositiveButton(R.string.general_menus_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            	RatingBar loRatingBar = (RatingBar)((AlertDialog)dialog).findViewById(R.id.rtbRatingPoi);
        		SessionManager loSession = new SessionManager(PoiDetailActivity.this);         		
        		dialog.dismiss();
            	PoiDetailActivity.this.coController.setRatingAsync(loSession.getUserName(), PoiDetailActivity.this.ciPoiId, loRatingBar.getRating());             	
           	
            }
        });
    	    	
    	loAlert.setNeutralButton(R.string.general_menus_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {  
            	dialog.dismiss();
            }
        });	
    	loAlert.setCancelable(false);
    	return loAlert.create();		
    }

	@Override
	public void setPoiData(PointOfInterestEntity pPoint) {
		this.coPoint = pPoint;
	}

	@Override
	public PointOfInterestEntity getPoiData(){
		return this.coPoint;
	}
	
	public void onClick_OpenImage(View view){
		this.coController.navigateToImageView(this.coPoint);
	}

	@Override
	protected void onPause() {
		ComponentName loComponent = new ComponentName(this, NetworkStatusReceiver.class);
		this.getPackageManager().setComponentEnabledSetting(loComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED , PackageManager.DONT_KILL_APP);		
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		ComponentName loComponent = new ComponentName(this, NetworkStatusReceiver.class);
		this.getPackageManager().setComponentEnabledSetting(loComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED  , PackageManager.DONT_KILL_APP);	
		super.onResume();
	}
}
