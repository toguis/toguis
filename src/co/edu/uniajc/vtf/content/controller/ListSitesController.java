package co.edu.uniajc.vtf.content.controller;

import co.edu.uniajc.vtf.content.interfaces.IListSites;
import co.edu.uniajc.vtf.security.model.CreateAccountModel;
import co.edu.uniajc.vtf.utils.ModelListener;

public class ListSitesController implements ModelListener{

	private IListSites coView;
	private CreateAccountModel coModel;
	
	@Override
	public void onGetData(Object pData, int type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Object pData, int type) {
		// TODO Auto-generated method stub
		
	}
	
}
