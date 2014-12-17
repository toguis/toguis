package co.edu.uniajc.vtf.utils;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class DirectionsEntity {
	private ArrayList<LatLng> coGeopoints;
	private String csDistance;
	private String csDuration;
	private String csInstructions;
	
	public ArrayList<LatLng> getGeospoint() {
		return coGeopoints;
	}
	public void setGeopoint(ArrayList<LatLng> pGeopoint) {
		coGeopoints = pGeopoint;
	}
	public String getDistance() {
		return csDistance;
	}
	public void setDistance(String distance) {
		csDistance = distance;
	}
	public String getDuration() {
		return csDuration;
	}
	public void setDuration(String duration) {
		csDuration = duration;
	}
	public String getInstructions() {
		return csInstructions;
	}
	public void setInstructions(String instructions) {
		csInstructions = instructions;
	}
	
	
}
