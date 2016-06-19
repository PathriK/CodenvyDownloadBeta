package in.pathri.codenvydownloadbeta.pojo;

import in.pathri.codenvydownloadbeta.Client.CodenvyClient;

import java.util.List;

public class CodenvyResponseProd implements CodenvyResponse {
  private String message;
  private String value;
  private String taskId;
  private String status;
  private List<ResourceLinks> links;
  private WorkspaceDetails workspaceReference;
  private String name;
  private String id;
  private WorkspaceDetails config;
  
  public WorkspaceDetails getWorkspaceConfig(){
    return this.config;
  }
  
  public String getId(){
    return this.id;
  }
  
  public String getMessage(){
    return this.message;
  }
  
  public String getValue(){
    return this.value;
  }

  public String getTaskId(){
    return this.taskId;
  }

  public String getStatus(){
    return this.status;
  }

  public List<ResourceLinks> getLinks(){
    return this.links;
  }

  public WorkspaceDetails getWorkspaceReference(){
    return this.workspaceReference;
  }

  public String getName(){
    return this.name;
  }

}
