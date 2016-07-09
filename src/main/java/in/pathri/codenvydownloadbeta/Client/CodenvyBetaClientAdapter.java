package in.pathri.codenvydownloadbeta.Client;

import java.util.List;
import java.util.Map;

import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.pojo.BuildResult;
import in.pathri.codenvydownloadbeta.pojo.BuildStatus;
import in.pathri.codenvydownloadbeta.pojo.Channels;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponseBeta;
import in.pathri.codenvydownloadbeta.pojo.CommandDetails;
import in.pathri.codenvydownloadbeta.pojo.LoginData;
import in.pathri.codenvydownloadbeta.responsehandlers.ApkDownloadHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.BuildOutputHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.BuildStatusHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.VoidResponseHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.WorkspaceStatusHandler;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public class CodenvyBetaClientAdapter implements CodenvyClientInterface<ResponseBody,CodenvyResponseBeta> {
  CodenvyBetaClient betaClient;
  CodenvyBetaWSClient betaWSClient;
  public CodenvyBetaClientAdapter(){
        this.betaClient = new CodenvyBetaClient();
      }
    public void apiInit() {
 		betaClient.apiInit();
    }
    
    public void postLogin(LoginData loginData, Callback < CodenvyResponseBeta > loginResponseHandler) {
		betaClient.postLogin(loginData,loginResponseHandler);
    }
    
    public void buildProj(String workspaceId, String project, CommandDetails command, Callback < ResponseBody > buildResponseHandler) {
      	betaClient.getWorkspaceDetail(workspaceId, new WorkspaceStatusHandler(workspaceId, this));
//			betaClient.buildProj(workspaceId,project,command,buildResponseHandler);
    }
    
    public void triggerBuild(){
    	betaWSClient.closeChannel();
    	CodenvyBetaWSClient processOutputWS = CodenvyBetaWSClient.getInstance(AppData.getWorkspaceId(),Channels.PROCESS_OUTPUT);
    	CodenvyBetaWSClient processStatusWS = CodenvyBetaWSClient.getInstance(AppData.getWorkspaceId(),Channels.PROCESS_STATUS);

    	AppData.generateGUID();
    	
    	processOutputWS.initChannel(AppData.getGUID(), new BuildOutputHandler(this));
    	processStatusWS.initChannel(AppData.getMachineId(), new BuildStatusHandler(this));
    	
    	betaClient.buildProj(AppData.getMachineId(),"",AppData.getCommand(), new VoidResponseHandler());
    	
    }
  
    public void startWorkspace(String workspaceId){
      betaWSClient = CodenvyBetaWSClient.getInstance(workspaceId,Channels.WORKSPACE_STATUS);
      try {
        Thread.sleep(5000);
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
      betaWSClient.initChannel(workspaceId, new WorkspaceStatusHandler(workspaceId, this));
      betaClient.startWorkspace(workspaceId, new VoidResponseHandler());
    }
    
    public void buildStatus(String workspaceId, String buildId, Callback < CodenvyResponseBeta > statusResponseHandler) {
        betaClient.buildStatus(workspaceId,buildId,statusResponseHandler);
    }
    
    public void getAPK(String machineId, String apkUrl, Callback < ResponseBody > apkDownloadHandler) {
        betaClient.getAPK(machineId, apkUrl,apkDownloadHandler);
    }
    
    public void getWorkspaceDetails(Callback < List<CodenvyResponseBeta> > workspaceResponseHandler){
      betaClient.getWorkspaceDetails(workspaceResponseHandler);
    }
    
    public void getProjectDetails(String wid,Callback < List<CodenvyResponseBeta> > projectResponseHandler){

		betaClient.getProjectDetails(wid,projectResponseHandler);
      
    }
  
	  public void getCommandDetails(String wid){
		  betaClient.getCommandDetails(wid);
	  }
	  	public String getCurrentURL(){
	     return betaClient.getCurrentURL();
	   }
	  
	  public boolean isInitialised(){
	    return betaClient.isInitialised();
	  }
	  
	  public void setWorkspaceList(Map < String, CodenvyResponseBeta > workspaceList){
	    betaClient.setWorkspaceList(workspaceList);
	  }
  
	  synchronized public void checkCompletion() {
		if(BuildStatus.COMPLETED.equals(AppData.getBuildStatus())){
			if(BuildResult.SUCCESS.equals(AppData.getBuildResult())){
				CodenvyClient.getAPK();
			}else if(BuildResult.FAILED.equals(AppData.getBuildResult())){
           HomePageActivity.updateStatusText(AppData.getBuildOutput());
			}
		}
	}


    public void buildProj(String workspaceId, String project,
            Callback<CodenvyResponseBeta> buildResponseHandler) {
        System.out.println("Not SUpported");
        
    }
}
