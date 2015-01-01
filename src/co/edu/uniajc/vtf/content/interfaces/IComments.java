/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
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
