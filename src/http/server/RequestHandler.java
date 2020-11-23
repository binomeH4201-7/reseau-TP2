package http.server;

import java.util.List;

public class RequestHandler {
    private Request request;
    private Response response;
    private String serverName;

    /*Constructeur, instancie l’objet request avec l’objet en paramètre de RequestHandler*/
    public RequestHandler(List<String> request, String serverName){
        this.request = new Request(request);
        response = new Response();
        this.serverName = serverName;
    }

    /*f(request) = response. Renvoie la réponse sous forme de bytes pour être directement envoyé*/
    public byte[] handleRequest(){
        try {
            switch (request.getHTTPMethod()) {
                case GET:
                    break;
                case POST:
                    break;
                case PUT:
                    break;
                case DELETE:
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

}
