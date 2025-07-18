package test;

import java.sql.Connection;
import java.sql.SQLException;
import server.DBConnection;

public class DBConnTest {

    public static boolean test() {
        try {
            Connection connection = DBConnection.realizarConexion();
            if (connection != null) {
                connection.close();
                return true;
            } else {
                return false;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver de MySQL: " + e.getMessage());
            return false;

        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
            return false;

        }
    }

}
