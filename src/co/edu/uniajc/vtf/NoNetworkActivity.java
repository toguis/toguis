package co.edu.uniajc.vtf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NoNetworkActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_no_network);
		Button loSessionLogout = (Button)this.findViewById(R.id.btnTry);
    	loSessionLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NoNetworkActivity.this.onClick_Try(v);
			}
   
        });		
	}

	public void onClick_Try(View view){
		Intent loIntent = new Intent(NoNetworkActivity.this, MainActivity.class);
		NoNetworkActivity.this.startActivity(loIntent);	
		NoNetworkActivity.this.finish();		
	}
}
