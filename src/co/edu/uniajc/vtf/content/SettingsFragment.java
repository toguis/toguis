package co.edu.uniajc.vtf.content;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.security.model.LogoutListener;
import co.edu.uniajc.vtf.utils.SessionManager;

public class SettingsFragment extends Fragment{
    
	protected ArrayList<LogoutListener> coModelListener;
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {			
		return inflater.inflate(R.layout.fragment_settings, container, false);     
    }
	
    @Override
    public void onActivityCreated(Bundle state) {
    	super.onActivityCreated(state);
    	Button loSessionLogout = (Button)this.getView().findViewById(R.id.btnSessionLogout);
    	SessionManager loSession = new SessionManager(this.getActivity());  	
    	loSessionLogout.setText("Salir : " + loSession.getNames());
    	
    	loSessionLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsFragment.this.startLogout();			
			}
   
        });
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
}
