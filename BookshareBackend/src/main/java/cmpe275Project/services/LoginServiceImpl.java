package cmpe275Project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmpe275Project.Model.Login;
import cmpe275Project.DAO.LoginDao;
import cmpe275Project.DAO.LoginDaoImpl;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginDao loginDao;
	
	@Override
	public Login findUserbyId(int id) {
		return null;
	}

	@Override
	public Login findByAccountName(String email) {
		
		if(loginDao == null)
		{
			loginDao = new LoginDaoImpl();
		}
		return loginDao.getStudentByEmail(email);		
	}

}
