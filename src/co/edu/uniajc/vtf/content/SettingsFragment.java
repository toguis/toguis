package co.edu.uniajc.vtf.content;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.controller.SettingsController;
import co.edu.uniajc.vtf.content.interfaces.CityEntity;
import co.edu.uniajc.vtf.content.interfaces.ISettings;
import co.edu.uniajc.vtf.content.model.LanguageEntity;
import co.edu.uniajc.vtf.security.model.LogoutListener;
import co.edu.uniajc.vtf.utils.SessionManager;

public class SettingsFragment extends Fragment implements ISettings{
    
	private SpinnerLanguageAdapter coLanguageAdapter;
	private int ciSelectedLanguageId;
	private SpinnerCityAdapter coCityAdapter;
	private int ciSelectedCityId;
	
	protected ArrayList<LogoutListener> coModelListener;
	private SettingsController coController;
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {			
		return inflater.inflate(R.layout.fragment_settings, container, false);     
    }
	
    @Override
    public void onActivityCreated(Bundle state) {
    	super.onActivityCreated(state);	
    	SessionManager loSession = new SessionManager(this.getActivity());  	
    		
    	Button loSessionLogout = (Button)this.getView().findViewById(R.id.btnSessionLogout);
    	loSessionLogout.setText("Salir : " + loSession.getNames());
    	loSessionLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsFragment.this.startLogout();			
			}
   
        });
    	
    	Button loSaveSettings = (Button)this.getView().findViewById(R.id.btnSaveSettings);
    	loSaveSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsFragment.this.SaveSettings();			
			}
   
        });
    	
    	Spinner loLanguage = (Spinner)this.getView().findViewById(R.id.spnLanguage);
    	loLanguage.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				ciSelectedLanguageId = position;				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
    		
    	});
    	
    	Spinner loCity = (Spinner)this.getView().findViewById(R.id.spnCities);
    	loCity.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				ciSelectedCityId = position;				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
    		
    	});
    	
    	this.coController = new SettingsController(this);
    	this.coController.getLanguagesAsync();
    	
	
    }
    
	public void startLogout()
	{
		for(LogoutListener item : this.coModelListener){
			item.logout();
		}
	}
	
	public void AddLogoutListener(LogoutListener pLogoutListener){
		if(this.coModelListener == null){
			this.coModelListener = new ArrayList<LogoutListener>();	
		}
		this.coModelListener.add(pLogoutListener);
	}
	
	public class SpinnerLanguageAdapter extends ArrayAdapter<LanguageEntity>{
		private List<LanguageEntity> coItems;
		
		public SpinnerLanguageAdapter(Context context, int resource, List<LanguageEntity> items) {
			super(context, resource);
			this.coItems = items;
		}
		
		public int getCount(){
		    return this.coItems.size();
		}
		
		public LanguageEntity getItem(int position){
		   return this.coItems.get(position);
		}
		
		public long getItemId(int position){
		   return position;
		}
		
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	LayoutInflater loInflater = SettingsFragment.this.getActivity().getLayoutInflater();
	    	View loView = null;
	    	if(convertView == null){
	    		loView = loInflater.inflate(R.layout.spinner_item, parent, false);
	    	}
	    	else{
	    		loView = convertView;
	    	}
				
	        TextView loSpinnerlabel = (TextView)loView.findViewById(R.id.lblSpinnerText);
	        loSpinnerlabel.setText(this.coItems.get(position).getName());

	        return loSpinnerlabel;
	    }

	    @Override
	    public View getDropDownView(int position, View convertView, ViewGroup parent) {
	        return this.getView(position, convertView, parent);
	    }
	}
	
	
	public class SpinnerCityAdapter extends ArrayAdapter<CityEntity>{
		private List<CityEntity> coItems;
		
		public SpinnerCityAdapter(Context context, int resource, List<CityEntity> items) {
			super(context, resource);
			this.coItems = items;
		}
		
		public int getCount(){
		    return this.coItems.size();
		}
		
		public CityEntity getItem(int position){
		   return this.coItems.get(position);
		}
		
		public long getItemId(int position){
		   return position;
		}
		
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	LayoutInflater loInflater = SettingsFragment.this.getActivity().getLayoutInflater();
	    	View loView = null;
	    	if(convertView == null){
	    		loView = loInflater.inflate(R.layout.spinner_item, parent, false);
	    	}
	    	else{
	    		loView = convertView;
	    	}
				
	        TextView loSpinnerlabel = (TextView)loView.findViewById(R.id.lblSpinnerText);
	        loSpinnerlabel.setText(this.coItems.get(position).getName());

	        return loSpinnerlabel;
	    }

	    @Override
	    public View getDropDownView(int position, View convertView, ViewGroup parent) {
	        return this.getView(position, convertView, parent);
	    }
	}
	
	@Override
	public void setCities(List<CityEntity> pData){
		Spinner loSpinner = (Spinner) this.getView().findViewById(R.id.spnCities);
		SpinnerCityAdapter loAdapter = new SpinnerCityAdapter(this.getActivity().getBaseContext(),R.layout.spinner_item, pData);
		loSpinner.setAdapter(loAdapter);	
		this.coCityAdapter = loAdapter;
	}
	
	@Override
	public void setLanguages(List<LanguageEntity> pData){
		Spinner loSpinner = (Spinner) this.getView().findViewById(R.id.spnLanguage);
		SpinnerLanguageAdapter loAdapter = new SpinnerLanguageAdapter(this.getActivity().getBaseContext(),R.layout.spinner_item, pData);
		loSpinner.setAdapter(loAdapter);
		this.coLanguageAdapter = loAdapter;
		this.coController.getCitiesAsync();
	}
	
	public void SaveSettings(){
		this.coController.SaveSettings();
		getLanguageValue();
	}
	
	@Override
	public boolean getMonumentValue(){
		CheckBox loControl = (CheckBox)this.getView().findViewById(R.id.chkMonuments);
		return loControl.isChecked();
	}
	
	@Override
	public boolean getMuseumValue(){
		CheckBox loControl = (CheckBox)this.getView().findViewById(R.id.chkMuseums);
		return loControl.isChecked();
	}
	
	@Override
	public boolean getRestaurantValue(){
		CheckBox loControl = (CheckBox)this.getView().findViewById(R.id.chkRestaurant);
		return loControl.isChecked();
	}
	
	@Override
	public boolean getInterestValue(){
		CheckBox loControl = (CheckBox)this.getView().findViewById(R.id.chkInterest);
		return loControl.isChecked();
	}
	
	@Override
	public boolean getBuildingValue(){
		CheckBox loControl = (CheckBox)this.getView().findViewById(R.id.chkBuilding);
		return loControl.isChecked();
	}
	
	@Override
	public boolean getTransportValue(){
		CheckBox loControl = (CheckBox)this.getView().findViewById(R.id.chkTransport);
		return loControl.isChecked();
	}
	
	@Override
	public boolean getHotelValue(){
		CheckBox loControl = (CheckBox)this.getView().findViewById(R.id.chkHotel);
		return loControl.isChecked();
	}
	
	@Override
	public boolean getEventValue(){
		CheckBox loControl = (CheckBox)this.getView().findViewById(R.id.chkEvent);
		return loControl.isChecked();
	}
	
	@Override
	public int getDistanceValue(){
		SeekBar loControl = (SeekBar)this.getView().findViewById(R.id.seekDistance);
		return loControl.getProgress();
	}
	
	@Override
	public int getLanguageValue(){
		return this.coLanguageAdapter.getItem(this.ciSelectedLanguageId).getId();
	}
	
	@Override
	public String getLanguageISOValue(){
		return this.coLanguageAdapter.getItem(this.ciSelectedLanguageId).getISOCode();
	}
	
	@Override
	public int getCityValue(){
		return this.coLanguageAdapter.getItem(this.ciSelectedCityId).getId();
	}
}

//falta
//toast de confirmacion
//visualizar el valor del slider
//falta cargar datos
