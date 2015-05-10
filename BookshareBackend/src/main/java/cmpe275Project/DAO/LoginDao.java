package cmpe275Project.DAO;

import cmpe275Project.Model.Login;

public interface LoginDao {

	public int registerNewStudent(Login student);
	public String loginStudent(Login student);
	public Login getStudentByEmail(String email);

}
