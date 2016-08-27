package in.pathri.codenvydownloadbeta.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.base.Joiner;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import in.pathri.codenvydownloadbeta.CustomLogger;
import in.pathri.codenvydownloadbeta.typeadapters.TableGsonAdapter;

import com.mukesh.tinydb.TinyDB;

public class AppData {
	private static final String className = AppData.class.getSimpleName();
	
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
  private static String machineToken;
  private static String projectURL;
  private static String artifactId;
  private static String artifactExt;



//  {
//	  AppData.clearAll();
//  }
  public static LoginData getLoginData(){
    return loginData;
  }

  public static String getWorkspaceName(){
    return workspaceName;
  }

  public static String getWorkspaceId(){
	  CustomLogger.d(className, "getWorkspaceId", "workspaceId", workspaceId);
    return workspaceId;
  }

  public static String getProject(){
    return project;
  }

  public static CommandDetails getCommand(){
	  if(command != null){
		  return command.getInstance(project);
	  }else{
		  return new CommandDetails("","","");
	  }    
  }

  public static String getBuildTaskId(){
    return buildTaskId;
  }

  public static String getApkUrl(){
	  CustomLogger.d(className, "getApkUrl", "apkUrl", apkUrl);
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
//	  return guidString;
	  return "491d48b8-b1ae-4f8b-b5b8-348c7fdec50e".toUpperCase();
  }
  
  public static BuildResult getBuildResult() {
	  CustomLogger.d(className, "getBuildResult", "buildResult", buildResult.toString());
	  return buildResult;	
  }
  
  public static BuildStatus getBuildStatus() {
	  return buildStatus;
  }
  
  public static String getBuildOutput(){
	  String output = Joiner.on(System.getProperty("line.separator")).join(buildOutput);
	  CustomLogger.d(className, "getBuildOutput", "output", output);
	  return output;	  
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
    CustomLogger.d(className, "setCommand", "command|commandDetail", command + "|" + AppData.command);
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
	  CustomLogger.d(className, "addBuildOutput", "line", line);
	  AppData.buildOutput.add(line);
  }
  
  public static void addCommandMap(String wid, String commandName, CommandDetails command){
	  CustomLogger.d(className, "addCommandMap", "wid::commandName::command=", wid + "::" + commandName  + "::" + command);
    try{
    	commandDetailsMap.put(wid,commandName, command);
    	CustomLogger.d(className, "addCommandMap", "commandDetailsMap", commandDetailsMap.toString());
    } catch(Exception e){
    	CustomLogger.e(className, "addCommandMap", "error::", e);
    }
	  
  }
  
  //Generators
  public static void generateGUID(){
	   UUID uuid = UUID.randomUUID();
	   AppData.guidString = uuid.toString();
  }
  
  public static void clearAll(){
	  CustomLogger.i(className, "ClearAll", "Into Function");
	  workspaceName=workspaceId=project="";
	  loginData = new LoginData("", "");
	  command = new CommandDetails("", "", "");
	  commandDetailsMap.clear();
	  commandDetailsMap = HashBasedTable.create();
	  clearBuildData();
  }
  
  public static void clearBuildData(){
	  CustomLogger.i(className, "clearBuildData", "Into Function");
	  buildTaskId=apkUrl=apkPath=machineId=guidString=machineToken=projectURL=artifactId=artifactExt="";
	  if(buildOutput != null){
		  buildOutput.clear();
	  }else{
       buildOutput = new ArrayList<String>();
     }
	  buildResult = BuildResult.NOT_SET;
	  buildStatus = BuildStatus.NOT_STARTED;	  	  
  }

public static String getMachineToken() {
	return machineToken;
}

public static void setMachineToken(String machineToken) {
	AppData.machineToken = machineToken;
}

public static void setProjectURL(String projectURL) {
	AppData.projectURL = projectURL;
}

public static String getProjectURL() {
	return projectURL;
}

public static String getArtifactId() {
	return artifactId;
}

public static void setArtifactId(String artifactId) {
	AppData.artifactId = artifactId;
}

public static String getArtifactExt() {
	return artifactExt;
}

public static void setArtifactExt(String artifactExt) {
	AppData.artifactExt = artifactExt;
}

public static void onPause(Context context){
	CustomLogger.i(className, "onPause", "into onPause");
	GsonBuilder gsonBuilder  = new GsonBuilder();
    gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
    gsonBuilder.registerTypeAdapter(Table.class, new TableGsonAdapter());
    Gson gson = gsonBuilder.create();
	TinyDB tinydb = new TinyDB(context);
	tinydb.putObject("AppData",new AppData(),gson);
}

public static void onResume(Context context){
	CustomLogger.i(className, "onResume", "into onResume");
  	GsonBuilder gsonBuilder  = new GsonBuilder();
    gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
    gsonBuilder.registerTypeAdapter(Table.class, new TableGsonAdapter());
    Gson gson = gsonBuilder.create();
	TinyDB tinydb = new TinyDB(context);
	try{
		if(tinydb.contains("AppData")){
			tinydb.getObject("AppData",AppData.class,gson);	
		}else{
			CustomLogger.i(className, "onResume", "AppData Key not present");
		}
		
	} catch(Exception e){
		CustomLogger.e(className, "onResume", "AppData Restore", e);
	}
	
}

}
