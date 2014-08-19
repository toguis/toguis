package co.edu.uniajc.vtf.security.model;

public class UserEntity {
	private String csEmail;
	private String csPassword;
	private String csNames;
	
	public String getEmail() {
		return csEmail;
	}
	
	public void setEmail(String pEmail) {
		this.csEmail = pEmail;
	}
	
	public String getPassword() {
		return csPassword;
	}
	
	public void setPassword(String pPassword) {
		this.csPassword = pPassword;
	}

	public String getNames() {
		return csNames;
	}

	public void setNames(String pNames) {
		this.csNames = pNames;
	}
	
	
}
