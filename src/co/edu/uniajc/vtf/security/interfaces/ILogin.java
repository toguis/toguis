/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.security.interfaces;

public interface ILogin {
	String getEmail();
	void setEmail(String pMail);
	
	String getPassword();
	void setPassword(String pPassword);
}
