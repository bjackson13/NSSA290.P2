import java.util.*;
import java.net.*;
import java.io.*;

public class SohoClient {

    public SohoClient(String ip, int port) {

        // Reading and writing to the server
        BufferedReader br = null;
        PrintWriter pw = null;

        Btye[] buf = null;

        // To get user's choice of tcp/udp and get message
        Scanner scan = new Scanner(System.in);

        try {
            Socket s = new Socket(ip, port);
            Socket us = new DatagramSocket();
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

            InetAddress inet = InetAddress.getLocalHost();

            System.out.println("Do you want TCP or UDP?");
            String type = scan.next();
            if (type.toLowerCase().equals("tcp")) {

                System.out.println("What's your message?");
                String message = scan.next();
                // Sends the clients message to the server
                pw.printlnl(message);
                pw.flush();

                // Print-outs for the needed information
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                Date date = new Date(System.currentTimeMillis());
                System.out.println("Connecting to " + ip + "With Ip Address " + inet + "\nUsing TCP on Port " + port
                        + "at " + date);
                System.out.println(date + "  " + message);

                System.out.println("Server message back: \n" + br.readLine());

            } else if (type.toLowerCase().equals("udp")) {

                System.out.println("What's your message?");
                String message = scan.next();

                buf = message.getBytes();
                // Sends the datagrampacket to the server and send it using the socket
                DatagramPacket dgp = new DatagramPacket(buf, buf.length, inet, port);
                us.send(dgp);

                System.out.println("Connecting to " + ip + "With Ip Address " + inet + "\nUsing UDP on Port " + port
                        + "at " + date);
                System.out.println(date + "  " + message);

                // Reuses the old datagram packet to create a reviceing packet to grab the
                // information that will be
                // sent from the user
                dgp = new DatagramPacket(buf, buf.length);
                us.recieve(dgp);
                String serversMessage = new String(dgp.getData(), 0, dgp.getLength());

            } else {
                System.out.println("Invalid input. Restart program.");
            }

            br.close();
            pw.close();
            s.close();

        } catch (IOException io) {

        }
    }

    public static void main(String[] args) {
        String ipAddress = args[0];
        int portNum = args[1];
        new SohoClient(ipAddress, portNum);
    }
}