package co.edu.uniajc.vtf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import co.edu.uniajc.vtf.security.ConfigLoginActivity;
import co.edu.uniajc.vtf.utils.SessionManager;

import com.facebook.Session;

public class ContentActivity extends Activity {
/*	private UiLifecycleHelper coUiHelper;
    private Session.StatusCallback coCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onFacebookSessionStateChange(session, state, exception);
        }
    };
	protected void onFacebookSessionStateChange(Session session, SessionState state,Exception exception) {
	    if (state.isOpened()) {
	    	
	    } else if (state.isClosed()) {
	    	SessionManager se = new SessionManager(this);
	    	se.endSession();
	    	Intent intt = new Intent(this, ConfigLoginActivity.class);
	    	this.startActivity(intt);
	    	this.finish();
	    }	
	}*/
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		TextView loControl = (TextView) this.findViewById(R.id.textView1);
		SessionManager se = new SessionManager(this);
		loControl.setText("Hola:"  + se.getNames());
	}
	
	public void borrarSesion(View view){
		SessionManager se = new SessionManager(this);
		se.endSession();
		if(Session.getActiveSession() != null)
			Session.getActiveSession().closeAndClearTokenInformation();		
    	Intent intt = new Intent(this, ConfigLoginActivity.class);
    	this.startActivity(intt);
    	this.finish();		
	}
	
	

}
