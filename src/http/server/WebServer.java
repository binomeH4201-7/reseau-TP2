///A Simple Web Server (WebServer.java)

package http.server;

import java.io.BufferedReader;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * 
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 * 
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {
  private int serverPort;

  /**
   * WebServer constructor.
   */
  public WebServer(int port){
    try {
      this.serverName = InetAddress.getLocalHost().getHostName();
    }catch (Exception e){
      this.serverName = new String();
    }
    this.serverPort=port;
  }

  protected void start() {
    ServerSocket soc;

    System.out.println("WebServer executing on port "+serverPort);
    System.out.println("(press ctrl-c to exit)");
    try {
      // create the main server socket
      soc = new ServerSocket(serverPort);
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }

    System.out.println("Waiting for connection");
    while(true) {
      try {
        // wait for a connection
        Socket remote = soc.accept();
        // remote is now the connected socket
        System.out.println("Connection, sending data.");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                remote.getInputStream()));
        OutputStream outputStream = remote.getOutputStream();

        communicationHandler(in,outputStream);

        outputStream.flush();
        outputStream.close();

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**Manage the communication channel passed in parameters through
   * Receive a request from the client
   * Send a response to suit the request
   *
   * @param in BufferedReader created from the socket
   * @param out OutputStream created from the socket
   *
   **/
  private void communicationHandler(BufferedReader in,OutputStream out) throws Exception{
      List<String> requestStrings = new ArrayList<String>();
      String str = in.readLine();

      //Lecture de l'en-tête
      while (str != null && !str.equals("")) {
        System.out.println("-" + str + "-");
        requestStrings.add(str);
        str = in.readLine();
      }
      System.out.println(requestStrings.toString());

      Request request = new Request(requestStrings);

      //Lecture du corps si necessaire
      if (request.getHTTPMethod().equals("POST")) {
        int nbChar = request.getContentLength();
        char[] cbuf = new char[nbChar];
        in.read(cbuf, 0, nbChar);
        request.setParameters(String.valueOf(cbuf));
        System.out.println(String.valueOf(cbuf));
      }

      RequestHandler requestHandler = new RequestHandler("", request);
      out.write(requestHandler.handleRequest());
  }


  /**
   * Start the application.
   * 
   * @param args
   *
   */
  public static void main(String args[]) {
    if (args.length != 1) {
      System.err.println("Usage java WebServer <server port number>");
      return;
    }
    WebServer ws = new WebServer(Integer.parseInt(args[0]));
    ws.start();
  }
}

