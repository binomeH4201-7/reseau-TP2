package http.server;

import java.util.*;
import java.nio.file.*;
import java.io.*;

public class Response {

  private byte[] body;
  private String response;
  private final String PROTOCOL = "HTTP/1.0 ";
  private Map<String,String> parameters;
  private List<String> allowParameters;

  private static final int[] codeError = {
    200,
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
  private static final String[] messageError = {
    "OK",
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
  private static Map<Integer,String> codeToError;
  
  public Response(){
    this.response = PROTOCOL;
    this.parameters = new HashMap<String,String>();
    this.allowParameters = null;
    this.body = new byte[0];
  }

  public void setResponseCode(int code){
    this.response += code+" "+getStringError(code);
  }

  public void setExtension(String contentType, String extension) {
    this.parameters.put("Content-type",contentType+"/"+extension);
  }

  public void addServerName(String server){
    this.parameters.put("Server",server);
  }

  public void addHTTPAllowedMethods(List<String> methods){
    this.allowParameters = methods;
  }

  public void addRessource(String ressourceName) throws IOException{
    String filePath = "./ressources"+ressourceName;
    this.body  = Files.readAllBytes(Paths.get(filePath));
  }

  public void writeRessource(String ressourceName, Map<String,String> content) throws IOException{
    BufferedWriter outFile = new BufferedWriter(new FileWriter("./ressources"+ressourceName));
    System.out.println("content: "+content);
    for(Map.Entry<String,String> entry : content.entrySet()){
      outFile.write("key:"+entry.getKey()+"  value:"+entry.getValue()+"\n");
    }
    outFile.close();
  }

  public void writeRessource(String ressourceName, Map<String,String> content, boolean append) throws IOException{
    BufferedWriter outFile = new BufferedWriter(new FileWriter("./ressources"+ressourceName,append));
    for(Map.Entry<String,String> entry : content.entrySet()){
      outFile.append("key:"+entry.getKey()+"  value:"+entry.getValue()+"\n");
    }
    outFile.close();
  }


  public boolean deleteRessource(String ressourceName) {
    File file = new File("./ressources"+ressourceName);
    return file.delete();
  }


  public byte[] toBytes(){
    byte[] header = this.getHeader();
    byte[] responseBytes = new byte[header.length+this.body.length];
    System.arraycopy(header,0,responseBytes,0,header.length);
    if(this.body.length>0){
      System.arraycopy(this.body,0,responseBytes,header.length,this.body.length);
    }
    return responseBytes;
  }

  private String getStringError(int code){
    return Response.codeToError.get(code);
  }

  private byte[] getHeader(){
    String header = this.completeHeader()
      +"\n";
    return header.getBytes();
  }

  private String completeHeader(){
    String header = this.response
                  + "\n";
    if(this.allowParameters != null){
      int i=1;
      header += "Allow: ";
      int nbMethods = this.allowParameters.size();
      for(String m: allowParameters){
        header+=m;
        if(i<nbMethods){
          header+=", ";
        }
        i++;
      }
      header +="\n";
    }
    for(Map.Entry<String,String> entry : this.parameters.entrySet()){
      header += entry.getKey()+": "+entry.getValue()+"\n";
    }
    return header;
  }

  public static void initError(){
    codeToError = new HashMap<Integer,String>();
    for(int i = 0; i < codeError.length; i++){
      codeToError.put(codeError[i],messageError[i]);
    }
  }

}
