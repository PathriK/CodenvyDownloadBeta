package in.pathri.codenvydownloadbeta.pojo;

import java.util.List;

public class ProcessOutput implements CodenvyResponse {
  private String responseCode;
  private String body;
  
  public RuntimeDetails getRuntime(){
    return null;
  }
  public WorkspaceDetails getWorkspaceConfig(){
    return null;
  }
  
  public String getId(){
    return "";
  }
  
  public String getMessage(){
    return this.body;
  }
  
  public String getValue(){
    return "";
  }

  public String getTaskId(){
    return "";
  }

  public String getStatus(){
//     return this.responseCode;
    return "";
  }

  public List<ResourceLinks> getLinks(){
    return null;
  }

  public WorkspaceDetails getWorkspaceReference(){
      return null;
  }

  public String getName(){
    return "";
  }
  
  public String getStatusCode(){
    return this.responseCode;
  }

}
