package in.pathri.codenvydownloadbeta.pojo;

public enum Channels {
  WORKSPACE_STATUS ("WorkspaceStatus","machine:status:_PARAM_:ws-machine");
  
  String channelName;
  String channel;
  
  private Channels(String channelName, String channel){
    this.channelName = channelName;
    this.channel = channel;
  }
  
  public String getChannel(String param){
    return this.channel.replace("_PARAM_",param) ;
  }
  
}
