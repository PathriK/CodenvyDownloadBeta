package in.pathri.codenvydownloadbeta.Client;

import okhttp3.OkHttpClient;
import okhttp3.JavaNetCookieJar;
import okhttp3.ResponseBody;

import in.pathri.codenvydownloadbeta.pojo.CodenvyResponseBeta;
import in.pathri.codenvydownloadbeta.pojo.LoginData;
import in.pathri.codenvydownloadbeta.pojo.ProjectDetails;
import in.pathri.codenvydownloadbeta.pojo.CommandDetails;
import in.pathri.codenvydownloadbeta.preferancehandlers.SetupActivity;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class CodenvyBetaClient implements CodenvyClientInterface<ResponseBody,CodenvyResponseBeta> {
    public final String BASE_URL = "http://beta.codenvy.com/api/";
    private Retrofit retrofit;
    private CodenvyApiService apiService;
  	 private boolean initialised = false;  
    private Map<String,CodenvyResponseBeta> workspaceDetailsMap;
  
    private interface CodenvyApiService {
        //Login
        @POST("auth/login")
        Call < CodenvyResponseBeta > postWithJson(@Body LoginData loginData);
        
        //Build
        @POST("machine/{machineId}/command")
        Call < CodenvyResponseBeta > buildProj(@Path("machineId") String machineId, @Body CommandData command);
        
//         //Build Status
//         @GET("builder/{wid}/status/{id}")
//         Call < CodenvyResponseBeta > buildStatus(@Path("wid") String workspaceId, @Path("id") String buildId);
        
        //Download APK
        @GET("machine/{machineId}/filepath/{path}")
        Call < ResponseBody > getAPK(@Path("machineId") String machineId, @Path("path") String filePath);
        
        //Get Workspace Details
        @GET("workspace")
        Call <List<CodenvyResponseBeta>> getWorkspaceDetails();
        
        //Get Project Details
        @GET("project/{wid}")
        Call <List<CodenvyResponseBeta>> getProjectDetails(@Path("wid") String workspaceId);
      
        //Get Worskspace Detail
        @GET("workspace/{wid}")
        Call <CodenvyResponseBeta> getWorkspaceDetail(@Path("wid") String workspaceId);
      
      	//Start Workspace
      	@POST("workspace/{wid}/runtime")
      	Call <CodenvyResponseBeta> startWorkspace(@Path("wid") String workspaceId);
    }
    
    public void apiInit() {
        try {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new JavaNetCookieJar(cookieManager))
            .build();
            
            retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
            
            // Service setup
            apiService = retrofit.create(CodenvyApiService.class);
          initialised = true;
            } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void postLogin(LoginData loginData, Callback < CodenvyResponseBeta > loginResponseHandler) {
        // Prepare the HTTP request
        Call < CodenvyResponseBeta > loginCall = apiService.postWithJson(loginData);
        
        // Asynchronously execute HTTP request
        loginCall.enqueue(loginResponseHandler);
    }
    
    public void buildProj(String workspaceId, String project, String command, Callback < CodenvyResponseBeta > buildResponseHandler) {
//         Prepare the HTTP request
        Call < CodenvyResponseBeta > buildCall = apiService.buildProj(workspaceId, project);
        
//         Asynchronously execute HTTP request
        buildCall.enqueue(buildResponseHandler);
    }
    
    public void buildStatus(String workspaceId, String buildId, Callback < CodenvyResponseBeta > statusResponseHandler) {
        // Prepare the HTTP request
        Call < CodenvyResponseBeta > statusCall = apiService.buildStatus(workspaceId, buildId);
        
        // Asynchronously execute HTTP request
        statusCall.enqueue(statusResponseHandler);
    }
    
    public void getAPK(String apkUrl, Callback < ResponseBody > apkDownloadHandler) {
        // Prepare the HTTP request
        Call < ResponseBody > getApkCall = apiService.getAPK(apkUrl);
        
        // Asynchronously execute HTTP request
        getApkCall.enqueue(apkDownloadHandler);
    }
    
    public void getWorkspaceDetails(Callback < List<CodenvyResponseBeta> > workspaceResponseHandler){
        Call <List<CodenvyResponseBeta>> workspaceDetailsCall = apiService.getWorkspaceDetails();
        workspaceDetailsCall.enqueue(workspaceResponseHandler);      
    }
    
    public void getProjectDetails(String wid,Callback < List<CodenvyResponseBeta> > projectResponseHandler){
//         Call <List<CodenvyResponseBeta>> projectDetailsCall = apiService.getProjectDetails(wid);
//         projectDetailsCall.enqueue(projectResponseHandler);
//         Iterator < CodenvyResponseBeta > iterator = workspaceList.iterator();
        List<String> names = new ArrayList<String>();
        CodenvyResponseBeta workspaceDetails = workspaceDetailsMap.get(wid);
      Iterator < ProjectDetails > iterator = workspaceDetails.getWorkspaceConfig().projects.iterator();
        while (iterator.hasNext()) {          	
            ProjectDetails projectDetails = iterator.next();
            String name = projectDetails.name;
            names.add(name);
        }
        final String[] namesArr = names.toArray(new String[names.size()]);
        SetupActivity.addProjectMap(wid,namesArr);
    }
  
  public void getCommandDetails(String wid){
    List<String> names = new ArrayList<String>();
        CodenvyResponseBeta workspaceDetails = workspaceDetailsMap.get(wid);
                Iterator < CommandDetails > iterator = workspaceDetails.getWorkspaceConfig().commands.iterator();
        while (iterator.hasNext()) {          	
            CommandDetails commandDetails = iterator.next();
            String name = commandDetails.name;
            names.add(name);
        }
        final String[] namesArr = names.toArray(new String[names.size()]);

      SetupActivity.addCommandMap(wid,namesArr);
  }
  
  public void getWorkspaceDetail(String wid, Callback <CodenvyResponseBeta> workspaceStatusHandler){
        Call <CodenvyResponseBeta> workspaceDetailCall = apiService.getWorkspaceDetail();
        workspaceDetailCall.enqueue(workspaceStatusHandler);          
  }
  
  public void startWorkspace(String wid, Callback <ResponseBody> voidResponseHandler){
    Call <ResponseBody> startWorkspaceCall = apiService.startWorkspace();
    startWorkspaceCall.enqueue(voidResponseHandler);
  }
  
  	public String getCurrentURL(){
     return BASE_URL;
   }
  
  public boolean isInitialised(){
    return initialised;
  }
  
  public void setWorkspaceList(Map < String, CodenvyResponseBeta > workspaceList){
    this.workspaceDetailsMap = workspaceList;
  }
}
