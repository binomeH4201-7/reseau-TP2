package http.server;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class Request {

    private String HTTPMethod;
    private String ressourceName;
    private String ressourceExtension;
    private String protocol;
    private String host;
    private String contentType;
    private String contentLength;
    private String content; //for PUT method
    private HashMap<String,String> parameters; //for POST and PUT method
    private HashMap<String,String> header;

    /* Construit l’objet Request à partir de la liste de String*/
    public Request(List<String> request){
        parameters = new HashMap<>();
        header = new HashMap<>();
        for(String param : request){
          String pair[] = param.split(": ",2);
          if(pair.length == 2)
            header.put(pair[0],pair[1]);
        }
        String[] firstLine = request.get(0).split(" ",3);
        HTTPMethod = String.valueOf(firstLine[0]);
        ressourceName = firstLine[1];
        protocol = firstLine[2];

        String[] hostLine = request.get(1).split(" ",2);
        host = hostLine[1];
        
        contentType = header.get("Content-Type");
        System.out.println("Content-type: "+contentType);
        
        contentLength = header.get("Content-Length");
        System.out.println("Content-length: "+contentLength);

        switch(HTTPMethod){
            case "HEAD" :
            case "OPTIONS" :
            case "POST":
            case "GET" :
                ressourceExtension = ressourceName.substring(ressourceName.lastIndexOf(".")+1);
                break;
            default :
                break;
        }
    }

    public void setParameters(String parametersString){
            String[] parametersLine = parametersString.split("\n");
            System.out.println(parametersLine.toString());
            for(String line:parametersLine){
                String[] parametersPair = line.split("&");
                System.out.println(parametersPair.toString());
                for(String p : parametersPair ) {
                    String[] pair = p.split("=",2);
                    System.out.println(pair.toString());
                    this.parameters.put(pair[0], pair[1]);
                }
            }
    }

    /*Renvoie la méthode HTTP de la requête*/
    public String getHTTPMethod() {
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

    public int getContentLength() {
        return Integer.parseInt(contentLength);
    }
}
