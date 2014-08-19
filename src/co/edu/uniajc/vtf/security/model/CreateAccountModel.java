package co.edu.uniajc.vtf.security.model;

public class CreateAccountModel {
	public void createAccount(UserEntity pUser) throws Exception{
		try{
			System.out.println("Saving: " + pUser.getEmail());
		}
		catch(Exception e){
			throw new Exception("");
		}
	}
	
	public boolean userExists(String pEmail){
		return pEmail.equals("raven9t@yahoo.com");
	}
}
