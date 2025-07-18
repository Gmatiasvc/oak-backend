package test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import server.VanityConsole;

public class TestMain {
	
	private static final String COLOR_RESET = "\u001B[0m"; // Reset color
	private static final String COLOR_RED = "\u001B[31m"; // Red color
	private static final String COLOR_GREEN = "\u001B[32m"; // Green color
	private static final String COLOR_YELLOW = "\u001B[33m"; // Yellow color
	private static final String COLOR_BLUE = "\u001B[34m"; // Blue color
	private static final String COLOR_PURPLE = "\u001B[35m"; // Purple color
	private static final String COLOR_BOLD = "\u001B[1m"; // Bold text
	

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	public static void shout(String message) {
		System.out.println(COLOR_YELLOW+"["+ COLOR_PURPLE + LocalDateTime.now().format(FORMATTER) + COLOR_YELLOW+"] " + COLOR_RESET + message);
	}

	public static void error(String message) {
		System.err.println(COLOR_YELLOW+"["+ COLOR_PURPLE + LocalDateTime.now().format(FORMATTER) + COLOR_YELLOW+"] " + COLOR_RED + "[✖] " + message + COLOR_RESET);
	}

	public static void sucess(String message) {
		System.out.println(COLOR_YELLOW+"["+ COLOR_PURPLE + LocalDateTime.now().format(FORMATTER) + COLOR_YELLOW+"] " + COLOR_GREEN + "[✔] " + message + COLOR_RESET);
	}
	public static void message(String message) {
		System.out.println(COLOR_YELLOW+"["+ COLOR_PURPLE + LocalDateTime.now().format(FORMATTER) + COLOR_YELLOW+"] " + COLOR_BLUE + "[?] " + message + COLOR_RESET);
	}
	
	public static void panic(String message) {
		System.err.println(COLOR_RED+"[" + LocalDateTime.now().format(FORMATTER) + "] " + COLOR_BOLD + "[!] " +message + COLOR_RESET);
	} 

	public static void space(){
		System.out.println(); 
	}

	public static void title(String title) {
		int len = title.length();
		System.out.println("=".repeat(len + 4));
		System.out.println(COLOR_BOLD+"  "+ title +"  " + COLOR_RESET);
		System.out.println("=".repeat(len + 4));
		space();
	}


	public static void main(String[] args) {
		
		message("Esta parte de las pruebas revisa la consola, si ves colores bonitos, todo va bien :)\nSi no ves colores, o los mensajes son distintos, revisa tu terminal, o la configuración de tu IDE.");

		
		space();
		message("Pruebas de los metodos de VanityConsole del paquete server, deben verse igual que los anteriores");
		space();
		VanityConsole.test();
		space();
		title("Pruebas de la conexion a la base de datos");
		if (DBConnTest.test()) {
			sucess("La conexion a la base de datos se realizo correctamente");
		} else {
			error("La conexion a la base de datos fallo, revisa los mensajes anteriores para mas detalles");
		}
	}
}
