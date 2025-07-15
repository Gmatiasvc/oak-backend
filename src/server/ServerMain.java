package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    public static void main(String[] args) {
        int portNumber = 4444; // Choose an available port

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server started on port " + portNumber);
            System.out.println("Waiting for a client connection...");

            // The accept() method blocks until a client connects
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            // Set up input and output streams for communication with the client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // true for auto-flush

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);
                out.println("Server received: " + inputLine); // Send a response back to the client
                if (inputLine.equals("bye")) {
                    break; // Exit loop if client sends "bye"
                }
            }

            System.out.println("Client disconnected.");
            // Close streams and socket
            in.close();
            out.close();
            clientSocket.close();

        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber + " or accept client connection.");
            System.err.println(e.getMessage());
        }
    }
}