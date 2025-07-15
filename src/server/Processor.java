package server;

public class Processor {


    private String trimCode(String request) {
        if (request.length() >= 3) {
            // Trim the request to a maximum of 3 characters
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
			return request;


		} else if (request.equalsIgnoreCase("bye") || code.equals("999")) {
			return "999Goodbye!";

		
		} else if (code.equals("000")|| request.equalsIgnoreCase("ping")) {
			return "000Pong";

		} else if (code.equals("101")) {
			return "This is a test message, please ignore it.";

		} else if (code.equals("102")) {
			return "This is a test message, please ignore it.";

		} else if (code.equals("103")) {
			return "This is a test message, please ignore it.";

		} else if (code.equals("104")) {
			return "This is a test message, please ignore it.";

		} else if (code.equals("105")) {
			return "This is a test message, please ignore it.";

		} else if (code.equals("106")) {
			return "This is a test message, please ignore it.";

		} else if (code.equals("107")) {
			return "This is a test message, please ignore it.";

		} else if (code.equals("108")) {
			return "This is a test message, please ignore it.";

		} else if (code.equals("109")) {
			return "This is a test message, please ignore it.";
		} else {
			return "What? Use a code please! " ;
		}

    }
}
