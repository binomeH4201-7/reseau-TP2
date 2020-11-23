package http.server;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class Request {
    enum Method {GET,POST,PUT,DELETE,OPTIONS,HEAD}

    private Method HTTPMethod;
    private String ressourceName;
    private String ressourceExtension;
    private String protocol;
    private String host;
    private String contentType;
    private String contentLength;
    private String content; //for PUT method
    private HashMap<String,String> parameters; //for POST method

    /* Construit l’objet Request à partir de la liste de String*/
    public Request(List<String> request){
        ListIterator<String> it = request.listIterator(3);

        String[] firstLine = request.get(0).split(" ",3);
        HTTPMethod = Method.valueOf(firstLine[0]);
        ressourceName = firstLine[1];
        protocol = firstLine[2];

        String[] hostLine = request.get(1).split(" ",2);
        host = hostLine[1];

        String[] contentTypeLine = request.get(2).split(" ",2);
        contentType = contentTypeLine[1];

        if(request.size()>3 && !request.get(3).isEmpty()){
            String[] contentlengthLine = it.next().split(" ",2);
            contentLength = contentlengthLine[1];
        }
        it.next();

        switch(HTTPMethod){
            case GET :
                ressourceExtension = ressourceName.substring(ressourceName.lastIndexOf(".")+1);
                break;
            case POST:
                while(it.hasNext()){
                    String line;
                    if(!(line = it.next()).isEmpty()){
                        String[] parametersPair = line.split("&");
                        for(String p : parametersPair ){
                            String[] pair = p.split("=");
                            parameters.put(pair[0],pair[1]);
                        }
                    }
                }
                break;
            case PUT:
                while(it.hasNext()){
                    content = "";
                    String line;
                    if(!(line = it.next()).isEmpty()){
                        content+=line;
                    }
                }
                break;
            default :
                break;
        }
    }

    /*Renvoie la méthode HTTP de la requête*/
    public Method getHTTPMethod() {
        return HTTPMethod;
    }

    /*Renvoie le nom de la ressource mentionnée dans la requête*/
    public String getRessourceName() {
        return ressourceName;
    }

    /*Renvoie le nom de la ressource mentionnée dans la requête*/
    public String getRessourceExtension() {
        return ressourceExtension;
    }

    public String getContentType() {
        return contentType;
    }

    /*Renvoie une HashMap des paramètres passés dans la requête POST*/
    public HashMap<String, String> getParameters() {
        return parameters;
    }

    /*Renvoie le contenu d'une requête PUT à inserer*/
    public String getContent() {
        return content;
    }
}
