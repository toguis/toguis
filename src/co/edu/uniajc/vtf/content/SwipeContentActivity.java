/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.content;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.ListSitesFragment.LoadActions;
import co.edu.uniajc.vtf.content.interfaces.ILoadExtenalDataFromList;
import co.edu.uniajc.vtf.content.interfaces.ILoadExtenalDataFromMap;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;
import co.edu.uniajc.vtf.security.ConfigLoginActivity;
import co.edu.uniajc.vtf.security.model.LogoutListener;
import co.edu.uniajc.vtf.utils.SessionManager;

import com.facebook.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

public class SwipeContentActivity extends FragmentActivity  implements  
		ActionBar.TabListener,
		GoogleApiClient.ConnectionCallbacks, 
		GoogleApiClient.OnConnectionFailedListener,
		LogoutListener,
		ILoadExtenalDataFromMap,
		ILoadExtenalDataFromList{
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
                actionBar.setSelectedNavigationItem(position);
            }
        });	
		
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.addTab(actionBar.newTab()
				 .setTabListener(this)
				 .setIcon(R.drawable.list48));		
		actionBar.addTab(actionBar.newTab()
				 .setTabListener(this)
				 .setIcon(R.drawable.map48));
		actionBar.addTab(actionBar.newTab()
				 .setTabListener(this)
				 .setIcon(R.drawable.ra48));
		actionBar.addTab(actionBar.newTab()
				 .setTabListener(this)
				 .setIcon(R.drawable.options48));
		
		//get the google api client 
		this.coApiClient = new GoogleApiClient.Builder(this)
			.addConnectionCallbacks(this)
			.addOnConnectionFailedListener(this)		
			.addApi(Plus.API)
			.addScope(Plus.SCOPE_PLUS_PROFILE)
			.build();			
	}
	
	public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
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
				ListSitesFragment loSites = new ListSitesFragment();	
				loSites.addLoadListeners(SwipeContentActivity.this);
				return loSites;		
			case 1:
				MapSitesFragment loMap = new MapSitesFragment();
				loMap.addLoadListeners(SwipeContentActivity.this);
				return loMap;
			case 2:
				return new ARLauncherFragment();
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
				
		//implements inside of a controller
    	Intent loIntent = new Intent(this, ConfigLoginActivity.class);
		if(loSession.getSessionType() == SessionManager.FACEBOOK_SESSION){
			loIntent.putExtra("no_fb_auth", 1);
		}
		loSession.endSession();
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

	@Override
	public void loadDataFromList(ArrayList<PointOfInterestEntity> pPoints, Location pLastLocation) {
		String lsTag = "android:switcher:" + SwipeContentActivity.this.coViewPager.getId() + ":1";
		MapSitesFragment loMapSitesFragment = (MapSitesFragment)this.getSupportFragmentManager().findFragmentByTag(lsTag);
		if(loMapSitesFragment != null){
			loMapSitesFragment.setLastLocation(pLastLocation);
			loMapSitesFragment.setAdapterData(pPoints, false);
		}
	}

	@Override
	public void loadDataFromMap(ArrayList<PointOfInterestEntity> pPoints, Location pLastLocation) {
   		String lsTag = "android:switcher:" + SwipeContentActivity.this.coViewPager.getId() + ":0";
		ListSitesFragment loListSitesFragment = (ListSitesFragment)this.getSupportFragmentManager().findFragmentByTag(lsTag);
		if(loListSitesFragment != null){
			loListSitesFragment.setLastLocation(pLastLocation);
			loListSitesFragment.setAdapterData(pPoints, false);
		}			 	
	}
}
