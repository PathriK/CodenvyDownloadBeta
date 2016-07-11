package in.pathri.codenvydownloadbeta.pojo;

public class Body {
  private String eventType;
  private String machineId;
  private String channel;
  
  public String getMachineId(){
	  return machineId;
  }
  
  public String getEventType(){
    return eventType;
  }
  
  public Body(String eventType, String machineId, String channel){
	  this.channel = channel;
	  this.eventType = eventType;
	  this.machineId = machineId;
  }
  
  @Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{channel:" + this.channel + ",eventType:" + this.eventType + ",machineId:" + this.machineId + "}";
	}
  
}
