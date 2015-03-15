/***********************************************************************************************
	 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf;

import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import co.edu.uniajc.vtf.controller.MainController;
import co.edu.uniajc.vtf.interfaces.IMain;
import co.edu.uniajc.vtf.utils.OptionsEntity;
import co.edu.uniajc.vtf.utils.OptionsManager;

public class MainActivity extends Activity implements IMain {
	private MainController coController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
       	OptionsManager loOptions = new OptionsManager(this); 
    	OptionsEntity loOptionsData = loOptions.getOptions();

    	//load the language
    	String lsISOLang = loOptionsData.getLanguageIsoId();
    	this.setLocal(lsISOLang);   	
		this.coController = new MainController(this);	
		this.coController.StartTimer();
	}
	
	//set local language before to start
	private void setLocal(String pISOLang) {
		Locale loLocale = new Locale(pISOLang);
		Resources loResource = getResources();
		DisplayMetrics loDm = loResource.getDisplayMetrics();
		Configuration loConfig = loResource.getConfiguration();
		loConfig.locale = loLocale;
		loResource.updateConfiguration(loConfig, loDm);        
	}

}
