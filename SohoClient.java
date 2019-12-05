import java.text.SimpleDateFormat;
import java.util.*;
import java.net.*;
import java.io.*;

public class SohoClient {

    public SohoClient(String ip, int port) {

        // Reading and writing to the server
        BufferedReader br = null;
        PrintWriter pw = null;

        byte[] buf = null;

        // To get user's choice of tcp/udp and get message
        Scanner scan = new Scanner(System.in);

        try {

            InetAddress inet = InetAddress.getByName(ip);
            System.out.println(inet);

            System.out.println("Do you want TCP or UDP?");
            String type = scan.next();

            Date date = new Date(System.currentTimeMillis());

            if (type.toLowerCase().equals("tcp")) {

                Socket s = new Socket(ip, port);
                br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

                System.out.print("What's your message?");
                scan.nextLine();
                String message = scan.nextLine();
                // Sends the clients message to the server
                pw.println(message);
                pw.flush();

                // Print-outs for the needed information
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                System.out.println("Connecting to " + ip + "With Ip Address " + inet + "\nUsing TCP on Port " + port
                        + "at " + date);
                System.out.println(date + "  " + message);

                System.out.println("Server message back: \n" + br.readLine());

                s.close();

            } else if (type.toLowerCase().equals("udp")) {

                DatagramSocket us = new DatagramSocket();

                System.out.println("What's your message?");
                scan.nextLine();
                String message = scan.nextLine();

                buf = message.getBytes();
                // Sends the datagrampacket to the server and send it using the socket
                DatagramPacket dgp = new DatagramPacket(buf, buf.length, inet, port);
                us.send(dgp);

                System.out.println("Connecting to " + ip + " With Ip Address " + InetAddress.getLocalHost()
                        + "\nUsing UDP on Port " + port + "at " + date);
                System.out.println(date + "\nThe message is: " + message);

                // Reuses the old datagram packet to create a reviceing packet to grab the
                // information that will be
                // sent from the user

                buf = new byte[1024];
                dgp = new DatagramPacket(buf, buf.length);
                us.receive(dgp);
                String serversMessage = new String(dgp.getData(), 0, dgp.getLength());
                System.out.println("Server message back: \n" + serversMessage);
                us.close();

            } else {
                System.out.println("Invalid input. Restart program.");
            }

            br.close();
            pw.close();
            System.exit(0);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String ipAddress = args[0];
        int portNum = Integer.parseInt(args[1]);
        new SohoClient(ipAddress, portNum);
    }
}