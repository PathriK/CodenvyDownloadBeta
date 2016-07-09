package in.pathri.codenvydownloadbeta.Client;

import java.util.List;
import java.util.Map;

import in.pathri.codenvydownloadbeta.pojo.CommandDetails;
import in.pathri.codenvydownloadbeta.pojo.LoginData;
import retrofit2.Callback;

public interface CodenvyClientInterface<T,U> {  
  void apiInit();
  public void postLogin(LoginData loginData, Callback < U > loginResponseHandler) ;
  public void buildProj(String workspaceId, String project, CommandDetails command, Callback < T > voidResponseHandler) ;
  public void buildProj(String workspaceId, String project, Callback < U > buildResponseHandler) ;
  public void buildStatus(String workspaceId, String buildId, Callback < U > statusResponseHandler) ;
  public void getAPK(String machineId, String apkPath, Callback < T > apkDownloadHandler) ;
  public void getWorkspaceDetails(Callback < List< U > > workspaceResponseHandler);
  public void getProjectDetails(String wid,Callback < List < U > > projectResponseHandler);
  public String getCurrentURL();
  public boolean isInitialised();
  public void setWorkspaceList(Map <String, U > workspaceList);
  public void getCommandDetails(String wid);
}
