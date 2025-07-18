package server;

import java.sql.*;

public class DBConnection {

	public static Connection realizarConexion() throws ClassNotFoundException, SQLException {
        String url, user, password;
        Class.forName("com.mysql.cj.jdbc.Driver");
        url = "jdbc:mysql://localhost:3306/alquiler_automoviles"; // Replace with your actual database name
        user = "root"; // Replace with your actual username
        password = "60529950"; // Replace with your actual password
        return DriverManager.getConnection(url, user, password);
    }   

}