package in.pathri.codenvydownloadbeta.Client;

import java.util.List;
import java.util.Map;

import in.pathri.codenvydownloadbeta.CustomLogger;
import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.pojo.BuildResult;
import in.pathri.codenvydownloadbeta.pojo.BuildStatus;
import in.pathri.codenvydownloadbeta.pojo.Channels;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponseBeta;
import in.pathri.codenvydownloadbeta.pojo.CommandDetails;
import in.pathri.codenvydownloadbeta.pojo.LoginData;
import in.pathri.codenvydownloadbeta.responsehandlers.ApkDownloadHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.ArtifactIdResponseHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.BuildOutputHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.BuildStatusHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.MachineResponseHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.VoidResponseHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.WorkspaceStatusHandler;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public class CodenvyBetaClientAdapter implements CodenvyClientInterface<ResponseBody,CodenvyResponseBeta> {
	private static final String className = CodenvyBetaClientAdapter.class.getSimpleName();
	public boolean isBuildStatusHandlerReady = false;
	public boolean isBuildOutputHandlerReady = false;
  CodenvyBetaClient betaClient;
  
  
  	public CodenvyBetaClientAdapter(){
  		CustomLogger.i(className, "CodenvyBetaClientAdapter", "Inside Constructor");
        this.betaClient = new CodenvyBetaClient();
      }
  
    public void apiInit() {
    	CustomLogger.i(className, "apiInit", "Inside Method");
 		betaClient.apiInit();
    }
    
    public void postLogin(LoginData loginData, Callback < CodenvyResponseBeta > loginResponseHandler) {
    	CustomLogger.d(className, "postLogin", "LoginData", loginData.toString());
		betaClient.postLogin(loginData,loginResponseHandler);
    }
    
    public void buildProj(String workspaceId, String project, CommandDetails command, Callback < ResponseBody > buildResponseHandler) {
    	CustomLogger.d(className, "buildProj", "workspaceId|project|command", workspaceId + "|" + project + "|" + command.toString());
      	betaClient.getWorkspaceDetail(workspaceId, new WorkspaceStatusHandler(workspaceId, this));
//			betaClient.buildProj(workspaceId,project,command,buildResponseHandler);
    }
    
    private void readyBuildHandler(){
    	CustomLogger.i(className, "triggerBuild", "Inside Trigger Build");    	
    	AppData.generateGUID();
    	isBuildOutputHandlerReady = isBuildStatusHandlerReady = false;
    	CodenvyBetaWSClient.getInstance(AppData.getWorkspaceId(),Channels.PROCESS_OUTPUT).initChannel(AppData.getGUID(), new BuildOutputHandler(this));
    	CodenvyBetaWSClient.getInstance(AppData.getWorkspaceId(),Channels.PROCESS_STATUS).initChannel(AppData.getMachineId(), new BuildStatusHandler(this));    	    
    }
    
    public void readyBuild(){
    	this.getMachineTokenAndProjectURL();
    	this.readyBuildHandler();    	
    }
    
    public void getArtifactId() {
		String projectURL = AppData.getProjectURL();
		String machineToken = AppData.getMachineToken();
		betaClient.getArtifactId(projectURL, machineToken, new ArtifactIdResponseHandler(this));
	}
    
    public synchronized void  triggerBuild(){
    	CustomLogger.d(className, "triggerBuild", "isBuildStatusHandlerReady|isBuildOutputHandlerReady", isBuildStatusHandlerReady + "|" + isBuildOutputHandlerReady);
    	if(isBuildStatusHandlerReady && isBuildOutputHandlerReady){
    		betaClient.buildProj(AppData.getMachineId(),Channels.PROCESS_OUTPUT.getChannel(AppData.getGUID()),AppData.getCommand(), new VoidResponseHandler());
    	}
    }
    
    public void getMachineTokenAndProjectURL(){
    	betaClient.getMachineDetails(AppData.getMachineId(), new MachineResponseHandler(this));
    }
    
    public void readyWorkspaceStatusHandler(String workspaceId){
    	CustomLogger.i(className, "startWorkspace", "Inside startWorkspace");
      CodenvyBetaWSClient.getInstance(workspaceId,Channels.WORKSPACE_STATUS).initChannel(workspaceId, new WorkspaceStatusHandler(workspaceId, this));      
    }
    
    public void startWorkspace(){
    	betaClient.startWorkspace(AppData.getWorkspaceId(), new VoidResponseHandler());	
    }
    
    
    public void buildStatus(String workspaceId, String buildId, Callback < CodenvyResponseBeta > statusResponseHandler) {
    	CustomLogger.d(className, "buildStatus", "workspaceId|buildId", workspaceId + "|" + buildId);
        betaClient.buildStatus(workspaceId,buildId,statusResponseHandler);
    }
    
    public void getAPK(String machineId, String apkUrl, Callback < ResponseBody > apkDownloadHandler) {
    	CustomLogger.d(className, "getAPK", "machineId|apkPath", machineId + "|" + apkUrl);
        betaClient.getAPK(machineId, apkUrl,apkDownloadHandler);
    }
    
    public void getWorkspaceDetails(Callback < List<CodenvyResponseBeta> > workspaceResponseHandler){
    	CustomLogger.i(className, "getWorkspaceDetails", "Into Function");
      betaClient.getWorkspaceDetails(workspaceResponseHandler);
    }
    
    public void getProjectDetails(String wid,Callback < List<CodenvyResponseBeta> > projectResponseHandler){
    	CustomLogger.d(className, "getProjectDetails", "wid", wid);
		betaClient.getProjectDetails(wid,projectResponseHandler);
      
    }
  
	  public void getCommandDetails(String wid){
		  CustomLogger.d(className, "getCommandDetails", "wid", wid);
		  betaClient.getCommandDetails(wid);
	  }
	  	public String getCurrentURL(){
	     return betaClient.getCurrentURL();
	   }
	  
	  public boolean isInitialised(){
	    return betaClient.isInitialised();
	  }
	  
	  public void setWorkspaceList(Map < String, CodenvyResponseBeta > workspaceList){
		  CustomLogger.i(className, "setWorkspaceList", "setWorkspaceList called");
	    betaClient.setWorkspaceList(workspaceList);
	  }
  
	  synchronized public void checkCompletion() {
		  CustomLogger.d(className, "checkCompletion", "BuildStatus", AppData.getBuildStatus().name());
		  CustomLogger.d(className, "checkCompletion", "BuildResult", AppData.getBuildResult().name());
		if(BuildStatus.COMPLETED.equals(AppData.getBuildStatus())){
			if(BuildResult.SUCCESS.equals(AppData.getBuildResult())){
				String artifactID = AppData.getArtifactId();
				String projectURL = AppData.getProjectURL();
				String machineToken = AppData.getMachineToken();
				if(artifactID != "" && projectURL != "" & machineToken != ""){
					AppData.setApkUrl(AppData.getProjectURL() + "/project/export/file/" + AppData.getProject() + "/target/" + AppData.getArtifactId() + "." + AppData.getArtifactExt() + "?token=" + AppData.getMachineToken());
					CodenvyClient.getAPK();					
				}
			}else if(BuildResult.FAILED.equals(AppData.getBuildResult())){
				HomePageActivity.updateStatusText(AppData.getBuildOutput());
			}
		}
	}


    public void buildProj(String workspaceId, String project,
            Callback<CodenvyResponseBeta> buildResponseHandler) {
    	CustomLogger.i(className, "buildProj", "Non Command Function::Not Supported");
        
    }
	@Override
	public void updateCookie(List<String> cookies) {
		CustomLogger.i(className, "updateCookie", "Update Cookie Called");
		betaClient.updateCookie(cookies);
	}
}
