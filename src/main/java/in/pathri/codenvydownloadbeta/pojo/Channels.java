package in.pathri.codenvydownloadbeta.Client;

public enum Channels {
  WORKSPACE_STATUS ("WorkspaceStatus","machine:status:_PARAM_:ws-machine");
  
  String channelName;
  String channel;
  
  public Channels(String channelName, String channel){
    this.channelName = channelName;
    this.channel = channel;
  }
  
  public String getChannel(String param){
    return this.channel.replace("_PARAM_",param) ;
  }
  
}
