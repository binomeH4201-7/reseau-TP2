///A Simple Web Server (WebServer.java)

package http.server;

import java.io.BufferedReader;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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

  /**
   * WebServer constructor.
   */
  protected void start() {
    ServerSocket soc;

    System.out.println("Webserver starting up on port 80");
    System.out.println("(press ctrl-c to exit)");
    try {
      // create the main server socket
      soc = new ServerSocket(3000);
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
        PrintWriter out = new PrintWriter(remote.getOutputStream());

        // read the data sent. We basically ignore it,
        // stop reading once a blank line is hit. This
        // blank line signals the end of the client HTTP
        // headers.
        String str = ".";
        /*while (str != null && !str.equals("")){
          str = in.readLine();
          request += str;
          }*/
        str = in.readLine();
        String[] request = str.split(" ",3);
        String method = request[0];
        String ressource = request[1];
        String protocol = request[2];

        while(str != null && !str.equals(""))
            str = in.readLine();

        switch(method){
          case "GET":
            out.println("HTTP/1.0 200 OK");
            out.println("Content-Type: text/html");
            out.println("Server: Bot");
            out.println("");
            //ouvrir le fichier qui correspond a ressource
            //envoyer son contenu
            BufferedReader inFile = new BufferedReader(new FileReader("./ressources"+ressource));
            String lineFile;
            while((lineFile = inFile.readLine()) != null){
              out.println(lineFile);
            }
            out.flush();
            break;
          case "POST":
            out.println("HTTP/1.0 200 OK");
            out.println("Server: Bot");
            out.println("");
            //TRAITEMENT
            //recuperer les paramètres :
            str = in.readLine();
            String[] parameters = str.split("&");
            //ecrire les paramètres dans la ressource demandée
            BufferedWriter outFile = null;
            try
            {
              outFile = new BufferedWriter(new FileWriter("./ressources"+ressource,true));
              String message = "";
              for(String s : parameters){
                message += "paramètre : "+s.split("=")[0]+" (valeur : "+s.split("=")[1]+").\n";
              }
              outFile.append(message);
              outFile.close();

            }
            catch(Exception E){
              System.err.println("Impossible d'acceder à la requete");
            }
            out.flush();
            break;
          default:
        }
        //Analyse de la requête HTTP :
        /*
           GET /index.html HTTP/1.1
           POST /ressource 
Host: www.example.com
<a black line>
         */

        out.flush();
        remote.close();
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
    WebServer ws = new WebServer();
    ws.start();
  }
}
