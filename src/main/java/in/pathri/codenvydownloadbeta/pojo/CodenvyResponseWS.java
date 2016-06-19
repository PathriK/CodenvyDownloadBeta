package in.pathri.codenvydownloadbeta.pojo;

import in.pathri.codenvydownloadbeta.Client.CodenvyClient;

import java.util.List;

public class CodenvyResponseWS implements CodenvyResponse {
  private String responseCode;
  private Body body;
  
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
    return "";
  }
  
  public String getValue(){
    return "";
  }

  public String getTaskId(){
    return "";
  }

  public String getStatus(){
//     return this.responseCode;
    return this.body.getEventType();
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
