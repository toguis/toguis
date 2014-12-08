package co.edu.uniajc.vtf.content.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.edu.uniajc.vtf.security.model.UserEntity;
import co.edu.uniajc.vtf.utils.BaseModel;
import co.edu.uniajc.vtf.utils.DateUtilities;
import co.edu.uniajc.vtf.utils.ModelListener;
import co.edu.uniajc.vtf.utils.RestAsyncTask;

public class CommentsModel extends BaseModel {

	public CommentsModel(String pBaseUrl) {
		super.csBaseUrl = pBaseUrl;
		super.csMethod = "";
		super.coModelListener = new ArrayList<ModelListener>();	
	}

	public void getCommentsAsync(int pPoiId){
		String lsQueryUrl = super.csBaseUrl + "ToguisPoints.svc/get_comments?";
		StringBuilder lsParams = new StringBuilder();
		lsParams.append("poiid=").append(pPoiId);
		lsQueryUrl += lsParams.toString();
		super.csMethod = "getCommentsAsync";
		RestAsyncTask loTask = new RestAsyncTask();
		loTask.addAsyncTaskListener(this);	
		loTask.execute("0", lsQueryUrl);
	}
	
	private void getComments(String pData){
		ArrayList<CommentEntity> loComments = new ArrayList<CommentEntity>();
		if(!pData.equals("")){
			try {
				
				JSONArray loArrayData = new JSONArray(pData);
				for (int i = 0; i < loArrayData.length(); i++) {
					JSONObject loCommentData = loArrayData.getJSONObject(i);
					CommentEntity loComment = new CommentEntity();
					
					loComment.setComment(loCommentData.getString("COM_COMMENT"));
					String lsRawDate = loCommentData.getString("COM_DATE");
					
					Pattern loDatePatt = Pattern.compile("^/Date\\((\\d+)([+-]\\d+)?\\)/$");
					Matcher m = loDatePatt.matcher(lsRawDate);
					Date loDate = new Date();
					if (m.matches()) {
						Long llMilliseconds = Long.parseLong(m.group(1));
						loDate = new Date(llMilliseconds);						
					}
					else{
						loDate = Calendar.getInstance().getTime();
					}
					
					SimpleDateFormat loSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
					String loFormattDate = loSdf.format(loDate);
					loComment.setDate(loFormattDate);
					
					JSONObject loUser = loCommentData.getJSONObject("TG_USER");
					loComment.setName(loUser.getString("USR_NAME"));
					loComment.setUserName(loUser.getString("USR_ID"));
					loComment.setId(loCommentData.getInt("COM_ID"));
					loComment.setPoiId(loCommentData.getInt("POI_ID"));
					
					loComments.add(loComment);

				}						
				for (ModelListener item : this.coModelListener){
					item.onGetData(loComments, 0);
				}					
			} catch (JSONException ex) {
				for (ModelListener item : this.coModelListener){
					item.onError(ex.getMessage(), 0);
				}	
			}
			
			
		}
	}
	
	public void setCommentAsync(CommentEntity pComment){
		String lsData = this.getFomattedData(pComment);
		String lsQueryUrl = super.csBaseUrl + "ToguisPoints.svc/set_comment";
		super.csMethod = "setCommentAsync";
		RestAsyncTask loTask = new RestAsyncTask();
		loTask.addAsyncTaskListener(this);		
		loTask.execute("1",lsQueryUrl,lsData);	
	}
	
	private void setComment(int pData){
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
	
	private String getFomattedData(CommentEntity pComment){
		StringBuilder loData = new StringBuilder();
		String loDate = DateUtilities.getFormattedDate(Calendar.getInstance());
		loData.append("{");
		loData.append("\"COM_COMMENT\":\"").append(pComment.getComment()).append("\",");
		loData.append("\"COM_DATE\":\"" + loDate +  "\",");
		loData.append("\"POI_ID\":\"").append(pComment.getPoiId()).append("\",");
		loData.append("\"USR_ID\":\"").append(pComment.getUserName()).append("\"");
		loData.append("}");

		return loData.toString();
	}
		
	@Override
	public void onQuerySuccessful(String result) {
		if(super.csMethod.equals("getCommentsAsync")){
			this.getComments(result);
		}
		else if(super.csMethod.equals("setCommentAsync")){
			this.setComment(Integer.parseInt(result));
		}
	}

	@Override
	public void onQueryError(String error) {
		for (ModelListener item : this.coModelListener){
			item.onError(error, 0);
		}	
	}	
}
