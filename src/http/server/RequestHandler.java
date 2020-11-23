package http.server;

import java.util.List;
import java.io.File;

public class RequestHandler {
  private Request request;
  private Response response;
  private String serverName;

  /*Constructeur, instancie l’objet request avec l’objet en paramètre de RequestHandler*/
  public RequestHandler(List<String> request, String serverName){
    this.request = new Request(request);
    response = new Response();
    this.serverName = serverName;
    Response.initError();
    Response.initTypes();
  }

  /*f(request) = response. Renvoie la réponse sous forme de bytes pour être directement envoyé*/
  public byte[] handleRequest(){
    try {
      switch (request.getHTTPMethod()) {
        case GET:
          break;
        case POST:
          post();
          break;
        case PUT:
          put();
          break;
        case DELETE:
          delete();
          break;
        case OPTIONS:
          break;
        case HEAD:
          break;
        default:
          break;
      }
    }catch (Exception e){}
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
    File file = new File("/.ressources"+request.getRessourceName());
    int code = 500;
    if(file.exists()){
      code = 204;
    }
    else{
      code = 201;
    }

    try{
      response.writeRessource(request.getRessourceName(),request.getContent());
      response.setResponseCode(code);
      response.addServerName(this.serverName);
    }
    catch(Exception E){
      response.setResponseCode(500);
    }
    response.finishHeader();
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
      }
    } else {
      code = 404;
    }
    response.setResponseCode(code);
    response.addServerName(this.serverName);
    response.finishHeader();
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
    File file = new File("/.ressources"+request.getRessourceName());
    int code = 500;
    if(file.exists()){
      code = 200;
    }
    else{
      code = 201;
    }

    try{
      response.writeRessource(request.getRessourceName(),request.getParameters());
      response.setResponseCode(code);
      response.addServerName(this.serverName);
      response.setExtension(request.getRessourceExtension());
      response.finishHeader();
      response.addRessource(request.getRessourceName());
    }
    catch(Exception E){
      response.setResponseCode(500);
    }
    response.finishHeader();

  }
}
