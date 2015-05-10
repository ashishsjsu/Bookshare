package cmpe275Project.DAO;

import java.sql.*;
import java.util.Calendar;
import org.springframework.stereotype.Repository;
import cmpe275Project.Model.Login;
import cmpe275Project.Util.SqlConfig;



@Repository
public class LoginDaoImpl implements LoginDao {
	
	private Connection connection;
	PreparedStatement preparedStatement=null;
	ResultSet resultSet=null;
	
	public LoginDaoImpl() {
		connection = SqlConfig.getConnection();
	}
	
	@Override
	public String loginStudent(Login student) {
		
		try{
				PreparedStatement preparedStatement = connection.prepareStatement("Select * from login where email = ?");
				// Parameters start with 1
				preparedStatement.setString(1, student.getEmail());
				ResultSet result = preparedStatement.executeQuery();
				
				if(result.next())
				{
					String email = result.getString("email");
					String password = result.getString("password");
					if(password.equals(student.getPassword()))
					{
						return result.getString("email");
					}
				}
				
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		
		//wrong password
		return null;

	}
	
	public int registerNewStudent(Login login)
	{
		int flag = 0;
		
		if(!isRegisteredStudent(login.getEmail()))
		{
			try {
				
				preparedStatement = connection.prepareStatement("Insert into Login (email, password, lastlogin) values (?, ?, ?)");
				preparedStatement.setString(1, login.getEmail());
				preparedStatement.setString(2, login.getPassword());
				preparedStatement.setString(3, Calendar.MONTH+1 +"/" + Calendar.DAY_OF_MONTH+"/"+Calendar.YEAR);
				
				//returns true if query executed successfully
				flag = preparedStatement.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally{
				
			}//finally
			
		}
		
		return flag;
	}

		@Override
	public Login getStudentByEmail(String email) {

		try{
			PreparedStatement preparedStatement = connection.prepareStatement("Select * from login where email = ?");
			// Parameters start with 1
			preparedStatement.setString(1, email);
			ResultSet result = preparedStatement.executeQuery();
			
			if(result.next())
			{
				String loginEmail = result.getString("email");
				String loginPassword = result.getString("password");
				return new Login(loginEmail, loginPassword);
			}
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}
	
	//HELPER FUNCTIONS
	
	public boolean isRegisteredStudent(String email)
	{
			boolean flag = false;
			
			try {
					preparedStatement = connection.prepareStatement("Select * from login where email = ?");
					preparedStatement.setString(1, email);
					resultSet = preparedStatement.executeQuery();
					//returns false if resultset is empty
					flag = resultSet.first();
				
				} 
			catch (SQLException e) {
				e.printStackTrace();
			}finally{
				
			}
			
			return flag;
			
	}
		
}


