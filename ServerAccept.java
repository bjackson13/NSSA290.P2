import java.net.*;  
import java.io.*;
import java.util.Scanner;
import java.util.Date;
import java.sql.Timestamp;

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
         Date date = new Date();
        
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
         System.out.println("Server IP Address: " + ip.getHostAddress());
         System.out.println("Server running "+ protocol +" on Port: " + PORT + " at " + new Timestamp(date.getTime()));
        
         
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
         ex.printStackTrace(System.out);
      }
   }
   
   //handles TCP client connections
   class TCPConnectionHandler extends Thread {
      
      boolean running = true;
      Socket client;
      BufferedReader br;
      PrintWriter pw;
      //get date for timestamp
      Date date= new Date();
      
      public TCPConnectionHandler(Socket _client) {
         client = _client;
      }
      
      public void run() {
         
         try {
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
            
            //print out new client connection address
            System.out.println(new Timestamp(date.getTime()) + ": New Connection From: " + client.getInetAddress().toString());
         
         } catch(IOException ex) {
            ex.printStackTrace(System.out);
         }
         
         String clientText = "";
         while(running) {
            //listen for client packets
            try {
               clientText = br.readLine();
               
               if(clientText != null) {
               
                  //print out client message
                  System.out.println(new Timestamp(date.getTime()) + ": Client Message Received: " + clientText);
                  
                  //send message back to client
                  pw.println(new Timestamp(date.getTime()).toString() + ": " + clientText);
                  
                  //if client sends 'end' command
                  if(clientText.toLowerCase().equals("end")) { 
                     kill();
                     System.out.println(new Timestamp(date.getTime()) + ": Ending connection with: " + client.getInetAddress().toString());
                     return;
                  }
               }
               
            } catch(IOException ex) {
               ex.printStackTrace(System.out);
            } catch(NullPointerException ex) {
               ex.printStackTrace(System.out);
            }
         }
      }
      
      public void kill() {
         running = false;
      }
      
   }
   
   //hanldes UDP connections
   class UDPConnectionHandler extends Thread {
      
      boolean running = true;
      DatagramSocket client;
      //get date for timestamp
      Date date= new Date();
      byte[] buf = new byte[1024];
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      
      public UDPConnectionHandler(DatagramSocket udpSocket) {
         client = udpSocket;
      }
      
      public void run() {
         
         while(running) {
            //listen for client packets
            try {
               client.receive(packet);
               
               packet = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());
               
               System.out.println(packet.getAddress());
               
                  //print out new client connection address
                  System.out.println(new Timestamp(date.getTime()) + ": New Connection From: " + packet.getAddress().toString());
                  
                  //print out client message
                  String clientText = new String(packet.getData(), 0, packet.getLength());
                  System.out.println(new Timestamp(date.getTime()) + ": Client Message Received: " + clientText);
                  
                  //send message back to client
                  buf = clientText.getBytes();
                  System.out.println("IP Address of packet: " + packet.getAddress());
                  DatagramPacket dgp = new DatagramPacket(buf, buf.length, packet.getAddress(), client.getLocalPort());
                  client.send(dgp);
                  
                  //if client sends 'end' command
                  if(clientText.toLowerCase().equals("end")) { 
                     kill();
                     System.out.println(new Timestamp(date.getTime()) + ": Ending connection with: " + client.getInetAddress().toString());
                     return;
                  }
               
            } catch(IOException ex) {
               ex.printStackTrace(System.out);
            } catch(NullPointerException ex) {
               ex.printStackTrace(System.out);
            }
         }
      }
      
      public void kill() {
         running = false;
      }
   }

}

