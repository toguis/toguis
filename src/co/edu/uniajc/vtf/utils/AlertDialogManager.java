/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import co.edu.uniajc.vtf.R;

public class AlertDialogManager {
	public static final int WARNING = 0;
	public static final int SUCCESS = 1;
	public static final int ERROR = 2;
	
    public static void showAlertDialog(Context context, String title, String message, int status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(title);
  
        // Setting Dialog Message
        alertDialog.setMessage(message);
  
    	switch(status){
    		case WARNING:
    			alertDialog.setIcon(R.drawable.warning); 
    			break;
    		case SUCCESS:
    			alertDialog.setIcon(R.drawable.success); 
    			break;
    		case ERROR:
    			alertDialog.setIcon(R.drawable.error); 
    			break;        			
    	}  	
          	
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }	
}
