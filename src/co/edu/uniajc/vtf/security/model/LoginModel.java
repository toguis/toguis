package co.edu.uniajc.vtf.security.model;


public class LoginModel {

	public boolean CheckCredentials(UserEntity pUser){
		boolean lboResult = false;
		if(pUser.getEmail().toLowerCase().equals("email@email.com") && pUser.getPassword().equals("c22b5f9178342609428d6f51b2c5af4c0bde6a42")){
			lboResult = true;
		}
		return lboResult;

	}
	
	public UserEntity getUser(String pEmail){
		UserEntity loUser = new UserEntity();
		loUser.setEmail(pEmail);
		loUser.setPassword("c22b5f9178342609428d6f51b2c5af4c0bde6a42");		
		loUser.setNames("Carlos Morante");
		return loUser;
	}

}
