package server;

import java.sql.*;
import java.util.ArrayList;

public class RequestHandler {

	public static ArrayList<String> arrayReconstructor(String str){
		ArrayList<String> array = new ArrayList<>();

		if (str == null || str.isEmpty()) {
			return array; 
		}
		String[] parts = str.split("\\|"); 
		
		for (String part : parts) {
			if (!part.isEmpty()) { 
				array.add(part.trim()); 
			}
		}

		return array;
	}
    private String trimCode(String request) {
        if (request.length() >= 3) {
            return request.substring(0, 3);
        }
        return request;
    }

    public String processRequest(String request) {
        String code = trimCode(request);

        if (request == null || request.isEmpty()) {
            return "What? Didn't get that";
        } 
		

		else if (code.equals("069")) {
            return "069 Funny!";
        } 
		

		else if (code.equals("001")) {
            return "001 Echo: " + request;
        } 
		

		else if (request.equalsIgnoreCase("bye") || code.equals("999")) {
            return "999 Goodbye!";
		} 
		

		else if (code.equals("000") || request.equalsIgnoreCase("ping")) {
            return "000 Pong";	
        } 
		
		
		else if (code.equals("101")) {
            String agencias = "201 ";
            try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT nombre,direccion FROM Agencia")) {
                while (rs.next()) {
                    agencias = agencias + "Nombre: " + rs.getString("nombre") + ", Dirección: " + rs.getString("direccion") + "\n";
                }
            } catch (SQLException e) {
                return "900 " + e.getMessage();
            } catch (ClassNotFoundException e) {
                return "901 " + e.getMessage();
            }
            return agencias;
        } 
		
		
		else if (code.equals("102")) {
			String dnis = "202 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT dni FROM Cliente")) {
				while (rs.next()) {
					dnis = dnis + rs.getString("dni")+"|";
				}
            } catch (SQLException e) {
                return "900 " + e.getMessage();
            } catch (ClassNotFoundException e) {
                return "901 " + e.getMessage();
            }
			return dnis;
        } 

		
		else if (code.equals("103")) {
			String clientes = "203 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Cliente")) {
				while (rs.next()) {
					if (rs.getString("sponsor_id") != null) {
						String sponsorNombre = "";
						try (Connection conn2 = DBConnection.realizarConexion(); Statement stmt2 = conn2.createStatement(); ResultSet rs2 = stmt2.executeQuery("SELECT nombre FROM Cliente where sponsor_id = '" + rs.getString("sponsor_id") + "'")) {
							while (rs2.next()) {
								sponsorNombre = rs2.getString("nombre");
							}
						} catch (SQLException e) {
							return "900 " + e.getMessage();
						} catch (ClassNotFoundException e) {
							return "901 " + e.getMessage();
						}
	
						clientes += "DNI: " + rs.getString("dni") + ", Nombre: " + rs.getString("nombre")
								+ ", Dirección: " + rs.getString("direccion") + ", Teléfono: " + rs.getString("telefono")
								+ ", Sponsor: " + sponsorNombre + "\n";
					} else {
						clientes += "DNI: " + rs.getString("dni") + ", Nombre: " + rs.getString("nombre")
								+ ", Dirección: " + rs.getString("direccion") + ", Teléfono: " + rs.getString("telefono") + "\n";
					}
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
			return clientes;
        } 
		
		
		else if (code.equals("104")) {
			String automoviles = "204 108s";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Automovil")) {
				while (rs.next()) {
					String garajeNombre = "";
					try (Statement stmt2 = conn.createStatement(); ResultSet rs2 = stmt2.executeQuery("SELECT nombre FROM Garaje WHERE garaje_id = '" + rs.getString("garaje_id") + "'")) {
						if (rs2.next()) {
							garajeNombre = rs2.getString("nombre");
						}
					}
	
					automoviles += "Placa: " + rs.getString("placa") + ", Modelo: " + rs.getString("modelo")
							+ ", Color: " + rs.getString("color") + ", Marca: " + rs.getString("marca")
							+ ", Garaje: " + garajeNombre + "\n";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
			return automoviles;
        } 
		

		else if (code.equals("105")) {
			String nombresGarajes = "205 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT nombre FROM Garaje")) {
				while (rs.next()) {
					nombresGarajes = nombresGarajes + rs.getString("nombre") + "|";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
			return nombresGarajes;
        } 
		

		else if (code.equals("106")) {
			String reservas = "206 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Reserva")) {
				while (rs.next()) {
					String clienteNombre = "";
					try (Statement stmt2 = conn.createStatement(); ResultSet rs2 = stmt2.executeQuery("SELECT nombre FROM Cliente WHERE cliente_id = '" + rs.getString("cliente_id") + "'")) {
						if (rs2.next()) {
							clienteNombre = rs2.getString("nombre");
						}
					}
	
					String agenciaNombre = "";
					try (Statement stmt3 = conn.createStatement(); ResultSet rs3 = stmt3.executeQuery("SELECT nombre FROM Agencia WHERE agencia_id = '" + rs.getString("agencia_id") + "'")) {
						if (rs3.next()) {
							agenciaNombre = rs3.getString("nombre");
						}
					}
	
					reservas += "Cliente: " + clienteNombre + ", Agencia: " + agenciaNombre
							+ ", Fecha Inicio: " + rs.getTimestamp("fecha_inicio") + ", Fecha Fin: " + rs.getTimestamp("fecha_fin")
							+ ", Precio Total: " + rs.getString("precio_total") + ", Entregado: " + (rs.getInt("entregado") == 1 ? "Sí" : "No");
					try (Statement stmt4 = conn.createStatement(); ResultSet rs4 = stmt4.executeQuery("SELECT placa FROM Reserva_Automovil WHERE reserva_id = '" + rs.getString("reserva_id") + "'")) {
						if (rs4.next()) {
							reservas += ", Placa: " + rs4.getString("placa");
						}
					} catch (SQLException e) {
						return "900 " + e.getMessage();
					}
					reservas += "\n";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
			return reservas;
        } 
		
		
		else if (code.equals("107")) {
			String nombresAgencias = "207 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT nombre FROM Agencia")) {
				while (rs.next()) {
					nombresAgencias = nombresAgencias + rs.getString("nombre") + "|";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
			return nombresAgencias;        
		} 
		
		
		else if (code.equals("108")) {
			String placas = "208 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT placa FROM Automovil")) {
				while (rs.next()) {
					placas = placas + rs.getString("placa") + "|";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
			return placas;
        } 
		
		
		else if (code.equals("109")) {
			String ids = "209 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT reserva_id FROM Reserva")) {
				while (rs.next()) {
					ids = ids + rs.getString("reserva_id") + "|";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
			return ids;
        } 
		
		
		else {
            return "What? Didn't understand that";
        }

    }

}
