/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.content.interfaces;

import java.util.ArrayList;

import co.edu.uniajc.vtf.utils.DirectionsEntity;

public interface INavigation {
	void handleGetDirectionsResult(ArrayList<DirectionsEntity> PdirectionPoints);
	void onError();
}
