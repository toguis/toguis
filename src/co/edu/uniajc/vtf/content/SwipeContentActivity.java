package co.edu.uniajc.vtf.content;



import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import co.edu.uniajc.vtf.R;

public class SwipeContentActivity extends FragmentActivity  implements ActionBar.TabListener {
	private AppSectionsPagerAdapter coAppSectionsPagerAdapter;
	private ViewPager coViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe_content);
		
		//get the pager
		this.coAppSectionsPagerAdapter = new AppSectionsPagerAdapter(this.getSupportFragmentManager());
		coViewPager = (ViewPager) findViewById(R.id.pagPagerContainer);
		coViewPager.setAdapter(coAppSectionsPagerAdapter);
		
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
		
		actionBar.addTab(actionBar.newTab().setText("Tab1").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Tab2").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Tab3").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Tab4").setTabListener(this));
	}
	
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			switch(arg0){
			case 0:
				return new ListSitesFragment();		
			case 1:
				return new MapSitesFragmenty();
			case 2:
				return new RALauncherFragment();
			case 3:
				return new SettingsFragment();
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
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
