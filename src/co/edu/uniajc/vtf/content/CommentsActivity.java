/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.content;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.ListSitesFragment.ListPointsAdapter;
import co.edu.uniajc.vtf.content.controller.CommentsController;
import co.edu.uniajc.vtf.content.interfaces.IComments;
import co.edu.uniajc.vtf.content.model.CommentEntity;
import co.edu.uniajc.vtf.receivers.NetworkStatusReceiver;
import co.edu.uniajc.vtf.utils.ResourcesManager;
import co.edu.uniajc.vtf.utils.SessionManager;

public class CommentsActivity extends Activity implements IComments{
	private ProgressDialog coProgressDialog;
	private CommentsController coController; 
	private int ciPoiId;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		this.coController = new CommentsController(this);
		this.ciPoiId = this.getIntent().getIntExtra("id", 0);
		final ActionBar coActionBar = getActionBar();
		coActionBar.setDisplayHomeAsUpEnabled(true);	
		this.loadData();
	}
	
	@Override
	public void loadData(){
		ResourcesManager loResource = new ResourcesManager(this);
		this.coProgressDialog = new ProgressDialog(this);
		this.coProgressDialog.setMessage(loResource.getStringResource(R.string.general_progress_message_loading));
		this.coProgressDialog.setCanceledOnTouchOutside(false);
		this.showProgressDialog();
		this.coController.getCommentsAsync(this.ciPoiId);
	}
	
    public static class ListCommentsAdapter extends BaseAdapter{
    	private Context coContext;
    	private ArrayList<CommentEntity> coData; 
		public ListCommentsAdapter(Context pContext, ArrayList<CommentEntity> pData) {
			this.coContext = pContext;
			this.coData = pData;
		}

		@Override
		public int getCount() {
			return coData.size();
		}

		@Override
		public Object getItem(int position) {
			return this.coData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View loView = null;
			if(convertView == null){
				LayoutInflater loInflater = (LayoutInflater)this.coContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				loView = loInflater.inflate(R.layout.list_comments, null);				
			}
			else{
				loView = convertView;
			}
			
			CommentEntity loCommentData = this.coData.get(position);
			TextView loName = (TextView)loView.findViewById(R.id.txtCommentName);
			loName.setText(loCommentData.getName());
			
			TextView loComment = (TextView)loView.findViewById(R.id.txtComment);
			loComment.setText(loCommentData.getComment());
			
			TextView loDate = (TextView)loView.findViewById(R.id.txtCommentDate);
			loDate.setText(loCommentData.getDate());
			
			return loView;
		}	
    }	
    
    @Override
	public void setAdapter(ListCommentsAdapter pAdapter) {
		try{
	    	ListView loList = (ListView)this.findViewById(R.id.lstComments);
	    	loList.setAdapter(pAdapter);			
		}
		catch(Exception ex){
	
		}
	}

	@Override
	public ListPointsAdapter getAdapter() {
    	ListView loList = (ListView)this.findViewById(R.id.lstComments);
		return (ListPointsAdapter)loList.getAdapter();
	}
	
	@Override
	public void setAdapterData(ArrayList<CommentEntity> pComments) {		
		this.hideProgressDialog();
		ListCommentsAdapter loAdapter = new ListCommentsAdapter(this, pComments);
    	this.setAdapter(loAdapter);		
	}

	
	public void showProgressDialog(){
		this.coProgressDialog.show();
	}

	@Override
	public void hideProgressDialog(){
		this.coProgressDialog.dismiss();
	}

	public void onClick_SetComment(View view){
		SessionManager loSession = new SessionManager(this);   		
		String lsUserName = loSession.getUserName();	
		EditText loInsertComment = (EditText)this.findViewById(R.id.txtInsertComment);		
		if(!loInsertComment.getText().toString().equals("")){
			String lsComment = loInsertComment.getText().toString().replace("\n", "\\n");
			CommentEntity loComment = new CommentEntity();
			loComment.setComment(lsComment);
			loComment.setUserName(lsUserName);
			loComment.setPoiId(this.ciPoiId);
			this.coController.setCommentAsync(loComment);			
		}
		else{
	      	ResourcesManager loResource = new ResourcesManager(this); 
	        Toast.makeText(this.getApplicationContext(), loResource.getStringResource(R.string.comments_message_empty), Toast.LENGTH_LONG).show();   			
		}
	}
	
	public void blankFields(){
		EditText loInsertComment = (EditText)this.findViewById(R.id.txtInsertComment);		
		loInsertComment.setText("");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.coController.navigateHome(item);
		return super.onOptionsItemSelected(item);
	}	
	
	@Override
	public int getPoiId(){
		return  this.ciPoiId;
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
