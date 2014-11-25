package co.edu.uniajc.vtf.utils;

public class OptionsEntity {
	private int ciLanguageId;
	private boolean cboFilterMonument;
	private boolean cboFilterMuseum;
	private boolean cboFilterHotel;
	private boolean cboFilterRestaurant;
	private boolean cboFilterInterest;
	private boolean cboFilterBuilding;
	private boolean cboFilterTransport;
	private boolean cboFilterEvent;
	private int ciCityId;
	private int ciArea;
	private String csSearch;
	
	public int getLanguageId() {
		return ciLanguageId;
	}
	public void setLanguageId(int languageId) {
		ciLanguageId = languageId;
	}
	public boolean isFilterMonument() {
		return cboFilterMonument;
	}
	public void setFilterMonument(boolean filterMonument) {
		cboFilterMonument = filterMonument;
	}
	public boolean isFilterMuseum() {
		return cboFilterMuseum;
	}
	public void setFilterMuseum(boolean filterMuseum) {
		cboFilterMuseum = filterMuseum;
	}
	public boolean isFilterHotel() {
		return cboFilterHotel;
	}
	public void setFilterHotel(boolean filterHotel) {
		cboFilterHotel = filterHotel;
	}
	public boolean isFilterRestaurant() {
		return cboFilterRestaurant;
	}
	public void setFilterRestaurant(boolean filterRestaurant) {
		cboFilterRestaurant = filterRestaurant;
	}
	public boolean isFilterInterest() {
		return cboFilterInterest;
	}
	public void setFilterInterest(boolean filterInterest) {
		cboFilterInterest = filterInterest;
	}
	public boolean isFilterBuilding() {
		return cboFilterBuilding;
	}
	public void setFilterBuilding(boolean filterBuilding) {
		cboFilterBuilding = filterBuilding;
	}
	public boolean isFilterTransport() {
		return cboFilterTransport;
	}
	public void setFilterTransport(boolean filterTransport) {
		cboFilterTransport = filterTransport;
	}
	public boolean isFilterEvent() {
		return cboFilterEvent;
	}
	public void setFilterEvent(boolean filterEvent) {
		cboFilterEvent = filterEvent;
	}
	public int getCityId() {
		return ciCityId;
	}
	public void setCityId(int cityId) {
		ciCityId = cityId;
	}
	public int getArea() {
		return ciArea;
	}
	public void setArea(int area) {
		ciArea = area;
	}
	public String getSearch() {
		return csSearch;
	}
	public void setSearch(String search) {
		csSearch = search;
	}
	
	
	
	
}
