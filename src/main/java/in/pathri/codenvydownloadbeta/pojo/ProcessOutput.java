package in.pathri.codenvydownloadbeta.pojo;

import java.util.List;

import com.google.gson.JsonObject;

public class ProcessOutput implements CodenvyResponse {
  private int responseCode;
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
  
  public int getStatusCode(){
    return this.responseCode;
  }
@Override
public String getMachineId() {
	return "";
}
@Override
public String getMachineToken() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public String getProjectURL() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public JsonObject getAttributes() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public String getAttribute(String key) {
	// TODO Auto-generated method stub
	return null;
}

}
