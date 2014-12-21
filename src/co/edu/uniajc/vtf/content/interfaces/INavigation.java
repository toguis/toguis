package co.edu.uniajc.vtf.content.interfaces;

import java.util.ArrayList;

import co.edu.uniajc.vtf.utils.DirectionsEntity;

public interface INavigation {
	void handleGetDirectionsResult(ArrayList<DirectionsEntity> PdirectionPoints);
	void onError();
}
