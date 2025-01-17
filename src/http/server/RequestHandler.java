package http.server;

import java.io.IOException;
import java.util.*;
import java.io.File;

/**
 * Class Handling the request by instantiating a response and writing it according to the request.
 *
 * @author BUONOMO Phanie, BATEL Arthur
 */
public class RequestHandler {

  private Request request;
  private Response response;
  private String serverName;
  private static Map<String,List<String>> typeToExtension;
  private static Map<String,List<String>> typeToMethod;

  private static final String[]       image = {"image","gif", "png", "jpeg"};
  private static final String[]       audio = {"audio","wav"};
  private static final String[]       video = {"video","mpeg"};
  private static final String[]        text = {"text","plain", "html", "javascript"};
  private static final String[] application = {"application","json", "pdf"};
  private static final String[][]     types = {audio,image,video,text,application};

  private static final String[]       imageMethods = {"GET","DELETE","OPTIONS","HEAD"};
  private static final String[]         audioMethods = {"GET","DELETE","OPTIONS","HEAD"};
  private static final String[]         videoMethods = {"GET","DELETE","OPTIONS","HEAD"};
  private static final String[]          textMethods = {"GET","POST","PUT","DELETE","OPTIONS","HEAD"};
  private static final String[]   applicationMethods = {"GET","POST","PUT","DELETE","OPTIONS","HEAD"};
  private static final String[][]      typesMethods = {audioMethods,imageMethods,videoMethods,textMethods,applicationMethods};

  /**RequestHandler constructor, instantiate the object from the request in parameter
   * Call the methods to initialize codeToError, typeToExtension and typeToMethod Maps
   *
   * @param serverName the server from which the object RequestHandler is instantiated
   * @param request the request to handle
   *
   **/
  public RequestHandler(String serverName, Request request){
    this.request = request;
    response = new Response();
    this.serverName = serverName;
    Response.initError();
    initTypes();
  }

  /**Build a response from the request received and return it
   *
   * @return table of bytes containing the response
   **/
  public byte[] handleRequest(){
    switch (request.getHTTPMethod()) {
        case "GET":
            get();
            break;
        case "POST":
          post();
          break;
        case "PUT":
          put();
          break;
        case "DELETE":
          delete();
            break;
        case "OPTIONS":
          option();
            break;
        case "HEAD":
            head();
          break;
        case "PATCH":
          patch();
          break;
        case "CONNECT":
          connect();
          break;
        case "TRACE":
          trace();
          break;
        default:
            badRequest();
          break;
      }
    return response.toBytes();
  }

  /**
    Crée ou remplace une ressource par le contenu de la requête
    Exemple de syntaxe :
    PUT /new.html HTTP/1.0
    Host: example.com
    Content-type: text/html
    Content-length : 16

    <p>New File</p>

    Si le fichier est créé, le code retour est 201
    Si le fichier est modifié avec succès, le code retour est 204
    Aucun corps de réponse n'est renvoyé
   */
  private void put(){
    File file = new File("./ressources"+request.getRessourceName());
    int code;
    if(file.exists()){
      code = 204;
    }
    else{
      code = 201;
    }

    try{
      response.writeRessource(request.getRessourceName(),request.getParameters());
      response.setResponseCode(code);
      response.addServerName(this.serverName);
    }
    catch(Exception e){
      response.setResponseCode(500);
      e.printStackTrace();
    }
  }

  /**
    Supprime la ressource précisée dans l'en-tête
    Exemple de syntaxe :
    DELETE /file.html HTTP/1.0

    La requête, ainsi que la réponse, n'ont pas de corps
    Si tout se passe bien, le code de retour est 204
    Si la ressource n'existe pas, le code de retour est 404
   */
  private void delete(){
    int code = 500;
    File file = new File("./ressources"+request.getRessourceName());
    if(file.exists()){
      try{
        if(response.deleteRessource(request.getRessourceName()))
          code = 204;
      }
      catch(Exception e){
        code = 500;
        e.printStackTrace();
      }
    } else {
      code = 404;
    }
    response.setResponseCode(code);
    response.addServerName(this.serverName);
  }

  /**
    Envoie des données au serveur dans le but de modifier des ressources sur
    le serveur
    Dans le cadre de ce TP, nous nous contenterons de modifier la ressource indiquée en ajoutant à la fin de celle-ci les données précisées
    Cela peut correspondre par exemple à une opération d'ajout dans une base de données très basique

    Exemple de syntaxe :
    POST /index.html HTTP/1.0
    Content-type text/plain
    Content-length: 13

    say=hi&to=Mom

    Dans ce TP, le corps de la réponse consiste en la ressource modifiée
    Si la ressource n'existe pas, elle sera crée (code de réponse 201)
    Si la ressource existe, elle sera complétée (code 200)

   */
  private void post(){
    File file = new File("./ressources"+request.getRessourceName());
    int code;
    if(file.exists()){
      code = 200;
    }
    else{
      code = 201;
    }

    try{
      String extension = request.getRessourceExtension();
      try{
        response.setExtension(findContentType(extension),extension);
        response.setResponseCode(code);
      }catch(Exception e){
        code = 415;
        response.setResponseCode(code);
      }
      response.writeRessource(request.getRessourceName(),request.getParameters(),true);
      response.addServerName(this.serverName);
      response.addRessource(request.getRessourceName());
    }
    catch(Exception e){
      response.setResponseCode(500);
      e.printStackTrace();
    }
  }

  /**
   Demande la ressource identifiée dans l'en-tête de la requête
   Exemple de syntaxe :
   GET /index.html
   Host: example.com

   Si la ressource a été récupérée et est renvoyée dans le corps du message : code retour 200
   Si la ressource n'a pas été trouvée : code retour 404
   Si une erreur de lecture survient : code retour 500
   Cords de la réponse : ressource
   */
  private void get(){
    File file = new File("./ressources"+request.getRessourceName());
        boolean fail = false;
        String extension = request.getRessourceExtension();
        try{
          response.setExtension(findContentType(extension),extension);
        }catch(Exception e){
          response.setResponseCode(415);
          fail = true;
        }
        if(!fail){
          if(!file.exists()){
            response.setResponseCode(404);
          } else {
            try {
              response.addServerName(serverName);
              response.addRessource(request.getRessourceName());
              response.setResponseCode(200);
            } catch (IOException e) {
              response.setResponseCode(500);
              e.printStackTrace();
            }
          }
        }
  }

  /**
   Demande les en-têtes qui seraient retournés si la ressource spécifiée était demandée avec une méthode HTTP GET.
   Exemple de syntaxe :
   HEAD /index.html

   Si la ressource a été récupérée : code retour 200
   Si la ressource n'a pas été trouvée : code retour 404
   Pas de corps
   */
  private void head(){
    File file = new File("./ressources"+request.getRessourceName());

      String extension = request.getRessourceExtension();
      try{
        response.setExtension(findContentType(extension),extension);
      }catch(Exception e){
        response.setResponseCode(415);
      }
    if(!file.exists()){
      response.setResponseCode(404);
    } else {
      response.addServerName(serverName);
        response.setResponseCode(200);
    }
  }

  /**
   Demande les options de communication (méthodes HTTP) pour la ressource ciblée dans l'en-tête
   Exemple de syntaxe :
   OPTIONS /index.html HTTP/1.1

   Si la ressource a été récupérée : code retour 200
   Si la ressource n'a pas été trouvée : code retour 404
   Pas de corps
   */
  private void option(){
    File file = new File("./ressources"+request.getRessourceName());

      try{
      response.addHTTPAllowedMethods(typeToMethod.get(findContentType(request.getRessourceExtension())));
      }catch (Exception e){
        response.setResponseCode(415);
      }
    if(!file.exists()){
      response.setResponseCode(404);
    } else {
      response.addServerName(serverName);
      response.setResponseCode(200);
    }
  }

  private void badRequest(){
    response.setResponseCode(400);
    response.addServerName(serverName);
  }   
  private void patch(){
    response.setResponseCode(501);
    response.addServerName(serverName);
  }
  private void trace(){
    response.setResponseCode(501);
    response.addServerName(serverName);
  }
  private void connect(){
    response.setResponseCode(501);
    response.addServerName(serverName);
  }

  /**
   * Return the MIME type corresponding to the extension of the file
   *
   * @param extension file extension
   * @throws Exception
   * @return String of the MIME type
   */
  private String findContentType(String extension) throws Exception{

    for(List<String> extensionsType : typeToExtension.values()){
      if(extensionsType.contains(extension)){
        return extensionsType.get(0);
      }
    }
    throw new UnsupportedMediaTypeException("Type non pris en charge");
  }

  /**
   * Initialize the typeToExtension and typeToMethods HashMap
   *
   */
  public static void initTypes(){
    typeToExtension = new HashMap<String,List<String>>();
    for(String[] type : types){
      typeToExtension.put(type[0],new ArrayList<String>(Arrays.asList(type)));
    }
    typeToMethod = new HashMap<String,List<String>>();
    int i=0;
    for(String[] type : typesMethods){
      typeToMethod.put(types[i][0],new ArrayList<String>(Arrays.asList(type)));
      i++;
    }
  }

}
