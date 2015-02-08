/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import co.edu.uniajc.vtf.NoNetworkActivity;
import co.edu.uniajc.vtf.utils.NetworkUtility;

public class NetworkStatusReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {		
		if(!NetworkUtility.isOnline(arg0)){	
			Intent loIntent = new Intent(arg0, NoNetworkActivity.class);
			loIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | 
			                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
			                    Intent.FLAG_ACTIVITY_NEW_TASK);
			arg0.startActivity(loIntent);				
		}
	}

}
