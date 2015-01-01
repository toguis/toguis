/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.utils;

import java.util.ArrayList;

public abstract class BaseModel implements RestAsyncTaskListener {
	protected String csMethod;
	protected String csBaseUrl;
	protected ArrayList<ModelListener> coModelListener;
	
	public void addModelListener(ModelListener pModelListener){
		this.coModelListener.add(pModelListener);
	}
	
}
