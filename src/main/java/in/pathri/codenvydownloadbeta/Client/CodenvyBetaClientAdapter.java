package in.pathri.codenvydownloadbeta.Client;

import java.util.List;
import java.util.Map;

import in.pathri.codenvydownloadbeta.pojo.Channels;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponseBeta;
import in.pathri.codenvydownloadbeta.pojo.LoginData;
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
    
    public void buildProj(String workspaceId, String project, String command, Callback < CodenvyResponseBeta > buildResponseHandler) {
      	betaClient.getWorkspaceDetail(workspaceId, new WorkspaceStatusHandler(workspaceId, this));
			betaClient.buildProj(workspaceId,project,command,buildResponseHandler);
    }
  
    public void startWorkspace(String workspaceId){
      betaWSClient = CodenvyBetaWSClient.getInstance(workspaceId,Channels.WORKSPACE_STATUS);
      betaWSClient.initChannel(workspaceId, new WorkspaceStatusHandler(workspaceId, this));
      betaClient.startWorkspace(workspaceId, new VoidResponseHandler());
    }
    
    public void buildStatus(String workspaceId, String buildId, Callback < CodenvyResponseBeta > statusResponseHandler) {
        betaClient.buildStatus(workspaceId,buildId,statusResponseHandler);
    }
    
    public void getAPK(String apkUrl, Callback < ResponseBody > apkDownloadHandler) {
        betaClient.getAPK(apkUrl,apkDownloadHandler);
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
}
