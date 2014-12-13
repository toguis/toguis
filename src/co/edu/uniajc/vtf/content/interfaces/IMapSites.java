package co.edu.uniajc.vtf.content.interfaces;

import java.util.ArrayList;

import android.location.Location;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;

public interface IMapSites {
	void setAdapterData(ArrayList<PointOfInterestEntity> pData);
	Location getCurrentLocation();
	void showProgressDialog();
	void hideProgressDialog();
}
