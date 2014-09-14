package co.edu.uniajc.vtf.security.model;

public interface LoginModelListener {
	void onGetUser(UserEntity pUser);
	void onError(String pData);
}
