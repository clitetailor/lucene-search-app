package mypackage.main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;

public class UDPServer {
    public static void main(String[] args) {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(19010);

            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                datagramSocket.receive(receivePacket);

                String block = new String(receivePacket.getData(), Charset.forName("UTF8"));

                System.out.println("block: " + block);

                InetAddress ipAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();

                sendData = block.toUpperCase().getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);

                datagramSocket.send(sendPacket);
            }
        } catch (SocketException e) {
            System.out.println("SocketException: " + e.getClass() + " : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getClass() + " : " + e.getMessage());
        }

    }
}
