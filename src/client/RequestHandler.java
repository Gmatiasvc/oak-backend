package client;

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
}
