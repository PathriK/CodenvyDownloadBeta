package in.pathri.codenvydownloadbeta.Client;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.Map;

import in.pathri.codenvydownloadbeta.pojo.CodenvyResponseProd;
import in.pathri.codenvydownloadbeta.pojo.CommandDetails;
import in.pathri.codenvydownloadbeta.pojo.LoginData;
import in.pathri.codenvydownloadbeta.preferancehandlers.SetupActivity;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class CodenvyProdClient implements CodenvyClientInterface<ResponseBody,CodenvyResponseProd> {
	private static final String className = CodenvyProdClient.class.getSimpleName();
    public final String BASE_URL = "https://codenvy.com/api/";
    private Retrofit retrofit;
    private CodenvyApiService apiService;
    private boolean initialised = false;  
  private Map <String, CodenvyResponseProd > workspaceList;
    
    private interface CodenvyApiService {
        //Login
        @POST("auth/login")
        Call < CodenvyResponseProd > postWithJson(
        @Body LoginData loginData
        );
        
        //Build
        @POST("builder/{wid}/build")
        Call < CodenvyResponseProd > buildProj(@Path("wid") String workspaceId, @Query("project") String project);
        
        //Build Status
        @GET("builder/{wid}/status/{id}")
        Call < CodenvyResponseProd > buildStatus(@Path("wid") String workspaceId, @Path("id") String buildId);
        
        //Download APK
        @GET
        Call < ResponseBody > getAPK(@Url String apkUrl);
        
        //Get Workspace Details
        @GET("workspace/all")
        Call <List<CodenvyResponseProd>> getWorkspaceDetails();
        
        //Get Project Details
        @GET("project/{wid}")
        Call <List<CodenvyResponseProd>> getProjectDetails(@Path("wid") String workspaceId);
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
    
    public void postLogin(LoginData loginData, Callback < CodenvyResponseProd > loginResponseHandler) {
        // Prepare the HTTP request
        Call < CodenvyResponseProd > loginCall = apiService.postWithJson(loginData);
        
        // Asynchronously execute HTTP request
        loginCall.enqueue(loginResponseHandler);
    }
    
    public void buildProj(String workspaceId, String project, CommandDetails command, Callback < ResponseBody > buildResponseHandler) {
		System.out.println("Prod BuildProj::Not Supported");
    }
  
  public void buildProj(String workspaceId, String project,
          Callback<CodenvyResponseProd> buildResponseHandler) {
        // Prepare the HTTP request
        Call < CodenvyResponseProd > buildCall = apiService.buildProj(workspaceId, project);
        
        // Asynchronously execute HTTP request
        buildCall.enqueue(buildResponseHandler);

  }

    
    public void buildStatus(String workspaceId, String buildId, Callback < CodenvyResponseProd > statusResponseHandler) {
        // Prepare the HTTP request
        Call < CodenvyResponseProd > statusCall = apiService.buildStatus(workspaceId, buildId);
        
        // Asynchronously execute HTTP request
        statusCall.enqueue(statusResponseHandler);
    }
    
    public void getAPK(String machineId, String apkUrl, Callback < ResponseBody > apkDownloadHandler) {
        // Prepare the HTTP request
        Call < ResponseBody > getApkCall = apiService.getAPK(apkUrl);
        
        // Asynchronously execute HTTP request
        getApkCall.enqueue(apkDownloadHandler);
    }
    
    public void getWorkspaceDetails(Callback < List<CodenvyResponseProd> > workspaceResponseHandler){
        Call <List<CodenvyResponseProd>> workspaceDetailsCall = apiService.getWorkspaceDetails();
        workspaceDetailsCall.enqueue(workspaceResponseHandler);
    }
    
    public void getProjectDetails(String wid,Callback < List<CodenvyResponseProd> > projectResponseHandler){
        Call <List<CodenvyResponseProd>> projectDetailsCall = apiService.getProjectDetails(wid);
        projectDetailsCall.enqueue(projectResponseHandler);
    }
    	public String getCurrentURL(){
     return BASE_URL;
   }
  public boolean isInitialised(){
    return initialised;
  }
  
   public void setWorkspaceList(Map <String, CodenvyResponseProd > workspaceList){
    this.workspaceList = workspaceList;
  }
  
  public void getCommandDetails(String wid){
    SetupActivity.addCommandMap(wid,new String[0]);
  }

@Override
public void updateCookie(List<String> cookies) {
	// TODO Auto-generated method stub
	
}

  }