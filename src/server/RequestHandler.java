package server;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT agencia_id, nombre,direccion FROM Agencia")) {
                while (rs.next()) {
                    agencias = agencias + "|" +rs.getString("agencia_id")+ "|" + rs.getString("nombre") + "|" + rs.getString("direccion") + "|%";
                }
            } catch (SQLException e) {
                return "900 " + e.getMessage();
            } catch (ClassNotFoundException e) {
                return "901 " + e.getMessage();
            }
            return agencias;
        } 
		
		
		else if (code.equals("102")) {
			String garajes = "202 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT garaje_id, nombre, ubicacion FROM Garaje")) {
				while (rs.next()) {
					garajes = garajes+"|" + rs.getString("garaje_id")+"|"+rs.getString("nombre")+"|"+rs.getString("ubicacion")+"|%";
				}
            } catch (SQLException e) {
                return "900 " + e.getMessage();
            } catch (ClassNotFoundException e) {
                return "901 " + e.getMessage();
            }
			return garajes;
        } 

		
		else if (code.equals("103")) {
			String clientes = "203 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Cliente")) {
				while (rs.next()) {
					if (rs.getString("sponsor_id") != null) {
						String sponsorNombre = "Sin Sponsor";
						try (Connection conn2 = DBConnection.realizarConexion(); Statement stmt2 = conn2.createStatement(); ResultSet rs2 = stmt2.executeQuery("SELECT nombre FROM Cliente where cliente_id = '" + rs.getString("sponsor_id") + "'")) {
							while (rs2.next()) {
								sponsorNombre = rs2.getString("nombre");
							}
						} catch (SQLException e) {
							return "900 " + e.getMessage();
						} catch (ClassNotFoundException e) {
							return "901 " + e.getMessage();
						}
	
						clientes += "|" + rs.getString("dni") + "|" + rs.getString("nombre")
								+ "|" + rs.getString("direccion") + "|" + rs.getString("telefono")
								+ "|" + sponsorNombre + "|%";
					} else {
						clientes += "|" + rs.getString("dni") + "|" + rs.getString("nombre")
								+ "|" + rs.getString("direccion") + "|" + rs.getString("telefono") + "|6|%";
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
	
					automoviles += "|" + rs.getString("placa") + "|" + rs.getString("modelo")
							+ "|" + rs.getString("color") + "|" + rs.getString("marca") + "|" + rs.getString("estado")
							+ "|" + garajeNombre + "|%";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
			return automoviles;
        } 
		
		
		else if (code.equals("105")) {
			String reservas = "205 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Reserva")) {
				
				while (rs.next()) {
					String clienteNombre = "No Asignado Nombre";
					try (Statement stmt2 = conn.createStatement(); ResultSet rs2 = stmt2.executeQuery("SELECT nombre FROM Cliente WHERE cliente_id = '" + rs.getString("cliente_id") + "'")) {
						if (rs2.next()) {
							clienteNombre = rs2.getString("nombre");
						}
					}
	
					String agenciaNombre = "No Asignada Agencia";
					try (Statement stmt3 = conn.createStatement(); ResultSet rs3 = stmt3.executeQuery("SELECT nombre FROM Agencia WHERE agencia_id = '" + rs.getString("agencia_id") + "'")) {
						if (rs3.next()) {
							agenciaNombre = rs3.getString("nombre");
						}
					}

					String placas = "No Asignado Placas";
					try (Statement stmt3 = conn.createStatement(); ResultSet rs3 = stmt3.executeQuery("SELECT placa FROM Reserva_Automovil WHERE reserva_id = '" + rs.getString("reserva_id") + "'")) {
						if (rs3.next()) {
							agenciaNombre = rs3.getString("nombre");
						}
					}
					
					reservas += "|" + rs.getString("reserva_id")+ "|"+placas+ "|"+ clienteNombre + "|"+ agenciaNombre + "|"+ rs.getTimestamp("fecha_inicio") + "|"+ rs.getTimestamp("fecha_fin") + "|"+ rs.getString("precio_total") + "|"+ (rs.getInt("entregado") == 1 ? "Sí" : "No")+ "|%";

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
		
		else if (code.equals("106")) {
			ArrayList<String> data = arrayReconstructor(request.substring(4));
			            String agencias = "206 ";
            try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT agencia_id, nombre,direccion FROM Agencia where nombre="+ "'" + data.get(0) + "'")) {
                while (rs.next()) {
                    agencias = agencias + "|" +rs.getString("agencia_id")+ "|" + rs.getString("nombre") + "|" + rs.getString("direccion") + "|%";
                }
            } catch (SQLException e) {
                return "900 " + e.getMessage();
            } catch (ClassNotFoundException e) {
                return "901 " + e.getMessage();
            }
            return agencias;
        } 
		
		
		else if (code.equals("107")) {
			ArrayList<String> data = arrayReconstructor(request.substring(4));
			String agencias = "207 ";
            try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT agencia_id, nombre,direccion FROM Agencia where direccion="+ "'" + data.get(0) + "'")) {
                while (rs.next()) {
                    agencias = agencias + "|" +rs.getString("agencia_id")+ "|" + rs.getString("nombre") + "|" + rs.getString("direccion") + "|%";
                }
            } catch (SQLException e) {
                return "900 " + e.getMessage();
            } catch (ClassNotFoundException e) {
                return "901 " + e.getMessage();
            }
            return agencias;
        } 
		
				
		else if (code.equals("108")) {
			ArrayList<String> data = arrayReconstructor(request.substring(4));
			            String agencias = "208 ";
            try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Garaje where nombre="+ "'" + data.get(0) + "'")) {
                while (rs.next()) {
                    agencias = agencias + "|" +rs.getString("garaje_id")+ "|" + rs.getString("nombre") + "|" + rs.getString("ubicacion") + "|%";
                }
            } catch (SQLException e) {
                return "900 " + e.getMessage();
            } catch (ClassNotFoundException e) {
                return "901 " + e.getMessage();
            }
            return agencias;
        } 
		
		
		else if (code.equals("109")) {
			ArrayList<String> data = arrayReconstructor(request.substring(4));
			String agencias = "209 ";
            try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Garaje where ubicacion="+ "'" + data.get(0) + "'")) {
                while (rs.next()) {
                    agencias = agencias + "|" +rs.getString("garaje_id")+ "|" + rs.getString("nombre") + "|" + rs.getString("ubicacion") + "|%";
                }
            } catch (SQLException e) {
                return "900 " + e.getMessage();
            } catch (ClassNotFoundException e) {
                return "901 " + e.getMessage();
            }
            return agencias;
        } 
		
		
		
		else if (code.equals("110")) {
			ArrayList<String> data = arrayReconstructor(request.substring(4));
			String automoviles = "210 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Automovil where placa = '" + data.get(0) + "'")) {
				while (rs.next()) {
					String garajeNombre = "";
					try (Statement stmt2 = conn.createStatement(); ResultSet rs2 = stmt2.executeQuery("SELECT nombre FROM Garaje WHERE garaje_id = '" + rs.getString("garaje_id") + "'")) {
						if (rs2.next()) {
							garajeNombre = rs2.getString("nombre");
						}
					}
	
					automoviles += "|" + rs.getString("placa") + "|" + rs.getString("modelo")
							+ "|" + rs.getString("color") + "|" + rs.getString("marca") + "|" + rs.getString("estado")
							+ "|" + garajeNombre + "|%";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
			return automoviles;
        } 
		
		
		else if (code.equals("111")) {
			ArrayList<String> data = arrayReconstructor(request.substring(4));
			String automoviles = "211 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Automovil where marca = '" + data.get(0) + "'")) {
				while (rs.next()) {
					String garajeNombre = "";
					try (Statement stmt2 = conn.createStatement(); ResultSet rs2 = stmt2.executeQuery("SELECT nombre FROM Garaje WHERE garaje_id = '" + rs.getString("garaje_id") + "'")) {
						if (rs2.next()) {
							garajeNombre = rs2.getString("nombre");
						}
					}
	
					automoviles += "|" + rs.getString("placa") + "|" + rs.getString("modelo")
							+ "|" + rs.getString("color") + "|" + rs.getString("marca") + "|" + rs.getString("estado")
							+ "|" + garajeNombre + "|%";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
			return automoviles;
        } 
		
		
		else if (code.equals("112")) {
			ArrayList<String> data = arrayReconstructor(request.substring(4));
			String clientes = "212 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Cliente where dni = '" + data.get(0) + "'")) {
				while (rs.next()) {
					if (rs.getString("sponsor_id") != null) {
						String sponsorNombre = "Sin Sponsor";
						try (Connection conn2 = DBConnection.realizarConexion(); Statement stmt2 = conn2.createStatement(); ResultSet rs2 = stmt2.executeQuery("SELECT nombre FROM Cliente where cliente_id = '" + rs.getString("sponsor_id") + "'")) {
							while (rs2.next()) {
								sponsorNombre = rs2.getString("nombre");
							}
						} catch (SQLException e) {
							return "900 " + e.getMessage();
						} catch (ClassNotFoundException e) {
							return "901 " + e.getMessage();
						}
	
						clientes += "|" + rs.getString("dni") + "|" + rs.getString("nombre")
								+ "|" + rs.getString("direccion") + "|" + rs.getString("telefono")
								+ "|" + sponsorNombre + "|%";
					} else {
						clientes += "|" + rs.getString("dni") + "|" + rs.getString("nombre")
								+ "|" + rs.getString("direccion") + "|" + rs.getString("telefono") + "|6|%";
					}
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
			return clientes;
        } 
		
		else if (code.equals("113")) {
			ArrayList<String> data = arrayReconstructor(request.substring(4));
			String sponsor_id;
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT cliente_id FROM Cliente where dni = '" + data.get(0) + "'")) {
				if (rs.next()) {
					sponsor_id = rs.getString("cliente_id");
				} else {
					return "801 Sponsor not found";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
			String clientes = "213 ";
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Cliente where sponsor_id = '" + sponsor_id + "'")) {
				while (rs.next()) {
					if (rs.getString("sponsor_id") != null) {
						String sponsorNombre = "Sin Sponsor";
						try (Connection conn2 = DBConnection.realizarConexion(); Statement stmt2 = conn2.createStatement(); ResultSet rs2 = stmt2.executeQuery("SELECT nombre FROM Cliente where cliente_id = '" + rs.getString("sponsor_id") + "'")) {
							while (rs2.next()) {
								sponsorNombre = rs2.getString("nombre");
							}
						} catch (SQLException e) {
							return "900 " + e.getMessage();
						} catch (ClassNotFoundException e) {
							return "901 " + e.getMessage();
						}
	
						clientes += "|" + rs.getString("dni") + "|" + rs.getString("nombre")
								+ "|" + rs.getString("direccion") + "|" + rs.getString("telefono")
								+ "|" + sponsorNombre + "|%";
					} else {
						clientes += "|" + rs.getString("dni") + "|" + rs.getString("nombre")
								+ "|" + rs.getString("direccion") + "|" + rs.getString("telefono") + "|6|%";
					}
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
			return clientes;
        } 
		
		
		else if (code.equals("114")) {
			ArrayList<String> data = arrayReconstructor(request.substring(4));

			if (data.size() < 1) {
				return "801 Not enough data";
			}
			String DNI = data.get(0);

			if (DNI == null || DNI.isEmpty()) {
				return "801 Not enough data";
			}
			String reservas = "214 ";

			try (Connection conn = DBConnection.realizarConexion()) {
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Reserva WHERE cliente_id = ?");

				try(Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT cliente_id FROM Cliente WHERE dni = '" + DNI + "'")) {
					if (rs.next()) {
						pstmt.setString(1, rs.getString("cliente_id"));
					} else {
						return "801 Cliente not found";
					}
				} catch (SQLException e) {
					return "900 " + e.getMessage();
				} 

				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					String clienteNombre = "No Asignado Nombre";
					try (Statement stmt2 = conn.createStatement(); ResultSet rs2 = stmt2.executeQuery("SELECT nombre FROM Cliente WHERE cliente_id = '" + rs.getString("cliente_id") + "'")) {
						if (rs2.next()) {
							clienteNombre = rs2.getString("nombre");
						}
					}
	
					String agenciaNombre = "No Asignada Agencia";
					try (Statement stmt3 = conn.createStatement(); ResultSet rs3 = stmt3.executeQuery("SELECT nombre FROM Agencia WHERE agencia_id = '" + rs.getString("agencia_id") + "'")) {
						if (rs3.next()) {
							agenciaNombre = rs3.getString("nombre");
						}
					}

					String placas = "No Asignado Placas";
					try (Statement stmt3 = conn.createStatement(); ResultSet rs3 = stmt3.executeQuery("SELECT placa FROM Reserva_Automovil WHERE reserva_id = '" + rs.getString("reserva_id") + "'")) {
						if (rs3.next()) {
							agenciaNombre = rs3.getString("nombre");
						}
					}
					
					reservas += "|" + rs.getString("reserva_id")+ "|"+placas+ "|"+ clienteNombre + "|"+ agenciaNombre + "|"+ rs.getTimestamp("fecha_inicio") + "|"+ rs.getTimestamp("fecha_fin") + "|"+ rs.getString("precio_total") + "|"+ (rs.getInt("entregado") == 1 ? "Sí" : "No")+ "|%";

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


		
		else if (code.equals("115")) {
			ArrayList<String> data = arrayReconstructor(request.substring(4));

			if (data.size() < 1) {
				return "801 Not enough data";
			}
			String Fecha = data.get(0);

			if (Fecha == null || Fecha.isEmpty()) {
				return "801 Not enough data";
			}
			String reservas = "215 ";

			try (Connection conn = DBConnection.realizarConexion()) {
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Reserva WHERE fecha_inicio = ?");


				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date fechaInicioDate;

				try {
					fechaInicioDate = new java.sql.Date(dateFormat.parse(Fecha).getTime());
				} catch (ParseException e) {
					return "902 " + e.getMessage();
				}
		
				pstmt.setTimestamp(1, new Timestamp(fechaInicioDate.getTime()));

				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					String clienteNombre = "No Asignado Nombre";
					try (Statement stmt2 = conn.createStatement(); ResultSet rs2 = stmt2.executeQuery("SELECT nombre FROM Cliente WHERE cliente_id = '" + rs.getString("cliente_id") + "'")) {
						if (rs2.next()) {
							clienteNombre = rs2.getString("nombre");
						}
					}
	
					String agenciaNombre = "No Asignada Agencia";
					try (Statement stmt3 = conn.createStatement(); ResultSet rs3 = stmt3.executeQuery("SELECT nombre FROM Agencia WHERE agencia_id = '" + rs.getString("agencia_id") + "'")) {
						if (rs3.next()) {
							agenciaNombre = rs3.getString("nombre");
						}
					}

					String placas = "No Asignado Placas";
					try (Statement stmt3 = conn.createStatement(); ResultSet rs3 = stmt3.executeQuery("SELECT placa FROM Reserva_Automovil WHERE reserva_id = '" + rs.getString("reserva_id") + "'")) {
						if (rs3.next()) {
							agenciaNombre = rs3.getString("nombre");
						}
					}
					
					reservas += "|" + rs.getString("reserva_id")+ "|"+placas+ "|"+ clienteNombre + "|"+ agenciaNombre + "|"+ rs.getTimestamp("fecha_inicio") + "|"+ rs.getTimestamp("fecha_fin") + "|"+ rs.getString("precio_total") + "|"+ (rs.getInt("entregado") == 1 ? "Sí" : "No")+ "|%";

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

		
		
		else if (code.equals("116")) {
			ArrayList<String> data = arrayReconstructor(request.substring(4));

			if (data.size() < 1) {
				return "801 Not enough data";
			}
			String Agencia = data.get(0);

			if (Agencia == null || Agencia.isEmpty()) {
				return "801 Not enough data";
			}
			String reservas = "216 ";

			try (Connection conn = DBConnection.realizarConexion()) {
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Reserva WHERE agencia_id = ?");

				try(Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT agencia_id FROM Agencia WHERE nombre = '" + Agencia + "'")) {
					if (rs.next()) {
						pstmt.setString(1, rs.getString("agencia_id"));
					} else {
						return "801 Cliente not found";
					}
				} catch (SQLException e) {
					return "900 " + e.getMessage();
				} 

				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					String clienteNombre = "No Asignado Nombre";
					try (Statement stmt2 = conn.createStatement(); ResultSet rs2 = stmt2.executeQuery("SELECT nombre FROM Cliente WHERE cliente_id = '" + rs.getString("cliente_id") + "'")) {
						if (rs2.next()) {
							clienteNombre = rs2.getString("nombre");
						}
					}
	
					String agenciaNombre = "No Asignada Agencia";
					try (Statement stmt3 = conn.createStatement(); ResultSet rs3 = stmt3.executeQuery("SELECT nombre FROM Agencia WHERE agencia_id = '" + rs.getString("agencia_id") + "'")) {
						if (rs3.next()) {
							agenciaNombre = rs3.getString("nombre");
						}
					}

					String placas = "No Asignado Placas";
					try (Statement stmt3 = conn.createStatement(); ResultSet rs3 = stmt3.executeQuery("SELECT placa FROM Reserva_Automovil WHERE reserva_id = '" + rs.getString("reserva_id") + "'")) {
						if (rs3.next()) {
							agenciaNombre = rs3.getString("nombre");
						}
					}
					
					reservas += "|" + rs.getString("reserva_id")+ "|"+placas+ "|"+ clienteNombre + "|"+ agenciaNombre + "|"+ rs.getTimestamp("fecha_inicio") + "|"+ rs.getTimestamp("fecha_fin") + "|"+ rs.getString("precio_total") + "|"+ (rs.getInt("entregado") == 1 ? "Sí" : "No")+ "|%";

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

		else if (code.equals("308")) {
			ArrayList<String> agencias = arrayReconstructor(request.substring(4));
			if (agencias.size() < 3) {
				return "801 Not enough data";
			}
			String agencia_id = agencias.get(0);
			String nombre = agencias.get(1);
			String direccion = agencias.get(2);
			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("update Agencia set nombre=?, direccion=? where agencia_id=?")) {
				stmt.setString(1, nombre);
				stmt.setString(2, direccion);
				stmt.setString(3, agencia_id);
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

		
		else if (code.equals("309")) {
			ArrayList<String> agencias = arrayReconstructor(request.substring(4));
			if (agencias.size() < 3) {
				return "801 Not enough data";
			}
			String garaje_id = agencias.get(0);
			String nombre = agencias.get(1);
			String direccion = agencias.get(2);
			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("update Garaje set nombre=?, ubicacion=? where garaje_id=?")) {
				stmt.setString(1, nombre);
				stmt.setString(2, direccion);
				stmt.setString(3, garaje_id);
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

		
		else if (code.equals("310")) {
			ArrayList<String> agencias = arrayReconstructor(request.substring(4));
			if (agencias.size() < 5) {
				return "801 Not enough data";
			}
			String modelo = agencias.get(0);
			String color = agencias.get(1);
			String marca = agencias.get(2);
			String estado = agencias.get(3);
			String garaje_id = agencias.get(4);
			
			
			String placa = agencias.get(5);
			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("update Automovil set modelo=?,color=?,marca=?,estado=?,garaje_id=? where placa=?")) {
				stmt.setString(1, modelo);
				stmt.setString(2, color);
				stmt.setString(3, marca);
				stmt.setString(4, estado);
				String garajeId = "";
				try (Statement stmt2 = conn.createStatement(); ResultSet rs = stmt2.executeQuery("SELECT garaje_id FROM Garaje WHERE nombre = '" + garaje_id + "'")) {
					if (rs.next()) {
						garajeId = rs.getString("garaje_id");
					}
				}
				stmt.setString(5, garajeId);
				stmt.setString(6, placa);
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

		
		else if (code.equals("311")) {
			ArrayList<String> agencias = arrayReconstructor(request.substring(4));
			if (agencias.size() < 5) {
				return "801 Not enough data";
			}
			String nombre = agencias.get(0);
			String direccion = agencias.get(1);
			String telefono = agencias.get(2);
			String sponsor_id = agencias.get(3);
			String dni = agencias.get(4);
			if (sponsor_id.equals("null")) {
				sponsor_id = "6";
			}
			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("update Cliente set nombre=?,direccion=?,telefono=?,sponsor_id=? where dni=?")) {
				stmt.setString(1, nombre);
				stmt.setString(2, direccion);
				stmt.setString(3, telefono);
				// Check if sponsor_id exists
				try (Statement stmt2 = conn.createStatement(); ResultSet rs = stmt2.executeQuery("SELECT cliente_id FROM Cliente WHERE dni = '" + sponsor_id + "'")) {
					if (rs.next()) {
						sponsor_id = rs.getString("cliente_id");
					} else {
						return "801 Sponsor not found";
					}
				}
				stmt.setString(4, sponsor_id);
				stmt.setString(5, dni);
				int rowsAffected = stmt.executeUpdate();
				if (rowsAffected > 0) {
					return "800";
				} else {
					System.out.println(nombre + " " + direccion + " " + telefono + " " + sponsor_id + " " + dni);
					return "801";
				}
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
        } 


		
		else if (code.equals("312")) {
			ArrayList<String> agencias = arrayReconstructor(request.substring(4));
			if (agencias.size() < 7) {
				return "801 Not enough data";
			}

			String cliente_id = agencias.get(0);
			String agencia_id = agencias.get(1);
			String fecha_inicio = agencias.get(2);
			String fecha_fin = agencias.get(3);
			String precio_total = agencias.get(4);
			String entregado = agencias.get(5);
			String reserva_id = agencias.get(6);

			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("update Reserva set cliente_id=?, agencia_id=?, fecha_inicio=?, fecha_fin=?, precio_total=?, entregado=? where reserva_id=?")) {
				
				try (Statement stmt2 = conn.createStatement(); ResultSet rs = stmt2.executeQuery("SELECT cliente_id FROM Cliente WHERE dni = '" + cliente_id + "'")) {
					if (rs.next()) {
						cliente_id = rs.getString("cliente_id");
					}
				}


			try (Statement stmt3 = conn.createStatement(); ResultSet rs2 = stmt3.executeQuery("SELECT agencia_id FROM Agencia WHERE nombre = '" + agencia_id + "'")) {
				if (rs2.next()) {
					agencia_id = rs2.getString("agencia_id");
				}
			}

			if (entregado.equalsIgnoreCase("No")) {
				entregado = "0";
			}
			else if (entregado.equalsIgnoreCase("Si")) {
				entregado = "1";
			}
				stmt.setString(1, cliente_id);
				stmt.setString(2, agencia_id);
				stmt.setTimestamp(3, Timestamp.valueOf(fecha_inicio));
				stmt.setTimestamp(4, Timestamp.valueOf(fecha_fin));
				stmt.setString(5, precio_total);
				stmt.setString(6, entregado);
				stmt.setString(7, reserva_id);
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











		else if (code.equals("401")) {
			ArrayList<String> datos = arrayReconstructor(request.substring(4));
			if (datos.size() < 1) {
				return "801 Not enough data";
			}
			String agencia_id = datos.get(0);
			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("delete from Agencia where agencia_id=?")) {
			
				stmt.setString(1, agencia_id);
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

		
		else if (code.equals("402")) {
			ArrayList<String> datos = arrayReconstructor(request.substring(4));
			if (datos.size() < 1) {
				return "801 Not enough data";
			}
			String garaje_id = datos.get(0);
			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("delete from Garaje where garaje_id=?")) {
			
				stmt.setString(1, garaje_id);
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

		
		else if (code.equals("403")) {
			ArrayList<String> datos = arrayReconstructor(request.substring(4));
			if (datos.size() < 1) {
				return "801 Not enough data";
			}
			String placa = datos.get(0);
			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("delete from Automovil where placa=?")) {
			
				stmt.setString(1, placa);
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
		

		else if (code.equals("404")) {
			ArrayList<String> datos = arrayReconstructor(request.substring(4));
			if (datos.size() < 1) {
				return "801 Not enough data";
			}
			String cliente_id = datos.get(0);
			System.out.println("Cliente ID: " + cliente_id);
			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("delete from Cliente where dni=?")) {
				stmt.setString(1, cliente_id);
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
		

		else if (code.equals("405")) {
			ArrayList<String> datos = arrayReconstructor(request.substring(4));
			if (datos.size() < 1) {
				return "801 Not enough data";
			}
			String reserva_id = datos.get(0);

			try (Connection conn = DBConnection.realizarConexion(); PreparedStatement stmt = conn.prepareStatement("delete from Reserva where reserva_id=?")) {
				
				stmt.setString(1, reserva_id);
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











		else if (code.equals("501")) {
			String report = "601 ";
			Map<String, Double> total = new HashMap<>();
			String agenciaId;
			String precioTotal;
			String agenciaNombre = null;
			ArrayList<String> array = new ArrayList<>();
		
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT agencia_id, precio_total FROM Reserva ")) {
					while (rs.next()) {
					
					agenciaId = rs.getString("agencia_id");
					precioTotal = rs.getString("precio_total");
					if (agenciaId == null || agenciaId.isEmpty()) {
						continue; // Skip if agencia_id is null or empty
					}
					try (Statement stmt2 = conn.createStatement(); ResultSet rs2 = stmt2.executeQuery("SELECT nombre FROM Agencia WHERE agencia_id = '" + agenciaId + "'")) {
						if (rs2.next()) {
							agenciaNombre = rs2.getString("nombre");
						} else {
							agenciaNombre = "No Asignada";
						}
					array.add(agenciaNombre + "|" + precioTotal);

					} catch (SQLException e) {
						return "900 " + e.getMessage();
					}
					 
				}

				for (String entry : array) {
					String[] parts = entry.split("\\|");
					if (parts.length == 2) {
						String agencia = parts[0];
						Double precio = Double.valueOf(parts[1]);
						if (total.containsKey(agencia)) {
							total.put(agencia, total.get(agencia) + precio);
						} else {
							total.put(agencia, precio);
						}
					}
				}

				for (Map.Entry<String, Double> entry : total.entrySet()) {
					report += "|" + entry.getKey() + "|" + entry.getValue() + "|%";
				}
				
				return report;
			} catch (SQLException e) {
				return "900 " + e.getMessage();
			} catch (ClassNotFoundException e) {
				return "901 " + e.getMessage();
			}
		}
		

		else if (code.equals("502")) {
			String report = "602 ";
			Map<String, Double> total = new HashMap<>();
			String fecha;
			String precioTotal;
			ArrayList<String> array = new ArrayList<>();
		
			try (Connection conn = DBConnection.realizarConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT fecha_inicio, precio_total FROM Reserva ")) {
					while (rs.next()) {
					
					fecha = rs.getString("fecha_inicio").substring(0,7);
					precioTotal = rs.getString("precio_total");
					if (fecha == null || fecha.isEmpty()) {
						continue; // Skip if agencia_id is null or empty
					}
					
					array.add(fecha + "|" + precioTotal);

					
					 
				}
				

				for (String entry : array) {
					String[] parts = entry.split("\\|");
					if (parts.length == 2) {
						String agencia = parts[0];
						Double precio = Double.valueOf(parts[1]);
						if (total.containsKey(agencia)) {
							total.put(agencia, total.get(agencia) + precio);
						} else {
							total.put(agencia, precio);
						}
					}
				}

				for (Map.Entry<String, Double> entry : total.entrySet()) {
					report += "|" + entry.getKey() + "|" + entry.getValue() + "|%";
				}
				
				return report;
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
