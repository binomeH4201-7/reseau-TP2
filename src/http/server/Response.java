package http.server;

import java.util.*;
import java.nio.file.*;
import java.io.*;

public class Response {

  private byte[] body;
  private String response;
  private final String PROTOCOL = "HTTP/1.0 ";
  private static Map<Integer,String> codeToError;

  private static final int[] codeError = {200,
                                          201,
                                          204,
                                          400,
                                          401,
                                          403,
                                          404,
                                          415,
                                          500,
                                          501,
                                          503};
  private static final String[] messageError = {"OK",
                                                "Created",
                                                "No Content",
                                                "Bad Request",
                                                "Unauthorized",
                                                "Forbidden",
                                                "Not Found",
                                                "Unsupported Media Type",
                                                "Internal Server Error",
                                                "Not Implemented",
                                                "Service Unavailable"};

  public Response(){
    this.response = PROTOCOL;
  }
  
  public void setResponseCode(int code){
    this.response += code+" "+getStringError(code);
    this.response += "\n";
  }

  public void setExtension(String extension) {
    String type = this.findContentType(extension);
    this.response +="Content-Type: "+type+"/"+extension;
    this.response +="\n";
  }

  public void setExtension(String contentType, String extension) {
    this.response +="Content-Type: "+contentType+"/"+extension;
    this.response +="\n";
  }

  public void addServerName(String server){
    this.response +="Server: "+server;
    this.response +="\n";
  }

  public void addHTTPAllowedMethods(List<RequestHandler.Method> methods){
    this.response +="Allow: ";
    int i=1;
    int nbMethods = methods.size();
    for(RequestHandler.Method m: methods){
      this.response+=m.name();
      if(i<nbMethods){
        this.response+=", ";
      }
      i++;
    }
    this.response +="\n";
  }

  public void addRessource(String ressourceName) throws IOException{
    String filePath = "./ressources"+ressourceName;
    this.body  = Files.readAllBytes(Paths.get(filePath));
  }
  
  public void writeRessource(String ressourceName, String content) throws IOException{
    BufferedWriter outFile = new BufferedWriter(new FileWriter("./ressources"+ressourceName));
    outFile.write(content);
    outFile.close();
  }
  
  public void writeRessource(String ressourceName, Map<String,String> content) throws IOException{
    BufferedWriter outFile = new BufferedWriter(new FileWriter("./ressources"+ressourceName,true));
    for(Map.Entry<String,String> entry : content.entrySet()){
      outFile.append("key:"+entry.getKey()+"  value:"+entry.getValue());
    }
    outFile.append("\n");
    outFile.close();
  }


  public boolean deleteRessource(String ressourceName) {
    File file = new File("./ressources"+ressourceName);
    return file.delete();
  }


  public byte[] toBytes(){
    this.finishHeader();
    byte[] header = this.getHeader();
    byte[] responseBytes = new byte[header.length+this.body.length];
    System.arraycopy(header,0,responseBytes,0,header.length);
    System.arraycopy(this.body,0,responseBytes,header.length,this.body.length);
    return responseBytes;
  }

  private void finishHeader(){
    this.response += "\n";
  }

  private String getStringError(int code){
    return Response.codeToError.get(code);
  }

  private byte[] getHeader(){
    return this.response.getBytes();
  }

  public static void initError(){
    codeToError = new HashMap<Integer,String>();
    for(int i = 0; i < codeError.length; i++){
      codeToError.put(codeError[i],messageError[i]);
    }
  }

}
