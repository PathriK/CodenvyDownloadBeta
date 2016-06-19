package in.pathri.codenvydownloadbeta.pojo;

public class AppData {
  private static LoginData loginData;
  private static String workspaceName;
  private static String workspaceId;
  private static String project;
  private static String command;
  private static String buildTaskId;
  private static String apkUrl;
  private static String apkPath;  
  
  public static LoginData getLoginData(){
    return loginData;
  }

  public static String getWorkspaceName(){
    return workspaceName;
  }

  public static String getWorkspaceId(){
    return workspaceId;
  }

  public static String getProject(){
    return project;
  }

  public static String getCommand(){
    return command;
  }

  public static String getBuildTaskId(){
    return buildTaskId;
  }

  public static String getApkUrl(){
    return apkUrl;
  }

  public static String getApkPath(){
    return apkPath;
  }
  
  public static String getApkFileName(){
    return "CodenvyDownload-" + buildTaskId + ".apk";
  }
  
//Setters  
  public static void setLoginData(LoginData loginData){
    AppData.loginData = loginData;
  }
  
  public static void setWorkspaceName(String workspaceName){
    AppData.workspaceName = workspaceName;
  }

  public static void setWorkspaceId(String workspaceId){
    AppData.workspaceId = workspaceId;
  }

  public static void setProject(String project){
    AppData.project = project;
  }

  public static void setCommand(String command){
    AppData.command = command;
  }

  public static void setBuildTaskId(String buildTaskId){
    AppData.buildTaskId = buildTaskId;
  }  

  public static void setApkUrl(String apkUrl){
    AppData.apkUrl = apkUrl;
  }  

  public static void setApkPath(String apkPath){
    AppData.apkPath = apkPath;
  }   
  
}
