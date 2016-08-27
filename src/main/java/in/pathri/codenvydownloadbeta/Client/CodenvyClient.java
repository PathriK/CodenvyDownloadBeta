package in.pathri.codenvydownloadbeta.Client;

import java.util.List;
import java.util.Map;

import android.util.Log;
import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponseBeta;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponseProd;
import in.pathri.codenvydownloadbeta.pojo.Servers;
import in.pathri.codenvydownloadbeta.preferancehandlers.SetupActivity;
import in.pathri.codenvydownloadbeta.responsehandlers.ApkDownloadHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.BuildResponseHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.LoginDialogResponseHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.LoginRefreshHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.LoginResponseHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.ProjectResponseHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.StatusResponseHandler;
import in.pathri.codenvydownloadbeta.responsehandlers.WorkspaceResponseHandler;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public class CodenvyClient {
	private static final String className = CodenvyClient.class.getSimpleName();
  static CodenvyClientInterface currentInstance;
  static CodenvyClientInterface prodInstance;
  static CodenvyClientInterface betaInsatnce;
  static List < CodenvyResponse > workspaceList;
  static Servers serverName;
  static String TAG = "CodenvyBeta";
  static {
    if(prodInstance == null){
      prodInstance = new CodenvyProdClient();
      Log.d(TAG,"Prod Instance Set");
    }
    if(betaInsatnce == null){
      betaInsatnce = new CodenvyBetaClientAdapter();
      Log.d(TAG,"Beta Instance Set");
    }
    currentInstance = prodInstance;
    Log.d(TAG,"Current Instance Set");
  }
  
  public static void switchServer(Servers serverName){
    if(serverName == Servers.PRODUCTION){
      currentInstance = prodInstance;
      Log.d(TAG,"Server change - Prod");
    }else{
      currentInstance = betaInsatnce;
      Log.d(TAG,"Server change - Beta");
    }
    CodenvyClient.serverName = serverName;
  }
  public static void apiInit(){
    Log.d(TAG,"Inside API Init" + currentInstance.getCurrentURL());
    currentInstance.apiInit();
  }
  public static void postBuildLogin() {
    Log.d(TAG,"Inside post login Init" + currentInstance.getCurrentURL());    
    	currentInstance.postLogin(AppData.getLoginData(),new LoginResponseHandler());    
  }

  public static void postSettingsLogin() {
    Log.d(TAG,"Inside post login Init" + currentInstance.getCurrentURL());
    currentInstance.postLogin(AppData.getLoginData(), new LoginDialogResponseHandler());
  }
  
  public static void postRefreshLogin() {
    Log.d(TAG,"Inside post login Init" + currentInstance.getCurrentURL());
    currentInstance.postLogin(AppData.getLoginData(), new LoginRefreshHandler(SetupActivity.workspaceProgressDialog));
  }
  
  public static void buildProj() {
    Log.d(TAG,"Inside build proj Init" + currentInstance.getCurrentURL());
    if(serverName == Servers.PRODUCTION){
    	currentInstance.buildProj(AppData.getWorkspaceId(),AppData.getProject(),new BuildResponseHandler());
    }else{
    	currentInstance.buildProj(AppData.getWorkspaceId(),AppData.getProject(),AppData.getCommand(),new BuildResponseHandler());	
    }    
  }
  public static void buildStatus() {
    Log.d(TAG,"Inside build status Init" + currentInstance.getCurrentURL());
    currentInstance.buildStatus(AppData.getWorkspaceId(),AppData.getBuildTaskId(),new StatusResponseHandler());
  }
  public static void getAPK() {
    Log.d(TAG,"Inside get apk Init" + currentInstance.getCurrentURL());
    currentInstance.getAPK(AppData.getMachineId(), AppData.getApkUrl(), new ApkDownloadHandler());
  }
  public static void getWorkspaceDetails(){
    Log.d(TAG,"Inside workspace details Init" + currentInstance.getCurrentURL());
    currentInstance.getWorkspaceDetails(new WorkspaceResponseHandler(SetupActivity.workspaceProgressDialog));
  }
  public static void getProjectDetails(String wid){
    Log.d(TAG,"Inside project etails Init" + currentInstance.getCurrentURL());
    currentInstance.getProjectDetails(wid,new ProjectResponseHandler(wid));
  }
  
  public static boolean isInitialised(){
    return currentInstance.isInitialised();    
  }
  
  public static void setWorkspaceList(Map < String, CodenvyResponse > workspaceList){
    currentInstance.setWorkspaceList(workspaceList);
  }
  
  public static void getCommandDetails(String wid){
    currentInstance.getCommandDetails(wid);
  }
  public static Servers getCurrentServer(){
    return serverName;
  }
  
  public static void installAPK(){
    HomePageActivity.installAPK(AppData.getApkPath());
  }
  public static void updateCookie(List<String> cookies) {
	  currentInstance.updateCookie( cookies);
  }
}
