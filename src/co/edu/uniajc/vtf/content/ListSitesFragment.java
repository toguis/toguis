package co.edu.uniajc.vtf.content;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import co.edu.uniajc.vtf.R;
import co.edu.uniajc.vtf.content.interfaces.IListSites;
import co.edu.uniajc.vtf.content.model.PointOfInterestEntity;

public class ListSitesFragment extends Fragment implements IListSites {
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sites_list, container, false);              
    }
	
    @Override
    public void onActivityCreated(Bundle state) {
    	super.onActivityCreated(state);
    	PointOfInterestEntity loPoint1 = new PointOfInterestEntity();
    	loPoint1.setTitle("Gato del rio");
    	loPoint1.setDescription("El Gato del Río es una obra del pintor y escultor Hernando Tejada la cual donó a la ciudad de Cali y que fue instalada en la ribera del río tutelar de la ciudad, en el sector noroeste de la ciudad, conocido como Normandía. ");
    	loPoint1.setSiteType(1);
    	loPoint1.setVisited(true);
    	loPoint1.setFavorite(false);
    	
    	PointOfInterestEntity loPoint2 = new PointOfInterestEntity();
    	loPoint2.setTitle("Museo La tertulia");
    	loPoint2.setDescription("El Museo La Tertulia es uno de los Museos de Arte del Valle del Cauca, el primero de Arte Moderno y el que tiene la colección de obras en soporte de papel más importante del país, conformada por 1500 piezas.");
    	loPoint2.setSiteType(2);
    	loPoint2.setVisited(false);
    	loPoint2.setFavorite(true);   
    	
    	ArrayList<PointOfInterestEntity> points = new ArrayList<PointOfInterestEntity>();
    	
    	points.add(loPoint1);
    	points.add(loPoint2);
    	ListPointsAdapter adapter = new ListPointsAdapter(this.getActivity(), points);
    	this.setAdapter(adapter);
    	
    	
    }
    
    public static class ListPointsAdapter extends BaseAdapter{
    	private Context coContext;
    	private ArrayList<PointOfInterestEntity> coData; 
		public ListPointsAdapter(Context pContext, ArrayList<PointOfInterestEntity> pData) {
			this.coContext = pContext;
			this.coData = pData;
		}

		@Override
		public int getCount() {
			return coData.size();
		}

		@Override
		public Object getItem(int position) {
			return this.coData.get(position);
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
				loView = loInflater.inflate(R.layout.list_points_item, null);				
			}
			else{
				loView = convertView;
			}
			PointOfInterestEntity loPoint = this.coData.get(position);
			
			TextView loTitle = (TextView)loView.findViewById(R.id.lblPointTitle);
			loTitle.setText(loPoint.getTitle());
			
			TextView loDescription = (TextView)loView.findViewById(R.id.lblPointDesc);
			loDescription.setText(loPoint.getDescription());
			int liImage = 0;
			
			switch(loPoint.getSiteType()){
				case 1:
					liImage = R.drawable.monument48;
					break;
				case 2:
					liImage = R.drawable.museum48;
					break;
				case 3:
					liImage = R.drawable.hotel48;
					break;
				case 4:
					liImage = R.drawable.restaurant48;
					break;
				case 5:
					liImage = R.drawable.interest48;
					break;
				case 6:
					liImage = R.drawable.building48;
					break;
				case 7:
					liImage = R.drawable.transport48;
					break;
				case 8:
					liImage = R.drawable.events48;
					break;									
			}
			
			ImageView loImage = (ImageView)loView.findViewById(R.id.imgPoint);
			loImage.setImageResource(liImage);
			
			ImageView loVisited =  (ImageView)loView.findViewById(R.id.imgPointVisited);
			if(loPoint.isVisited()){		
				loVisited.setImageResource(R.drawable.visited16);	
			}
			else{
				loVisited.setImageResource(R.drawable.empty16);	
				
			}
			
			ImageView loFavorite =  (ImageView)loView.findViewById(R.id.imgPointFavorite);
			if(loPoint.isFavorite()){			
				loFavorite.setImageResource(R.drawable.favorite16);									
			}
			else{
				loFavorite.setImageResource(R.drawable.empty16);
			}

			return loView;
		}

	
    }

	@Override
	public void setAdapter(ListPointsAdapter pAdapter) {
    	ListView loList = (ListView)this.getView().findViewById(R.id.lstPoints);
    	loList.setAdapter(pAdapter);
		
	}

	@Override
	public ListPointsAdapter getAdapter() {
    	ListView loList = (ListView)this.getView().findViewById(R.id.lstPoints);
		return (ListPointsAdapter)loList.getAdapter();
	}
}
