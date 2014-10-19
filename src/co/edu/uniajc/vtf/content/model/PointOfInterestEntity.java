package co.edu.uniajc.vtf.content.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class PointOfInterestEntity {
	private int siteType;
	private String csTitle;
	private String csDescription;
	private String csImage;
	private boolean cboFavorite;
	private boolean cboVisited;
	private int ciRating;
	private double cdLatitude;
	private double cdLongitude;
	private double csAltitude;
	private String csAddress;
	
	public int getSiteType() {
		return siteType;
	}
	public void setSiteType(int siteType) {
		this.siteType = siteType;
	}
	public String getAddress() {
		return csAddress;
	}
	public void setAddress(String address) {
		this.csAddress = address;
	}
	public String getTitle() {
		return csTitle;
	}
	public void setTitle(String title) {
		this.csTitle = title;
	}
	public String getDescription() {
		return csDescription;
	}
	public void setDescription(String description) {
		this.csDescription = description;
	}
	public Bitmap getImage() {
		byte[] loData = Base64.decode(this.csImage, Base64.DEFAULT);
		Bitmap loBitmap = BitmapFactory.decodeByteArray(loData, 0, loData.length);	
		return loBitmap;
	}
	public void setImage(String image) {
		this.csImage = image;
	}
	public boolean isFavorite() {
		return cboFavorite;
	}
	public void setFavorite(boolean favorite) {
		this.cboFavorite = favorite;
	}
	public boolean isVisited() {
		return cboVisited;
	}
	public void setVisited(boolean visited) {
		this.cboVisited = visited;
	}
	public int getRating() {
		return ciRating;
	}
	public void setRating(int rating) {
		this.ciRating = rating;
	}
	public double getLatitude() {
		return cdLatitude;
	}
	public void setLatitude(double latitude) {
		this.cdLatitude = latitude;
	}
	public double getLongitude() {
		return cdLongitude;
	}
	public void setLongitude(double longitude) {
		this.cdLongitude = longitude;
	}
	public double getAltitude() {
		return csAltitude;
	}
	public void setAltitude(double altitude) {
		this.csAltitude = altitude;
	}
	
	
	
	
}
