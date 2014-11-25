package co.edu.uniajc.vtf;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import co.edu.uniajc.vtf.content.ListSitesFragment;
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
       	/*OptionsManager loOptions = new OptionsManager(this); 
    	OptionsEntity loOptionsData = loOptions.getOptions();

    	loOptionsData.setFilterMonument(true);
    	loOptionsData.setFilterMuseum(true);
    	loOptions.createOptions(loOptionsData);*/
    	
		this.coController = new MainController(this);	
		this.coController.StartTimer();
	}

}
