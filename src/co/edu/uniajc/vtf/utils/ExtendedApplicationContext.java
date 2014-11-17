package co.edu.uniajc.vtf.utils;

import java.util.ArrayList;

import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;
import android.app.Application;

public class ExtendedApplicationContext extends Application{
	private ArrayList<PointOfInterestEntity> coBufferPoints;

	public ArrayList<PointOfInterestEntity> getBufferPoints() {
		return coBufferPoints;
	}

	public void setBufferPoints(ArrayList<PointOfInterestEntity> bufferPoints) {
		coBufferPoints = bufferPoints;
	}
	
	
}
