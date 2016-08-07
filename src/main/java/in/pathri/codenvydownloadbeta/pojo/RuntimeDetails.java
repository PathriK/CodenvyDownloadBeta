package in.pathri.codenvydownloadbeta.pojo;

import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class RuntimeDetails {
  private List<MachineDetails> machines;
  private JsonObject servers;
  private JsonObject envVariables;
  
  public List<MachineDetails> getMachies(){
    return machines;
  }

public MachineDetails getMachine(int i) {
	return machines.get(i);
}

public JsonObject getServerDetail(String searchKey, String searchValue){
	JsonObject servers = this.servers;	
	for(Entry<String, JsonElement> server: servers.entrySet()){
		String ref = server.getValue().getAsJsonObject().get(searchKey).getAsString();
		if(ref.equalsIgnoreCase(searchValue)){
			return server.getValue().getAsJsonObject();
		}
	}
	return null;
}

public String getEnvVariable(String key){
	return this.envVariables.get(key).getAsString();
}
}
