package co.edu.uniajc.vtf.utils;

public interface RestAsyncTaskListener {
	void onQuerySuccessful(String result);
	void onQueryError(String error);
}

