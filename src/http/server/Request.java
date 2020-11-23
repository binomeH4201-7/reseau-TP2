package http.server;

public class Request {
    private String requestString;
    private String method;
    private String ressource;
    private String protocol;
    private String host;
    private String contentType;
    private String contentLength;
    private String body;

    public void Header(String request){
        requestString = request;
        String[] lines = requestString.split("\n",4);

        String[] firstLine = lines[0].split(" ",3);
        method = firstLine[0];
        ressource = firstLine[1];
        protocol = firstLine[2];

        String[] hostLine = lines[0].split(" ",2);
        host = hostLine[1];

        String[] contentTypeLine = lines[0].split(" ",2);
        contentType = contentTypeLine[1];

        if(lines.length>3){
            String[] contentlengthLine = lines[0].split(" ",2);
            contentLength = contentlengthLine[1];
        }
    }


}
