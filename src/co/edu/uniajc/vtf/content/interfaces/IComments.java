package co.edu.uniajc.vtf.content.interfaces;

import java.util.ArrayList;

import co.edu.uniajc.vtf.content.CommentsActivity.ListCommentsAdapter;
import co.edu.uniajc.vtf.content.ListSitesFragment.ListPointsAdapter;
import co.edu.uniajc.vtf.content.model.CommentEntity;

public interface IComments {
	void hideProgressDialog();
	void setAdapter(ListCommentsAdapter pAdapter);
	ListPointsAdapter getAdapter();	
	void setAdapterData(ArrayList<CommentEntity> pComments);
	void loadData();
	void blankFields();
	int getPoiId();
}
