package server;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

	
	public static String arrayDeconstructor(ArrayList<String> array) {
		if (array == null || array.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String item : array) {
			if (sb.length() > 0) {
				sb.append("|");
			}
			sb.append(item.trim());
		}
		return sb.toString();
	}
	
	public static ArrayList<String> superArrayReconstructor(String str){
		ArrayList<String> array = new ArrayList<>();

		if (str == null || str.isEmpty()) {
			return array; 
		}
		String[] parts = str.split("\\%"); 
		
		for (String part : parts) {
			if (!part.isEmpty()) { 
				array.add(part.trim()); 
			}
		}

		return array;
	}
	
	public static String superArrayDeconstructor(ArrayList<String> array) {
		if (array == null || array.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String item : array) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(item.trim());
		}
		return sb.toString();
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
                    agencias = agencias + "Nombre: " + rs.getString("nombre") + ", Dirección: " + rs.getString("direccion") + "|";
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
								+ ", Sponsor: " + sponsorNombre + "|";
					} else {
						clientes += "DNI: " + rs.getString("dni") + ", Nombre: " + rs.getString("nombre")
								+ ", Dirección: " + rs.getString("direccion") + ", Teléfono: " + rs.getString("telefono") + "|";
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
			String automoviles = "204 ";
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
							+ ", Garaje: " + garajeNombre + "|";
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
					reservas += "|";
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
		
		
		else if (code.equals("301")) {
			ArrayList<String> agencias = arrayReconstructor(request.substring(4));
			if (agencias.size() < 2) {
				return "801 Not enough data";
			}
			String nombre = agencias.get(0);
			String direccion = agencias.get(1);
			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO Agencia (nombre, direccion) VALUES (?, ?)")) {
				stmt.setString(1, nombre);
				stmt.setString(2, direccion);
				int rowsAffected = stmt.executeUpdate();
				if (rowsAffected > 0) {
					return "800";
				} else {
					return "801";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
        } 

		
		else if (code.equals("302")) {
			ArrayList<String> clientes = arrayReconstructor(request.substring(4));
			if (clientes.size() < 2) {
				return "801 Not enough data";
			}
			String nombre = clientes.get(0);
			String direccion = clientes.get(1);

			try (Connection cn = DBConnection.realizarConexion(); PreparedStatement stmt = cn.prepareStatement("INSERT INTO Garaje (nombre, ubicacion) VALUES (?, ?)")) {
				stmt.setString(1, nombre);
				stmt.setString(2, direccion);
				int rowsAffected = stmt.executeUpdate();
				if (rowsAffected > 0) {
					return "800";
				} else {
					return "801";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
        } 
		

		else if (code.equals("303")) {
			ArrayList<String> datos = arrayReconstructor(request.substring(4));
			if (datos.size() < 4) {
				return "801 Not enough data";
			}
			String dni = datos.get(0);
			String nombre = datos.get(1);
			String direccion = datos.get(2);
			String telefono = datos.get(3);
			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO Cliente (dni, nombre, direccion, telefono) VALUES (?, ?, ?, ?)")) {
				stmt.setString(1, dni);
				stmt.setString(2, nombre);
				stmt.setString(3, direccion);
				stmt.setString(4, telefono);
				int rowsAffected = stmt.executeUpdate();
				if (rowsAffected > 0) {
					return "800";
				} else {
					return "801";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
        } 
		

		else if (code.equals("304")) {
			ArrayList<String> datos = arrayReconstructor(request.substring(4));
			if (datos.size() < 5) {
				return "801 Not enough data";
			}
			String dni = datos.get(0);
			String nombre = datos.get(1);
			String direccion = datos.get(2);
			String telefono = datos.get(3);
			String sponsor = datos.get(4);
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT cliente_id FROM Cliente where dni = '" + sponsor + "'")) {
				while (rs.next()) {
					sponsor = rs.getString("cliente_id");
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
	
			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO Cliente (dni, nombre, direccion, telefono, sponsor_id) VALUES (?, ?, ?, ?, ?)")) {
				stmt.setString(1, dni);
				stmt.setString(2, nombre);
				stmt.setString(3, direccion);
				stmt.setString(4, telefono);
				stmt.setString(5, sponsor);
				int rowsAffected = stmt.executeUpdate();
				if (rowsAffected > 0) {
					return "800";
				} else {
					return "801";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
        } 
		

		else if (code.equals("305")) {
			ArrayList<String> datos = arrayReconstructor(request.substring(4));
			if (datos.size() < 5) {
				return "801 Not enough data";
			}
			String placa = datos.get(0);
			String modelo = datos.get(1);
			String color = datos.get(2);
			String marca = datos.get(3);
			String estado = datos.get(4);
			String garaje_nombre = datos.get(5);

			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO Automovil (placa, modelo, color, marca,estado, garaje_id) VALUES (?, ?, ?, ?, ?, ?)")) {
				stmt.setString(1, placa);
				stmt.setString(2, modelo);
				stmt.setString(3, color);
				stmt.setString(4, marca);
				stmt.setString(5, estado);
	
				String garajeId = "";
				try (Statement stmt2 = conn.createStatement(); ResultSet rs = stmt2.executeQuery("SELECT garaje_id FROM Garaje WHERE nombre = '" + garaje_nombre + "'")) {
					if (rs.next()) {
						garajeId = rs.getString("garaje_id");
					}
				}
	
				if (garajeId != null) {
					stmt.setString(6, garajeId);
					int rowsAffected = stmt.executeUpdate();
					if (rowsAffected > 0) {
						return "800";
					} else {
						return "801";
					}
				} else {
					return "801";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
        } 
		
		
		else if (code.equals("306")) {
			ArrayList<String> datos = arrayReconstructor(request.substring(4));
			if (datos.size() < 6) {
				return "801 Not enough data";
			}
			String clienteDNI = datos.get(0);
			String AgenciaNombre = datos.get(1);
			String fechaInicio = datos.get(2);
			String fechaFin = datos.get(3);
			String precio = datos.get(4);
			String entregadoString = datos.get(5);

					String entregado = "0";
		if ("No Entregado".equals(entregadoString)){
			entregado = "0";
		} 
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fechaInicioDate;
		Date fechaFinDate;
		
		try {
			fechaFinDate = new java.sql.Date(dateFormat.parse(fechaFin).getTime());
			fechaInicioDate = new java.sql.Date(dateFormat.parse(fechaInicio).getTime());
		} catch (ParseException e) {
			return "902 " + e.getMessage();
		}

	

		try (Connection conn = DBConnection.realizarConexion();
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO Reserva (cliente_id, agencia_id, fecha_inicio, fecha_fin, precio_total, entregado) VALUES (?, ?, ?, ?, ?, ?)")) {

			String clienteId = "";
			try (Statement stmt2 = conn.createStatement(); ResultSet rs = stmt2.executeQuery("SELECT cliente_id FROM Cliente WHERE dni = '" + clienteDNI + "'")) {
				if (rs.next()) {
					clienteId = rs.getString("cliente_id");
				}
			}

			String agenciaId = "";
			try (Statement stmt3 = conn.createStatement(); ResultSet rs2 = stmt3.executeQuery("SELECT agencia_id FROM Agencia WHERE nombre = '" + AgenciaNombre + "'")) {
				if (rs2.next()) {
					agenciaId = rs2.getString("agencia_id");
				}
			}

			stmt.setString(1, clienteId);
			stmt.setString(2, agenciaId);
			stmt.setTimestamp(3, new Timestamp(fechaInicioDate.getTime()));
			stmt.setTimestamp(4, new Timestamp(fechaFinDate.getTime()));
			stmt.setString(5, precio);
			stmt.setString(6, entregado);

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				return "800";
			} else {
				return "801";
			}
		} catch (SQLException e) {
			return "900 " + e.getMessage();
		} catch (ClassNotFoundException e) {
			return "901 " + e.getMessage();
		}
        } 
		
		
		else if (code.equals("307")) {
			ArrayList<String> datos = arrayReconstructor(request.substring(4));
			if (datos.size() < 4) {
				return "801 Not enough data";
			}
			String placa = datos.get(0);
			String reservaId = datos.get(1);
			String precio_Alquiler = datos.get(2);
			String litros_Inicial = datos.get(3);
			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO Reserva_Automovil (reserva_id, placa, precio_alquiler,litros_inicial) VALUES (?, ?, ?, ?) on duplicate key update precio_alquiler = ?, litros_inicial = ?")) {
			
				stmt.setString(1, reservaId);
				stmt.setString(2, placa);
				stmt.setString(3, precio_Alquiler);
				stmt.setString(4, litros_Inicial);
				stmt.setString(5, precio_Alquiler);
				stmt.setString(6, litros_Inicial);
				int rowsAffected = stmt.executeUpdate();
				if (rowsAffected > 0) {
					return "800";
				} else {
					return "801";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
	
        } 
		
		
		else {
            return "What? Didn't understand that";
        }

    }

}
