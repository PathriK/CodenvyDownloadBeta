package in.pathri.codenvydownloadbeta.pojo;

import java.util.List;

public interface CodenvyResponse {
   public WorkspaceDetails getWorkspaceConfig();
  
  public String getId();
  
  public String getMachineId();
  
  public String getMessage();
  
  public String getValue();

  public String getTaskId();

  public String getStatus();

  public List<ResourceLinks> getLinks();
  
  public WorkspaceDetails getWorkspaceReference();

  public String getName();
  
  public int getStatusCode();

}
