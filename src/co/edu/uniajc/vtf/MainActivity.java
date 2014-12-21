package co.edu.uniajc.vtf;

import android.app.Activity;
import android.os.Bundle;
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

    	loOptionsData.setArea(5000);
    	loOptionsData.setLanguageIsoId("es");
    	loOptions.createOrUpdateOptions(loOptionsData);
    	
		this.coController = new MainController(this);	
		this.coController.StartTimer();
	}

}
