///A Simple Web Server (WebServer.java)

package http.server;

import java.io.BufferedReader;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
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
  private String serverName;
  private int serverPort;

  /**
   * WebServer constructor.
   */
  public WebServer(String name, int port){
    this.serverName=name;
    this.serverPort=port;
  }

  protected void start() {
    ServerSocket soc;

    System.out.println("WebServer "+serverName+" executing on port "+serverPort);
    System.out.println("(press ctrl-c to exit)");
    try {
      // create the main server socket
      soc = new ServerSocket(serverPort);
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }

    System.out.println("Waiting for connection");
    for (;;) {
      try {
        // wait for a connection
        Socket remote = soc.accept();
        // remote is now the connected socket
        System.out.println("Connection, sending data.");
        BufferedReader in = new BufferedReader(new InputStreamReader(
              remote.getInputStream()));
        OutputStream outputStream = remote.getOutputStream();

        List<String> request = new ArrayList<String>();
        String str = in.readLine();
        int nbDelimiters = 0;

        //On lit les données tant que deux lignes d'affilé ne sont pas nulles

        while(nbDelimiters<2) {
          if(str!=null && !str.equals("")){
            System.out.println("-"+str+"-");
            str = in.readLine();
            request.add(str);
          }else{
            nbDelimiters++;
          }
          System.out.println(nbDelimiters);
        }

        System.out.println("requete reçue");
        RequestHandler requestHandler = new RequestHandler(this.serverName);
        str = in.readLine();
        System.out.println("param = "+str);
        outputStream.write(requestHandler.handleRequest(request));

      } catch (Exception e) {
        System.out.println("Error: " + e);
      }
    }
  }


  /**
   * Start the application.
   * 
   * @param args
   *            Command line parameters are not used.
   */
  public static void main(String args[]) {
    if (args.length != 2) {
      System.err.println("Usage java WebServer <server host name> <server port number>");
      return;
    }
    WebServer ws = new WebServer(args[0],Integer.parseInt(args[1]));
    ws.start();
  }
}

