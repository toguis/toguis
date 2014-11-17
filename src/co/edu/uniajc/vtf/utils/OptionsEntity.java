package co.edu.uniajc.vtf.utils;

public class OptionsEntity {
	private int ciLanguageId;
	private boolean lboFilterMonument;
	private boolean lboFilterMuseum;
	private boolean lboFilterHotel;
	private boolean FilterRestaurant;
	private boolean FilterInterest;
	private boolean FilterBuilding;
	private boolean FilterTransport;
	private boolean FilterEvent;
	private int CityId;
	private int Area;
	
	public int getLanguageId() {
		return ciLanguageId;
	}
	public void setLanguageId(int languageId) {
		ciLanguageId = languageId;
	}
	public boolean isFilterMonument() {
		return lboFilterMonument;
	}
	public void setFilterMonument(boolean filterMonument) {
		lboFilterMonument = filterMonument;
	}
	public boolean isFilterMuseum() {
		return lboFilterMuseum;
	}
	public void setFilterMuseum(boolean filterMuseum) {
		lboFilterMuseum = filterMuseum;
	}
	public boolean isFilterHotel() {
		return lboFilterHotel;
	}
	public void setFilterHotel(boolean filterHotel) {
		lboFilterHotel = filterHotel;
	}
	public boolean isFilterRestaurant() {
		return FilterRestaurant;
	}
	public void setFilterRestaurant(boolean filterRestaurant) {
		FilterRestaurant = filterRestaurant;
	}
	public boolean isFilterInterest() {
		return FilterInterest;
	}
	public void setFilterInterest(boolean filterInterest) {
		FilterInterest = filterInterest;
	}
	public boolean isFilterBuilding() {
		return FilterBuilding;
	}
	public void setFilterBuilding(boolean filterBuilding) {
		FilterBuilding = filterBuilding;
	}
	public boolean isFilterTransport() {
		return FilterTransport;
	}
	public void setFilterTransport(boolean filterTransport) {
		FilterTransport = filterTransport;
	}
	public boolean isFilterEvent() {
		return FilterEvent;
	}
	public void setFilterEvent(boolean filterEvent) {
		FilterEvent = filterEvent;
	}
	public int getCityId() {
		return CityId;
	}
	public void setCityId(int cityId) {
		CityId = cityId;
	}
	public int getArea() {
		return Area;
	}
	public void setArea(int area) {
		Area = area;
	}
	
	
	
}
