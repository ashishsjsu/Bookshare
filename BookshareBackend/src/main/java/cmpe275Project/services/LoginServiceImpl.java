package cmpe275Project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmpe275Project.Model.Login;
import cmpe275Project.DAO.LoginDao;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginDao  loginDao;
	
	@Override
	public Login findUserbyId(int id) {
		return null;
	}

	@Override
	public Login findByAccountName(String email) {
		
		return loginDao.getStudentByEmail(email);		
	}

}
