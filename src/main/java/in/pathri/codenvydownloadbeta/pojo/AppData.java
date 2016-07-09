package in.pathri.codenvydownloadbeta.pojo;

import java.util.UUID;  
import java.util.List;

import com.google.common.base.Joiner;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class AppData {
  private static LoginData loginData;
  private static String workspaceName;
  private static String workspaceId;
  private static String project;
  private static CommandDetails command;
  private static String buildTaskId;
  private static String apkUrl;
  private static String apkPath;  
  private static String machineId;
  private static String guidString;
  private static BuildResult buildResult;
  private static BuildStatus buildStatus;
  private static List<String> buildOutput;
  private static Table<String, String,CommandDetails> commandDetailsMap = HashBasedTable.create();
  
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

  public static CommandDetails getCommand(){
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

  public static void setCommand(String command){
    AppData.command = commandDetailsMap.get(workspaceId,command);
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
  
  public static void addCommandMap(String wid, String commandName, CommandDetails command){
    commandDetailsMap.put(wid,commandName, command);
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
