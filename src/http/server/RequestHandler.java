package http.server;

import java.util.List;

public class RequestHandler {
    private Request request;
    /**private Response response;

    /*Constructeur, instancie l’objet request avec l’objet en paramètre de RequestHandler*/
    public RequestHandler(List<String> request){
        this.request = new Request(request);
    }

    /*f(request) = response. Renvoie la réponse sous forme de bytes pour être directement envoyé*/
    /**public Byte[] handleRequest(){
        d
        return response.toBytes();
    }**/

}
