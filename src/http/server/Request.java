package http.server;

import java.util.HashMap;
import java.util.List;

/**
 * Class representing a request.
 * Interprets the request strings received as a java Object.
 *
 * @author BUONOMO Phanie, BATEL Arthur
 */
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

    /**
     * Build Request object from a String list of request lines
     *
     * @param request list of the request lines
     */
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

    /**
     * Interpret the body of a request as parameters and save them
     *
     * @param parametersString request body String containing the parameters
     */
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

    /**
     * Give the HTTP method form the request
     *
     * @return String representing the HTTP method
     */
    public String getHTTPMethod() {
        return HTTPMethod;
    }

    /**
     * Give the name of the ressource mentioned in the request
     *
     * @return String of the ressource name
     */
    public String getRessourceName() {
        return ressourceName;
    }

    /**
     * Give the extension of the ressource
     *
     * @return String of extension
     */
    public String getRessourceExtension() {
        return ressourceExtension;
    }

    /**
     * Give the content-type passed in the request
     *
     * @return String of the content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Give the MIME the HashMap containing <key,value> pairs of parameters passed in the request body
     *
     * @return HashMap<String,String> of parameters
     */
    public HashMap<String, String> getParameters() {
        return parameters;
    }

    /**
     * Give the content of a PUT request to insert in the ressources
     *
     * @return String of the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Return the content-length of the request body, passed in the header
     *
     * @return int : the number of char in the body of the request
     */
    public int getContentLength() {
        return Integer.parseInt(contentLength);
    }
}
