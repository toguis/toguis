package co.edu.uniajc.vtf.utils;

import android.content.Context;
import android.content.res.Resources;

public class ResourcesManager {
	private Context context;
		
	public ResourcesManager(Context pContext) {
		this.context = pContext;
	}

	public String getStringResource(int pId){
		Resources loResource = this.context.getResources();
		return loResource.getString(pId);
	}
}
