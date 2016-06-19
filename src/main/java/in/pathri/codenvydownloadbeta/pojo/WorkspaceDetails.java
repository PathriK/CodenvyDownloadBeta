package in.pathri.codenvydownloadbeta.pojo;

import java.util.List;

public class WorkspaceDetails {
  public String name;
  public String id;
  public List<ProjectDetails> projects;
  public List<CommandDetails> commands;
  
  public WorkspaceDetails(String name,String id){
    this.name = name;
    this.id = id;
  }
}
