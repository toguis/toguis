/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 * Original code by: Emil Adjiev
 * WebSite: http://blog-emildesign.rhcloud.com/?p=822
 ***********************************************************************************************/
package co.edu.uniajc.vtf.utils;

import java.util.ArrayList;
import java.util.Map;

import org.w3c.dom.Document;

import android.os.AsyncTask;
import co.edu.uniajc.vtf.content.interfaces.INavigation;

import com.google.android.gms.maps.model.LatLng;

public class GetDirectionsAsyncTask extends AsyncTask<Map<String, String>, Object, ArrayList<DirectionsEntity>> {
	public static final String USER_CURRENT_LAT = "user_current_lat";
    public static final String USER_CURRENT_LONG = "user_current_long";
    public static final String DESTINATION_LAT = "destination_lat";
    public static final String DESTINATION_LONG = "destination_long";
    public static final String DIRECTIONS_MODE = "directions_mode";
    public static final String LANGUAGE = "language";
    private INavigation coView;
    private Exception coException;
 
    public GetDirectionsAsyncTask(INavigation activity)
    {
        super();
        this.coView = activity;
    }
 
    public void onPreExecute()
    {

    }
 
    @Override
    public void onPostExecute(ArrayList<DirectionsEntity> result)
    {   
        if (coException == null)
        {
            coView.handleGetDirectionsResult(result);
        }
        else
        {
            processException();
        }
    }
 
    @Override
    protected ArrayList<DirectionsEntity> doInBackground(Map<String, String>... params)
    {
        Map<String, String> paramMap = params[0];
        try
        {
            LatLng loFromPosition = new LatLng(Double.valueOf(paramMap.get(USER_CURRENT_LAT)) , Double.valueOf(paramMap.get(USER_CURRENT_LONG)));
            LatLng loToPosition = new LatLng(Double.valueOf(paramMap.get(DESTINATION_LAT)) , Double.valueOf(paramMap.get(DESTINATION_LONG)));
            GMapV2Direction loMapService = new GMapV2Direction();
            String lsLanguage = paramMap.get(LANGUAGE);
            Document loResultDocument = loMapService.getDocument(loFromPosition, loToPosition, paramMap.get(DIRECTIONS_MODE), lsLanguage);
            ArrayList<DirectionsEntity> loDirectionPoints = loMapService.getDirection(loResultDocument);
            return loDirectionPoints;
        }
        catch (Exception e)
        {
            coException = e;
            return null;
        }
    }
 
    private void processException()
    {
        this.coView.onError();
    }
}
