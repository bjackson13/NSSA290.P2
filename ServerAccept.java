import java.net.*;  
import java.io.*;
import java.util.Scanner;

/*
   Project 2
   Team 1
   NSSA 290
   Fall 2019
*/

public class ServerAccept {
   
   private final int PORT = 44400;
   Scanner scanner = new Scanner(System.in);
   
   public static void main(String[] args) {
      ServerAccept server = new ServerAccept();
   }
   
   public ServerAccept() {
      try {
        
         boolean isTCP = false;
         boolean socketSet = false;
         ServerSocket tcpSocket = null;
         DatagramSocket udpSocket = null;
         InetAddress ip = InetAddress.getLocalHost();
        
        //get UDP or TCP from the user
         do {
            System.out.println("Please select TCP or UDP: ");
            String tcpUDP =  scanner.nextLine();
            if(tcpUDP.toLowerCase().equals("tcp")) {
               tcpSocket = new ServerSocket(PORT);
               isTCP = true;
               socketSet = true;
            } else if(tcpUDP.toLowerCase().equals("udp")) {
               udpSocket  = new DatagramSocket(PORT);
               socketSet = true;
            }
         } while(socketSet == false);
        
         String protocol = isTCP ? "TCP" : "UDP";
         System.out.println("Server Hostname: " + ip.getHostName());
         System.out.println("Server IP Address: " + ip);
         System.out.println("Server running "+ protocol +" on Port: " + PORT);
        
         
         //handle tcp connections
         if(isTCP) {
            Socket clientSocket = null;
            while(true) {
               clientSocket = tcpSocket.accept();
               TCPConnectionHandler conn = new TCPConnectionHandler(clientSocket); 
               conn.start();
            }
         }
         
         //handle UDP connections
         if(!isTCP) {
            UDPConnectionHandler responseHandler = new UDPConnectionHandler(udpSocket);
            responseHandler.start();
         }
         
      } catch(IOException ex) {
         System.out.println(ex);
      }
   }
   
   //handles TCP client connections
   class TCPConnectionHandler extends Thread {
      
      boolean running = true;
      
      public TCPConnectionHandler(Socket client) {
      
      }
      
      public void run() {
         while(running) {
         }
      }
      
      public void kill() {
         running = false;
      }
      
   }
   
   //hanldes UDP connections
   class UDPConnectionHandler extends Thread {
      
      boolean running = true;
      
      public UDPConnectionHandler(DatagramSocket udpSocket) {
      
      }
      
      public void run() {
         while(running) {
            //listen for client packets
         }
      }
      
      public void kill() {
         running = false;
      }
   }

}

