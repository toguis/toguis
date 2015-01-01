/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.controller.ARLauncherController;
import co.edu.uniajc.vtf.content.interfaces.IARLauncher;

public class ARLauncherFragment extends Fragment implements IARLauncher {
	private ARLauncherController coController;
	
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_launcher_ar, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle state) {
    	super.onActivityCreated(state);	
    	ImageButton loLaunchAR = (ImageButton)this.getView().findViewById(R.id.btnLaunchAR);
    	loLaunchAR.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ARLauncherFragment.this.coController.navigateToARView();			
			}
        });
    	
    	coController = new ARLauncherController(this);
    } 
    
}
