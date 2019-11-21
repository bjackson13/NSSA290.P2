import java.net.*;  
import java.io.*;

public class ServerAccept {
   
   private final int PORT = 44400;
   
   public static void main(String[] args) {
      
   }
   
   public ServerAccept() {
      try {
      
         ServerSocket socket = new ServerSocket(PORT);
      } catch(IOException) {
         System.out
      }
   }

   class ConnectionHandler extends Thread {
      
      public ConnectionHandler(Socket client) {
      
      }
      
   }

}

