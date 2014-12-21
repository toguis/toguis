package co.edu.uniajc.vtf.content.interfaces;

import android.graphics.Bitmap;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;

public interface IPoiDetail {
	void setTitle(String pTitle);
	void setImage(Bitmap pImage);
	void setRating(double pRating);
	void setDescription(String pDescription);
	void setFavorite(boolean pIsFavorite);
	void setAddress(String pAddress);
	boolean isFavorite();
	void setVisited(boolean pWasVisited);
	boolean wasVisited();
	void hideProgressDialog();
	double getPersonalRating();
	void setPersonalRating(double pRating);
	void loadData();
	void setPoiData(PointOfInterestEntity pPoint);
	PointOfInterestEntity getPoiData();
}
