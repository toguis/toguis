package co.edu.uniajc.vtf.content;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.controller.PoiDetailController;
import co.edu.uniajc.vtf.content.interfaces.IPoiDetail;
import co.edu.uniajc.vtf.utils.OptionsEntity;
import co.edu.uniajc.vtf.utils.OptionsManager;
import co.edu.uniajc.vtf.utils.SessionManager;

public class PoiDetailActivity extends Activity implements IPoiDetail {

	private PoiDetailController coController; 
	private int ciPoiId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_detail);
		this.coController = new PoiDetailController(this);	
		SessionManager loSession = new SessionManager(this); 
		OptionsManager loOptions = new OptionsManager(this); 	
		OptionsEntity loOptionsData =  loOptions.getOptions();		
		this.ciPoiId = this.getIntent().getIntExtra("id", 0);
		this.coController.getPoiDetailAsync(loSession.getUserName(), this.ciPoiId, loOptionsData.getLanguageId());
		final ActionBar coActionBar = getActionBar();
		coActionBar.setDisplayHomeAsUpEnabled(true);		
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
		OptionsManager loOptions = new OptionsManager(this); 	
		OptionsEntity loOptionsData =  loOptions.getOptions();					
		this.coController.setFavoriteAsync(loSession.getUserName(), this.ciPoiId);
	}
	
	public void onClick_SetVisited(View view){
		SessionManager loSession = new SessionManager(this); 
		OptionsManager loOptions = new OptionsManager(this); 	
		OptionsEntity loOptionsData =  loOptions.getOptions();	
		this.coController.setVisitedAsync(loSession.getUserName(), this.ciPoiId);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.coController.navigateHome(item);
		return super.onOptionsItemSelected(item);
	}	
}
