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

import android.location.Location;
import co.edu.uniajc.vtf.content.ListSitesFragment.ListPointsAdapter;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;

public interface IListSites {
	void setAdapter(ListPointsAdapter pAdapter);
	ListPointsAdapter getAdapter();	
	void setAdapterData(ArrayList<PointOfInterestEntity> pPoints);
	void setAdapterData(ArrayList<PointOfInterestEntity> pPoints, boolean triggerEvent);
	Location getCurrentLocation();
	void showProgressDialog();
	void hideProgressDialog();
	
}
