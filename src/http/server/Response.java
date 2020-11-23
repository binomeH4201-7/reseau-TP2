package http.server;

import java.util.*;
import java.nio.file.*;
import java.io.*;

public class Response {

  private int responseCode;
  private String serverName;
  private byte[] body;
  private String response;
  private final String PROTOCOL = "HTTP/1.0 ";
  private static Map<String,List<String>> typeToExtension;
  private static Map<Integer,String> codeToError;

  private static final String[]       image = {"image","gif", "png", "jpeg"};
  private static final String[]       audio = {"audio","wav"};
  private static final String[]       video = {"video","mpeg"};
  private static final String[]        text = {"text","plain", "html", "javascript"};
  private static final String[] application = {"application","json", "pdf"};
  private static final String[][]     types = {audio,image,video,text,application};

  private static final int[] codeError = {200,
                                          201,
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
    this.responseCode = code;
    this.response += code+" "+getStringError(code);
    this.response += "\n";
  }

  public void setExtension(String extension) {
    String type = this.findContentType(extension);
    this.response +="Content-Type: "+type+"/"+extension;
    this.response +="\n";
  }

  public void addServerName(String server){
    this.serverName = server;
    this.response +="Server: "+server;
    this.response +="\n";
  }

  public void addRessource(String ressourceName) throws Exception{
    this.finishHeader();

    String filePath = "./ressources"+ressourceName;
    this.body  = Files.readAllBytes(Paths.get(filePath));
  }

  public byte[] toBytes(){
    byte[] header = this.getHeader();
    byte[] responseBytes = new byte[header.length+this.body.length];
    System.arraycopy(header,0,responseBytes,0,header.length);
    System.arraycopy(this.body,0,responseBytes,header.length,this.body.length);
    return responseBytes;
  }

  public void finishHeader(){
    this.response += "\n";
  }

  private String getStringError(int code){
    return Response.codeToError.get(code);
  }

  private byte[] getHeader(){
    return this.response.getBytes();
  }

  private String findContentType(String extension){
    for(List<String> extensionsType : Response.typeToExtension.values()){
      if(extensionsType.contains(extension)){
          return extensionsType.get(0);
      }
    }
    return null;
  }

  public static void initTypes(){
    typeToExtension = new HashMap<String,List<String>>();
    for(String[] type : types){
      typeToExtension.put(type[0],new ArrayList<String>(Arrays.asList(type)));
    }
  }

  public static void initError(){
    codeToError = new HashMap<Integer,String>();
    for(int i = 0; i < codeError.length; i++){
      codeToError.put(codeError[i],messageError[i]);
    }
  }

}
