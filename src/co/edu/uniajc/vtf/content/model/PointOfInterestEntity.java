/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.content.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

public class PointOfInterestEntity implements Parcelable{
    private int ciId;
    private int ciSiteType;
    private String csTitle;
    private String csDescription;
    private String csImage;
    private boolean cboFavorite;
    private boolean cboVisited;
    private double cdRating;
    private double cdLatitude;
    private double cdLongitude;
    private double cdAltitude;
    private String csAddress;
    private double cdAvgRating;
  
    public int getId() {
        return ciId;
    }
    
    public void setId(int id) {
        this.ciId = id;
    }
    
    public int getSiteType() {
        return ciSiteType;
    }
    
    public void setSiteType(int siteType) {
        this.ciSiteType = siteType;
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
    
    public Bitmap getBitmapImage(){
        byte[] loData = Base64.decode(this.csImage, Base64.DEFAULT);
        Bitmap loBitmap = BitmapFactory.decodeByteArray(loData, 0, loData.length);	
        return loBitmap;   	
    }
    
    public String getImage() {
        return this.csImage;
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
    
    public double getRating() {
        return cdRating;
    }
    
    public void setRating(double rating) {
        this.cdRating = rating;
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
            return cdAltitude;
    }
    
    public void setAltitude(double altitude) {
            this.cdAltitude = altitude;
    }
    
    public double getAvgRating() {
        return cdAvgRating;
    }

    public void setAvgRating(double AvgRating) {
        this.cdAvgRating = AvgRating;
    }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel in, int flags) {
		in.writeByte((byte) (this.cboFavorite ? 1 : 0));
		in.writeByte((byte) (this.cboVisited ? 1 : 0));
		in.writeDouble(this.cdAvgRating);
		in.writeDouble(this.cdLatitude);
		in.writeDouble(this.cdLongitude);
		in.writeDouble(this.cdRating);
		in.writeInt(this.ciId);
		in.writeString(this.csAddress);
		in.writeDouble(this.cdAltitude);
		in.writeString(this.csDescription);
		//in.writeString(this.csImage);
		in.writeString(this.csTitle);
		in.writeInt(this.ciSiteType);
	}	

	
    public static final Parcelable.Creator<PointOfInterestEntity> CREATOR = new Creator<PointOfInterestEntity>() {  
		 public PointOfInterestEntity createFromParcel(Parcel source) {  
			 PointOfInterestEntity loPoint = new PointOfInterestEntity();  
			 loPoint.cboFavorite = source.readByte() != 0;
			 loPoint.cboVisited = source.readByte() != 0;
			 loPoint.cdAvgRating = source.readDouble();
			 loPoint.cdLatitude = source.readDouble();
			 loPoint.cdLongitude = source.readDouble();
			 loPoint.cdRating = source.readDouble();
			 loPoint.ciId = source.readInt();
			 loPoint.csAddress = source.readString();
			 loPoint.cdAltitude = source.readDouble();
			 loPoint.csDescription = source.readString();
			 //loPoint.csImage = source.readString();
			 loPoint.csTitle = source.readString();
			 loPoint.ciSiteType = source.readInt();
		
		     return loPoint;  
		 }

		@Override
		public PointOfInterestEntity[] newArray(int size) {
			// TODO Auto-generated method stub
			return new PointOfInterestEntity[size];
		}  
    };
}

