package cmpe275Project.services;

import cmpe275Project.Model.Login;

public interface LoginService {

	public Login findUserbyId(int id);
	public Login findByAccountName(String email);
	//public Login createNewUserAccount(Login login);
}