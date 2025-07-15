package server;

import java.sql.*;

public class DBConnection {

	public static Connection realizarConexion() throws ClassNotFoundException, SQLException {
        String url, user, password;
        Class.forName("com.mysql.cj.jdbc.Driver");
        url = "jdbc:mysql://localhost:3306/tu_base_de_datos"; // Replace with your actual database name
        user = "---"; // Replace with your actual username
        password = "------"; // Replace with your actual password
        return DriverManager.getConnection(url, user, password);
    }   
}