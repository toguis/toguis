package co.edu.uniajc.vtf.utils;


public interface ModelListener {
	void onGetData(Object pData, int type);
	void onError(Object pData, int type);
}
