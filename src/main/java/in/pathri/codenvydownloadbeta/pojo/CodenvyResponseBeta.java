package in.pathri.codenvydownloadbeta.pojo;

import java.util.List;

import com.google.gson.JsonObject;

public class CodenvyResponseBeta implements CodenvyResponse {
  private String message;
  private String value;
  private String taskId;
  private String status;
  private List<ResourceLinks> links;
  private WorkspaceDetails workspaceReference;
  private String name;
  private String id;
  private WorkspaceDetails config;
  private RuntimeDetails runtime;
  private JsonObject attributes;
  
  public WorkspaceDetails getConfig() {
	return config;
}
public JsonObject getAttributes() {
	return attributes;
}

public String getAttribute(String key){
	return attributes.get(key).getAsString();
}

public RuntimeDetails getRuntime(){
    return this.runtime;
  }
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
      return new WorkspaceDetails(config.name,id);
  }

  public String getName(){
    return this.name;
  }
	@Override
	public int getStatusCode() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getMachineId() {
		return this.runtime.getMachine(0).getId();
	}
	public String getMachineToken() {
		return this.runtime.getEnvVariable("USER_TOKEN");
	}
	@Override
	public String getProjectURL() {
		JsonObject server = this.runtime.getServerDetail("ref", "wsagent");
		if(server != null){
			return server.get("url").getAsString();
		}
		return "";				
	}
}
