package co.edu.uniajc.vtf.content.interfaces;

import java.util.ArrayList;

import android.location.Location;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;

public interface ILoadExtenalDataFromMap {
	void loadDataFromMap(ArrayList<PointOfInterestEntity> pPoints, Location pLastLocation);
}
