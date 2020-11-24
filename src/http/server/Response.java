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
  private static Map<String,List<String>> typeToExtension;
  private static Map<Integer,String> codeToError;

  private static final String[]       image = {"image","gif", "png", "jpeg"};
  private static final String[]       audio = {"audio","wav"};
  private static final String[]       video = {"video","mpeg"};
  private static final String[]        text = {"text","plain", "html"};
  private static final String[] application = {"application","json", "pdf"};
  private static final String[][]     types = {audio,image,video,text,application};

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

  public Response(){
    this.response = PROTOCOL;
    this.parameters = new HashMap<String,String>();
    this.allowParameters = null;
  }

  public void setResponseCode(int code){
    this.response += code+" "+getStringError(code);
    this.response += "\n";
  }

  public void setExtension(String extension) {
    String type = this.findContentType(extension);
    this.parameters.put("Content-type",type+"/extension");
  }

  public void addServerName(String server){
    this.parameters.put("Server",server);
  }

  public void addHTTPAllowedMethods(List<String> methods){
    this.allowParameters = methods;
    this.response +="Allow: ";
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
    byte[] header = this.getHeader();
    byte[] responseBytes = new byte[header.length+this.body.length];
    System.arraycopy(header,0,responseBytes,0,header.length);
    System.arraycopy(this.body,0,responseBytes,header.length,this.body.length);
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
      int nbMethods = allowParameters.size();
      for(String m: allowParameters){
        header +=m;
        if(i<nbMethods){
          header +=", ";
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
