package co.edu.uniajc.vtf.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class OptionsManager {
	private SharedPreferences coShared;
	private Context coContext;
	private Editor coEditor;
	private static final String OPT_NAME = "toguisOptions";
	private static final String OPTION_LANGUAGE = "languageId";
	private static final String OPTION_FILTER_MONUMENT = "monument";
	private static final String OPTION_FILTER_MUSEUM = "museum";
	private static final String OPTION_FILTER_HOTEL = "hotel";
	private static final String OPTION_FILTER_RESTAURANT = "restaurant";
	private static final String OPTION_FILTER_INTEREST = "interest";
	private static final String OPTION_FILTER_BUILDING = "building";
	private static final String OPTION_FILTER_TRANSPORT = "transport";
	private static final String OPTION_FILTER_EVENT = "event";
	private static final String OPTION_CITY = "cityId";
	private static final String OPTION_RANGE_AREA = "area";
	private static final String OPTION_SEARCH = "search";
	private static final String OPTION_LANG_ISO_ID = "isoid";
	
	public OptionsManager(Context pContext){
		this.coContext = pContext;
		this.coShared = this.coContext.getSharedPreferences(OPT_NAME, Context.MODE_PRIVATE);
		this.coEditor = this.coShared.edit();
	}
		
	/*public int getLanguageId() {
		return this.coShared.getInt(OPTION_LANGUAGE, 1);
	}
	
	public boolean isFilterMonument() {
		return this.coShared.getBoolean(OPTION_FILTER_MONUMENT, true);
	}
 
	public boolean isFilterMuseum() {
		return this.coShared.getBoolean(OPTION_FILTER_MUSEUM, true);
	}
 
	public boolean isFilterHotel() {
		return this.coShared.getBoolean(OPTION_FILTER_HOTEL, true);
	}
 
	public boolean isFilterRestaurant() {
		return this.coShared.getBoolean(OPTION_FILTER_RESTAURANT, true);
	}
 
	public boolean isFilterInterest() {
		return this.coShared.getBoolean(OPTION_FILTER_INTEREST, true);
	}
 
	public boolean isFilterBuilding() {
		return this.coShared.getBoolean(OPTION_FILTER_BUILDING, true);
	}
 
	public boolean isFilterTransport() {
		return this.coShared.getBoolean(OPTION_FILTER_TRANSPORT, true);
	}
 
	public boolean isFilterEvent() {
		return this.coShared.getBoolean(OPTION_FILTER_EVENT, true);
	}
 
	public int getCityId() {
		return this.coShared.getInt(OPTION_CITY, 1);
	}
 
	public int getArea() {
		return this.coShared.getInt(OPTION_RANGE_AREA, 10);
	}

	public String getSearch(){
		return this.coShared.getString(OPTION_SEARCH, "");
	}
	*/
	public OptionsEntity getOptions(){
		OptionsEntity loOptions = new OptionsEntity();
		loOptions.setLanguageId(this.coShared.getInt(OPTION_LANGUAGE, 1));
		loOptions.setFilterMonument(this.coShared.getBoolean(OPTION_FILTER_MONUMENT, true));
		loOptions.setFilterMuseum(this.coShared.getBoolean(OPTION_FILTER_MUSEUM, true));
		loOptions.setFilterHotel(this.coShared.getBoolean(OPTION_FILTER_HOTEL, true));
		loOptions.setFilterRestaurant(this.coShared.getBoolean(OPTION_FILTER_RESTAURANT, true));
		loOptions.setFilterInterest(this.coShared.getBoolean(OPTION_FILTER_INTEREST, true));
		loOptions.setFilterBuilding(this.coShared.getBoolean(OPTION_FILTER_BUILDING, true));
		loOptions.setFilterTransport(this.coShared.getBoolean(OPTION_FILTER_TRANSPORT, true));
		loOptions.setFilterEvent(this.coShared.getBoolean(OPTION_FILTER_EVENT, true));
		loOptions.setCityId(this.coShared.getInt(OPTION_CITY, 1));
		loOptions.setArea(this.coShared.getInt(OPTION_RANGE_AREA, 100));
		loOptions.setSearch(this.coShared.getString(OPTION_SEARCH, ""));
		loOptions.setLanguageIsoId(this.coShared.getString(OPTION_LANG_ISO_ID, "en"));
		return loOptions;
	}
	
	public void createOrUpdateOptions(OptionsEntity pOptions){
		if(this.coEditor != null){
			this.coEditor.putInt(OPTION_LANGUAGE, pOptions.getLanguageId());
			this.coEditor.putBoolean(OPTION_FILTER_MONUMENT, pOptions.isFilterMonument());
			this.coEditor.putBoolean(OPTION_FILTER_MUSEUM, pOptions.isFilterMuseum());
			this.coEditor.putBoolean(OPTION_FILTER_HOTEL, pOptions.isFilterHotel());
			this.coEditor.putBoolean(OPTION_FILTER_RESTAURANT, pOptions.isFilterRestaurant());
			this.coEditor.putBoolean(OPTION_FILTER_INTEREST, pOptions.isFilterInterest());
			this.coEditor.putBoolean(OPTION_FILTER_BUILDING, pOptions.isFilterBuilding());
			this.coEditor.putBoolean(OPTION_FILTER_TRANSPORT, pOptions.isFilterTransport());
			this.coEditor.putBoolean(OPTION_FILTER_EVENT, pOptions.isFilterEvent());
			this.coEditor.putInt(OPTION_CITY, pOptions.getCityId());
			this.coEditor.putInt(OPTION_RANGE_AREA, pOptions.getArea());
			this.coEditor.putString(OPTION_SEARCH, pOptions.getSearch());
			this.coEditor.putString(OPTION_LANG_ISO_ID, pOptions.getLanguageIsoId());
			this.coEditor.commit();
		}
	}
	
	public void endSession(){
		if(this.coEditor != null){
			this.coEditor.clear();
			this.coEditor.commit();			
		}
	}
}
