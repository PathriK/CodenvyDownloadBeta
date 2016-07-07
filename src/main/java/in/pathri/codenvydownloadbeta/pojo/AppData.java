package in.pathri.codenvydownloadbeta.pojo;

import java.util.UUID;  

import java.util.List;

import com.google.common.base.Joiner;

public class AppData {
  private static LoginData loginData;
  private static String workspaceName;
  private static String workspaceId;
  private static String project;
  private static CommandData command;
  private static String buildTaskId;
  private static String apkUrl;
  private static String apkPath;  
  private static String machineId;
  private static String guidString;
  private static BuildResult buildResult;
  private static BuildStatus buildStatus;
  private static List<String> buildOutput;
  
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

  public static CommandData getCommand(){
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
  
  public static String getMachineId(){
	  return machineId;	  
  }
  
  public static String getGUID(){
	  return guidString;
  }
  
  public static BuildResult getBuildResult() {
	  return buildResult;	
  }
  
  public static BuildStatus getBuildStatus() {
	  return buildStatus;
  }
  
  public static String getBuildOutput(){
	  return Joiner.on(System.getProperty("line.separator")).join(buildOutput);
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

  public static void setCommand(CommandData command){
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

  public static void setMachineId(String machineId){
	    AppData.machineId = machineId;
  }

  public static void setBuildResult(BuildResult buildResult) {
		AppData.buildResult = buildResult;	
  }

  public static void setBuildStatus(BuildStatus buildStatus) {
		AppData.buildStatus = buildStatus;	
  }
  
  public static void addBuildOutput(String line){
	  AppData.buildOutput.add(line);
  }
  
  //Generators
  public static void generateGUID(){
	   UUID uuid = UUID.randomUUID();
	   AppData.guidString = uuid.toString();
  }
  
  public static void clearAll(){
	  workspaceName=workspaceId=project=buildTaskId=apkUrl=apkPath=machineId=guidString="";
	  buildOutput.clear();
	  buildResult = BuildResult.NOT_SET;
	  buildStatus = BuildStatus.NOT_STARTED;
	  loginData = null;
	  command = null;
  }



}
