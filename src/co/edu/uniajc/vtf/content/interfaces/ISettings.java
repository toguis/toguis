package co.edu.uniajc.vtf.content.interfaces;

import java.util.List;

import co.edu.uniajc.vtf.content.model.LanguageEntity;

public interface ISettings {
	void setCities(List<CityEntity> pData);
	void setLanguages(List<LanguageEntity> pData);
	boolean getMonumentValue();
	boolean getMuseumValue();
	boolean getRestaurantValue();
	boolean getInterestValue();
	boolean getBuildingValue();
	boolean getTransportValue();
	boolean getHotelValue();
	boolean getEventValue();
	int getDistanceValue();
	int getLanguageValue();
	String getLanguageISOValue();
	int getCityValue();
	void setMonumentValue(boolean pValue);
	void setMuseumValue(boolean pValue);
	void setRestaurantValue(boolean pValue);
	void setInterestValue(boolean pValue);
	void setBuildingValue(boolean pValue);
	void setTransportValue(boolean pValue);
	void setHotelValue(boolean pValue);
	void setEventValue(boolean pValue);
	void setDistanceValue(int pValue);
	void setLanguageValue(int pValue);
	void setCityValue(int pValue);
}
