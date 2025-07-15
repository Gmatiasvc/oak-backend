package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit; // To show current time in messages

public class ServerMain {

    private static final int PORT = 6969; // Funi port
    // Define the size of our thread pool.
    // A common choice is (number of CPU cores * 2) or a fixed number based on expected load.
    private static final int THREAD_POOL_SIZE = 5; // Allows 5 clients to be processed concurrently

    // A formatter for logging timestamps
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        // Create a fixed-size thread pool.
        // This pool will manage a set number of threads to handle incoming client connections.
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // Use a try-with-resources statement to ensure the ServerSocket is closed
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(LocalDateTime.now().format(FORMATTER) + " | Server listening on port " + PORT);
            System.out.println(LocalDateTime.now().format(FORMATTER) + " | Waiting for client connections...");

            // The server runs indefinitely, continuously accepting new client connections
            while (true) {
                try {
                    // serverSocket.accept() blocks until a client connects.
                    // When a client connects, a new Socket object is returned, representing that specific connection.
                    Socket clientSocket = serverSocket.accept();
                    String clientAddress = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
                    System.out.println(LocalDateTime.now().format(FORMATTER) + " | Client connected from: " + clientAddress);

                    // Submit the client handling logic as a Runnable task to the thread pool.
                    // The ExecutorService will pick an available thread from its pool to execute this task.
                    executor.submit(new ClientHandler(clientSocket, clientAddress));

                } catch (IOException e) {
                    System.err.println(LocalDateTime.now().format(FORMATTER) + " | Error accepting client connection: " + e.getMessage());
                    // In a production server, you might log this error and continue,
                    // or implement a more sophisticated error recovery mechanism.
                }
            }

        } catch (IOException e) {
            System.err.println(LocalDateTime.now().format(FORMATTER) + " | Error starting server: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for server startup errors
        } finally {
            // It's crucial to shut down the executor service when the server application is closing.
            // This prevents resource leaks and ensures all submitted tasks are completed or gracefully terminated.
            System.out.println(LocalDateTime.now().format(FORMATTER) + " | Shutting down server and thread pool...");
            executor.shutdown(); // Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted.
            try {
                // Wait a certain amount of time for existing tasks to complete.
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    // If tasks don't complete within the timeout, force shutdown all running tasks.
                    executor.shutdownNow();
                    System.err.println(LocalDateTime.now().format(FORMATTER) + " | Thread pool did not terminate gracefully. Forcing shutdown.");
                }
            } catch (InterruptedException ex) {
                // Re-interrupt the current thread if interrupted while waiting for termination.
                executor.shutdownNow();
                Thread.currentThread().interrupt();
                System.err.println(LocalDateTime.now().format(FORMATTER) + " | Server shutdown interrupted.");
            }
            System.out.println(LocalDateTime.now().format(FORMATTER) + " | Server stopped.");
        }
    }

    /**
     * A Runnable class to handle communication with a single client.
     * Each instance of this class will run in its own thread from the ExecutorService pool.
     */
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private String clientAddress;

        public ClientHandler(Socket clientSocket, String clientAddress) {
            this.clientSocket = clientSocket;
            this.clientAddress = clientAddress;
        }

        @Override
        public void run() {
            // Use try-with-resources for automatically closing streams and the socket
            try (
                // Get input stream to read data from the client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Get output stream to send data to the client, 'true' for auto-flushing
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            ) {
                String inputLine;
                System.out.println(LocalDateTime.now().format(FORMATTER) + " | Handler for " + clientAddress + " started.");

                // Read lines from the client until the client closes the connection
                // or sends a specific "bye" message.
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(LocalDateTime.now().format(FORMATTER) + " | Received from " + clientAddress + ": " + inputLine);

                    // Process the request (in this simple case, just echo it back)
                    String response = "Echo from server [" + Thread.currentThread().getName() + "]: " + inputLine;
                    out.println(response); // Send response back to client

                    if (inputLine.equalsIgnoreCase("bye")) {
                        break; // Client requested to close connection
                    }
                }
                System.out.println(LocalDateTime.now().format(FORMATTER) + " | Client disconnected: " + clientAddress);

            } catch (IOException e) {
                // Log any errors that occur during communication with this specific client
                System.err.println(LocalDateTime.now().format(FORMATTER) + " | Error handling client " + clientAddress + ": " + e.getMessage());
                // e.printStackTrace(); // Uncomment for detailed stack trace for client errors
            } finally {
                // Ensure the client socket is closed, even if errors occurred.
                // This releases the resources associated with the connection.
                try {
                    if (!clientSocket.isClosed()) {
                        clientSocket.close();
                        System.out.println(LocalDateTime.now().format(FORMATTER) + " | Socket for " + clientAddress + " closed.");
                    }
                } catch (IOException e) {
                    System.err.println(LocalDateTime.now().format(FORMATTER) + " | Error closing client socket " + clientAddress + ": " + e.getMessage());
                }
            }
        }
    }
}