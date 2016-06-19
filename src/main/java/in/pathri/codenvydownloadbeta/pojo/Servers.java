package in.pathri.codenvydownloadbeta.pojo;

public enum Servers {
    PRODUCTION("Production"),
    BETA("Beta");
    
    String serverName;
    Servers(String serverName){
      this.serverName = serverName;
    }
    
    public static String[] getServerNames() {
    Servers[] servers = values();
    String[] names = new String[servers.length];

    for (int i = 0; i < servers.length; i++) {
        names[i] = servers[i].serverName;
    }

    return names;
}
  
  public static String[] getServerValues(){
       Servers[] servers = values();
    String[] names = new String[servers.length];

    for (int i = 0; i < servers.length; i++) {
        names[i] = servers[i].name();
    }

    return names;
  }
  
  public static String getDefaultValue(){
    return PRODUCTION.name();
  }
  
    public static String getDefaultEntry(){
    return PRODUCTION.serverName;
  }

  }