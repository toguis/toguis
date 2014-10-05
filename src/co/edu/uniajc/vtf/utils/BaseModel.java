package co.edu.uniajc.vtf.utils;

import java.util.ArrayList;

public abstract class BaseModel implements RestAsyncTaskListener {
	protected String csMethod;
	protected String csBaseUrl;
	protected ArrayList<ModelListener> coModelListener;
	
	public void addModelListener(ModelListener pModelListener){
		this.coModelListener.add(pModelListener);
	}
	
}
