package cmpe275Project.Util;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class SqlConfig {
	private static Connection connection = null;
    
    public static Connection getConnection() {
    	if (connection != null)
            return connection;
        else {
        	// MySQL Database Connection.
            try {
               /* Properties prop = new Properties();
                InputStream inputStream = SqlConfig.class.getClassLoader().getResourceAsStream("/db.properties");
                prop.load(inputStream);
                String driver = prop.getProperty("JDBC_Driver");
                String url = prop.getProperty("DB_URl");
                String user = prop.getProperty("username");
                String password = prop.getProperty("password");
                Class.forName(driver);*/
                
                String url = "jdbc:mysql://localhost:3306/BookshareDB";
                String user = "root";
                String password = "root";
                
                try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                connection = DriverManager.getConnection(url, user, password);
            
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            return connection;
        }
    }
}
