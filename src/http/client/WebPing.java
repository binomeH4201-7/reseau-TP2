package http.client;

import java.net.InetAddress;
import java.net.Socket;

public class WebPing {
  public static void main(String[] args) {

    if (args.length != 2) {
      System.err.println("Usage java WebPing <server host name> <server port number>");
      return;
    }	

    String httpServerHost = args[0];
    int httpServerPort = Integer.parseInt(args[1]);
    httpServerHost = args[0];
    httpServerPort = Integer.parseInt(args[1]);

    try {
      InetAddress addr;      Socket sock = new Socket(httpServerHost, httpServerPort);
      addr = sock.getInetAddress();
        
      System.out.println("Connected to " + addr);
      sock.close();
    } catch (java.io.IOException e) {
      System.out.println("Can't connect to " + httpServerHost + ":" + httpServerPort);
      System.out.println(e);
    }
  }
}
