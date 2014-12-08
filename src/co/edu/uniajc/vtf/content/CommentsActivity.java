package co.edu.uniajc.vtf.content;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.ListSitesFragment.ListPointsAdapter;
import co.edu.uniajc.vtf.content.controller.CommentsController;
import co.edu.uniajc.vtf.content.interfaces.IComments;
import co.edu.uniajc.vtf.content.model.CommentEntity;
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
		this.loadData();
	}
	
	@Override
	public void loadData(){
		ResourcesManager loResource = new ResourcesManager(this);
		this.coProgressDialog = new ProgressDialog(this);
		this.coProgressDialog.setMessage(loResource.getStringResource(R.string.general_progress_message));
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
			
			//SessionManager loSession = new SessionManager(coContext);   		
			//String lsUserName = loSession.getUserName();		
			
			/*if(lsUserName.equals(loCommentData.getUserName())){
				loName.setTextColor(Color.parseColor("#FF6600"));
			}*/
			
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
	}
	
	public void blankFields(){
		EditText loInsertComment = (EditText)this.findViewById(R.id.txtInsertComment);		
		loInsertComment.setText("");
	}
	
}
