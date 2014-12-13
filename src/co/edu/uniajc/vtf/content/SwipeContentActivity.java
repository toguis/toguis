package co.edu.uniajc.vtf.content;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.ListSitesFragment.LoadActions;
import co.edu.uniajc.vtf.security.ConfigLoginActivity;
import co.edu.uniajc.vtf.security.model.LogoutListener;
import co.edu.uniajc.vtf.utils.ResourcesManager;
import co.edu.uniajc.vtf.utils.SessionManager;

import com.facebook.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.plus.Plus;

public class SwipeContentActivity extends FragmentActivity  implements  ActionBar.TabListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LogoutListener {
	private AppSectionsPagerAdapter coAppSectionsPagerAdapter;
	private ViewPager coViewPager;
	
	private static final int RC_SIGN_IN = 0;
    private GoogleApiClient coApiClient;
    private boolean coIntentInProgress;
    private ConnectionResult coConnectionResult;
    private boolean coSignInClicked;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe_content);
		
		//get the pager
		this.coAppSectionsPagerAdapter = new AppSectionsPagerAdapter(this.getSupportFragmentManager(), this);
		this.coViewPager = (ViewPager) findViewById(R.id.pagPagerContainer);
		this.coViewPager.setAdapter(coAppSectionsPagerAdapter);

		//get the action bar
		final ActionBar actionBar = this.getActionBar();
		coViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	//SwipeContentActivity.this.loadSitesList(position);
                actionBar.setSelectedNavigationItem(position);
            }
        });	
		
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ResourcesManager loResource = new ResourcesManager(this);		
		actionBar.addTab(actionBar.newTab().setText(loResource.getStringResource(R.string.swipe_content_tab_site)).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(loResource.getStringResource(R.string.swipe_content_tab_map)).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(loResource.getStringResource(R.string.swipe_content_tab_ar)).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(loResource.getStringResource(R.string.swipe_content_tab_settings)).setTabListener(this));
		
		//get the google api client 
		this.coApiClient = new GoogleApiClient.Builder(this)
			.addConnectionCallbacks(this)
			.addOnConnectionFailedListener(this)		
			.addApi(Plus.API)
			.addScope(Plus.SCOPE_PLUS_PROFILE)
			.build();		
		
	}
	
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        private Fragment mCurrentFragment;

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }
        
		private FragmentActivity coFragment;
		public AppSectionsPagerAdapter(FragmentManager fm, FragmentActivity fragment) {
			super(fm);
			this.coFragment = fragment;
		}

		@Override
		public Fragment getItem(int arg0) {
			switch(arg0){
			case 0:
				return new ListSitesFragment();		
			case 1:
				return new MapSitesFragment();
			case 2:
				return new RALauncherFragment();
			case 3:
				SettingsFragment loSettingFrament = new SettingsFragment();
				loSettingFrament.AddLogoutListener((LogoutListener)this.coFragment);
				return loSettingFrament;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 4;
		}
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		this.coViewPager.setCurrentItem(tab.getPosition());		
		this.loadSitesList(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!coIntentInProgress) {
			coConnectionResult = result;	
			if (coSignInClicked) {
				resolveSignInError();
			}
		}	
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int cause) {
		this.coApiClient.connect();
		
	}
	
	public void deleteSesion(){
		SessionManager loSession = new SessionManager(this);
		switch(loSession.getSessionType()){
			case SessionManager.FACEBOOK_SESSION:
				if(Session.getActiveSession() != null)
					Session.getActiveSession().closeAndClearTokenInformation();
				break;
			case SessionManager.GOOGLE_SESSION:	
				if(coApiClient.isConnected()){
					Plus.AccountApi.clearDefaultAccount(coApiClient);
					coApiClient.disconnect();					
				}
				
		}
		
		loSession.endSession();
		//implements inside of a controller
    	Intent loIntent = new Intent(this, ConfigLoginActivity.class);
    	this.startActivity(loIntent);
    	this.finish();		
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.coApiClient.connect();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	    if (this.coApiClient.isConnected()) {
	    	this.coApiClient.disconnect();
	    }
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    
		if (requestCode == RC_SIGN_IN) {
		    if (resultCode != RESULT_OK) {
		        coSignInClicked = false;
		    }			
			coIntentInProgress = false;
			if (!this.coApiClient.isConnecting()) {
				this.coApiClient.connect();
			}
		}	    
	}
	
	private void resolveSignInError() {
		if (coConnectionResult.hasResolution()) {
			try {
				coIntentInProgress = true;
				startIntentSenderForResult(coConnectionResult.getResolution().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
			} 
			catch (SendIntentException e) {
				coIntentInProgress = false;
				this.coApiClient.connect();
			}
		}
	}

	@Override
	public void logout() {
		this.deleteSesion();
	}

	private void loadSitesList(int pPosition){
    	//Load the fragment list on page select
    	if(pPosition == 0){
    		//this part is a selective loading for the list site fragment, because we need load when the user
    		//swipe the tab and that user select the tab 0
       		String lsTag = "android:switcher:" + SwipeContentActivity.this.coViewPager.getId() + ":0";
    		ListSitesFragment loListSitesFragment = (ListSitesFragment)this.getSupportFragmentManager().findFragmentByTag(lsTag);
    		if(loListSitesFragment != null)
    			loListSitesFragment.loadList(LoadActions.LOAD_CACHE);	        	
    	}
    	else {
       		String lsTag = "android:switcher:" + SwipeContentActivity.this.coViewPager.getId() + ":0";
    		ListSitesFragment loListSitesFragment = (ListSitesFragment)this.getSupportFragmentManager().findFragmentByTag(lsTag);
    		if(loListSitesFragment != null)
    			loListSitesFragment.unloadList();	                 		
    	}		
    	
	}
	
}
