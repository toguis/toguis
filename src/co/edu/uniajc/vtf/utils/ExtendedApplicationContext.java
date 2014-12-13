package co.edu.uniajc.vtf.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;

import com.google.android.gms.maps.model.Marker;

public class ExtendedApplicationContext extends Application{
	private ArrayList<PointOfInterestEntity> coBufferPoints;

	public ArrayList<PointOfInterestEntity> getBufferPoints() {
		return coBufferPoints;
	}

	public void setBufferPoints(ArrayList<PointOfInterestEntity> bufferPoints) {
		coBufferPoints = bufferPoints;
	}

}
