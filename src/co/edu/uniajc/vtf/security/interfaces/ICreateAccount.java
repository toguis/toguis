package co.edu.uniajc.vtf.security.interfaces;

public interface ICreateAccount {
	String getEmail();
	void setEmail(String pMail);
	
	String getPassword();
	void setPassword(String pPassword);
	
	String getRepeatPassword();
	void setRepeatPassword(String pPassword);
	
	String getNames();
	void setNames(String pNames);
	
	boolean getMale();
	void setMale(boolean pState);
	
	boolean getFemale();
	void setFemale(boolean pState);	
}
