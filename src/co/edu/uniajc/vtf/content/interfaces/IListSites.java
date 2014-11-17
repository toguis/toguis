package co.edu.uniajc.vtf.content.interfaces;

import java.util.ArrayList;

import android.location.Location;
import co.edu.uniajc.vtf.content.ListSitesFragment.ListPointsAdapter;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;

public interface IListSites {
	void setAdapter(ListPointsAdapter pAdapter);
	ListPointsAdapter getAdapter();	
	void setAdapterData(ArrayList<PointOfInterestEntity> pPoints);
	Location getCurrentLocation();
}
