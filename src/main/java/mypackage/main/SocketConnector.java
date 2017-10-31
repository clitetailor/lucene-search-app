package mypackage.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnector {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(19010);

            while (true) {
                Socket client = server.accept();
                waitForConnection(client);
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getClass() + " : " + e.getMessage());
        }
    }

    private static void waitForConnection(Socket client) {
        System.out.println("Connecting to a client ...");
        try {
            InputStream inputStream = client.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();

            if (line.toLowerCase().startsWith("write")) {
                String data = readAll(bufferedReader);

                System.out.println("Write: " + data);
            }

            if (line.toLowerCase().startsWith("search")) {
                String data = readAll(bufferedReader);
            }

            client.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getClass() + " : " + e.getMessage());
        }
    }

    private static String readAll(BufferedReader bufferedReader) {
        try {
            StringBuilder builder = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                builder.append(line);
                builder.append("\n");
                line = bufferedReader.readLine();
            }

            return builder.toString();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getClass() + " : " + e.getMessage());
        }

        return null;
    }
}
