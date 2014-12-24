package co.edu.uniajc.vtf.utils;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class DirectionsEntity implements Parcelable{
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
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel in, int flags) {
		in.writeString(this.csDistance);
		in.writeString(this.csDuration);
		in.writeString(this.csInstructions);
	}	

	
    public static final Parcelable.Creator<DirectionsEntity> CREATOR = new Creator<DirectionsEntity>() {  
		 public DirectionsEntity createFromParcel(Parcel source) {  
			 DirectionsEntity loDirection = new DirectionsEntity();  
			 loDirection.csDistance = source.readString();
			 loDirection.csDuration = source.readString();
			 loDirection.csInstructions = source.readString();		
		     return loDirection;  
		 }

		@Override
		public DirectionsEntity[] newArray(int size) {
			// TODO Auto-generated method stub
			return new DirectionsEntity[size];
		}  
    };
	
}
