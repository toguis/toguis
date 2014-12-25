package co.edu.uniajc.vtf.utils;

import java.util.ArrayList;

import android.app.Application;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;

public class ExtendedApplicationContext extends Application{
	private ArrayList<PointOfInterestEntity> coBufferPoints;
	private Object coData;
	
	public Object getData() {
		return coData;
	}

	public void setData(Object data) {
		coData = data;
	}

	public ArrayList<PointOfInterestEntity> getBufferPoints() {
		return coBufferPoints;
	}

	public void setBufferPoints(ArrayList<PointOfInterestEntity> bufferPoints) {
		coBufferPoints = bufferPoints;
	}
	
	

}
