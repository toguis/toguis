/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.utils;

import android.content.Context;
import android.content.res.Resources;

public class ResourcesManager {
	private Context context;
		
	public ResourcesManager(Context pContext) {
		this.context = pContext;
	}

	public String getStringResource(int pId){
		Resources loResource = this.context.getResources();
		return loResource.getString(pId);
	}
}
