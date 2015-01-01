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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.utils.DirectionsEntity;

public class NavigationRouteActivity extends Activity {

	private ArrayList<DirectionsEntity> coDirectionPoints;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation_route);
		this.coDirectionPoints = this.getIntent().getParcelableArrayListExtra("routemap");
		this.loadData();
	}

	public void loadData(){
		try{
	    	ListView loList = (ListView)this.findViewById(R.id.lstRoute);
	    	ListMapRouteAdapter loAdapter = new ListMapRouteAdapter(this);
	    	loList.setAdapter(loAdapter);			
		}
		catch(Exception ex){
	
		}		
	}
	
    public class ListMapRouteAdapter extends BaseAdapter{
    	private Context coContext;
    	
		public ListMapRouteAdapter(Context pContext) {
			this.coContext = pContext;
		}

		@Override
		public int getCount() {
			return NavigationRouteActivity.this.coDirectionPoints.size();
		}

		@Override
		public Object getItem(int position) {
			return NavigationRouteActivity.this.coDirectionPoints.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View loView = null;
			if(convertView == null){
				LayoutInflater loInflater = (LayoutInflater)this.coContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				loView = loInflater.inflate(R.layout.list_route_item, null);				
			}
			else{
				loView = convertView;
			}
			
			DirectionsEntity loPoint = NavigationRouteActivity.this.coDirectionPoints.get(position);
			
			TextView loInstruction = (TextView)loView.findViewById(R.id.lblInstruction);
			loInstruction.setText(Html.fromHtml(loPoint.getInstructions()));
			
			TextView loDistance = (TextView)loView.findViewById(R.id.lblDistance);
			loDistance.setText(Html.fromHtml(loPoint.getDistance()));
			
			TextView loDuration = (TextView)loView.findViewById(R.id.lblDuration);
			loDuration.setText(Html.fromHtml(loPoint.getDuration()));
			
			return loView;
		}	
    }
}
