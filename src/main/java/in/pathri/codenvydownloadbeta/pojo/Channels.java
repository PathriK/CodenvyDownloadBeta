package in.pathri.codenvydownloadbeta.pojo;

public enum Channels {
  WORKSPACE_STATUS ("WorkspaceStatus","machine:status:_PARAM_:ws-machine",CodenvyResponseWS.class),
  PROCESS_OUTPUT ("processOutput","process:output:_PARAM_",ProcessOutput.class),
  PROCESS_STATUS ("processStatus","machine:process:_PARAM_", CodenvyResponseWS.class);
  
  String channelName;
  String channel;
  Class responseClass;
  
  private Channels(String channelName, String channel, Class responseClass){
    this.channelName = channelName;
    this.channel = channel;
    this.responseClass = responseClass;
  }
  
  public String getChannel(String param){
    return this.channel.replace("_PARAM_",param) ;
  }
  
  public Class getResponseClass(){
	  return this.responseClass;
  }
  
}
