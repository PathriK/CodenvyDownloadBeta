package in.pathri.codenvydownloadbeta.Client;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import in.pathri.codenvydownloadbeta.CustomLogger;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponseBeta;
import in.pathri.codenvydownloadbeta.pojo.CommandDetails;
import in.pathri.codenvydownloadbeta.pojo.LoginData;
import in.pathri.codenvydownloadbeta.pojo.ProjectDetails;
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

public class CodenvyBetaClient implements CodenvyClientInterface<ResponseBody, CodenvyResponseBeta> {

	private static final String className = CodenvyBetaClient.class.getSimpleName();

	public final String BASE_URL = "https://beta.codenvy.com/api/";
	private Retrofit retrofit;
	private CodenvyApiService apiService;
	private boolean initialised = false;
	private Map<String, CodenvyResponseBeta> workspaceDetailsMap;
	static String TAG = "CodenvyBetaClient";

	private interface CodenvyApiService {
		// Login
		@POST("auth/login")
		Call<CodenvyResponseBeta> postWithJson(@Body LoginData loginData);

		// Build
		@POST("machine/{machineId}/command")
		Call<ResponseBody> buildProj(@Path("machineId") String machineId, @Query("outputChannel") String channelGUID, @Body CommandDetails command);

		// //Build Status
		// @GET("builder/{wid}/status/{id}")
		// Call < CodenvyResponseBeta > buildStatus(@Path("wid") String
		// workspaceId, @Path("id") String buildId);

        //Download APK
        @GET
        Call < ResponseBody > getAPK(@Url String apkUrl);
        
		// Get Workspace Details
		@GET("workspace")
		Call<List<CodenvyResponseBeta>> getWorkspaceDetails();

//		// Get Project Details
//		@GET("project/{wid}")
//		Call<List<CodenvyResponseBeta>> getProjectDetails(@Path("wid") String workspaceId);

		// Get Worskspace Detail
		@GET("workspace/{wid}")
		Call<CodenvyResponseBeta> getWorkspaceDetail(@Path("wid") String workspaceId);

		// Start Workspace
		@POST("workspace/{wid}/runtime")
		Call<ResponseBody> startWorkspace(@Path("wid") String workspaceId);
	}

	public void apiInit() {
		try {
			CustomLogger.i(className, "apiInit", "Inside Method");
			CookieManager cookieManager = new CookieManager();
			cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
			OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(cookieManager))
					.build();

			retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
					.addConverterFactory(GsonConverterFactory.create()).build();

			// Service setup
			apiService = retrofit.create(CodenvyApiService.class);
			initialised = true;
		} catch (Exception e) {
			CustomLogger.e(className, "apiInit", "Api Init Error", e);
			e.printStackTrace();
		}
	}

	public void postLogin(LoginData loginData, Callback<CodenvyResponseBeta> loginResponseHandler) {
		
		CustomLogger.d(className, "postLogin", "LoginData", loginData.toString());		
		// Prepare the HTTP request
		Call<CodenvyResponseBeta> loginCall = apiService.postWithJson(loginData);

		// Asynchronously execute HTTP request
		loginCall.enqueue(loginResponseHandler);
	}

	public void buildProj(String workspaceId, String project, CommandDetails command,
			Callback<ResponseBody> voidResponseHandler) {
		CustomLogger.d(className, "buildProj", "machineId|project|command", workspaceId + "|" + project + "|" + command.toString());
		// Prepare the HTTP request
		Call<ResponseBody> buildCall = apiService.buildProj(workspaceId, project, command);

		// Asynchronously execute HTTP request
		buildCall.enqueue(voidResponseHandler);
	}

	public void buildStatus(String workspaceId, String buildId, Callback<CodenvyResponseBeta> statusResponseHandler) {
		CustomLogger.d(className, "buildStatus", "workspaceId|buildId", workspaceId + "|" + buildId);
		// // Prepare the HTTP request
		// Call < CodenvyResponseBeta > statusCall =
		// apiService.buildStatus(workspaceId, buildId);

		// // Asynchronously execute HTTP request
		// statusCall.enqueue(statusResponseHandler);
		System.out.println("NOT IMPLEMENTED");
	}

	public void getAPK(String machineId, String apkPath, Callback<ResponseBody> apkDownloadHandler) {
		CustomLogger.d(className, "getAPK", "machineId|apkPath", machineId + "|" + apkPath);
		
		String downloadURL = "machine/" + machineId + "/filepath/" + apkPath;		
		// Prepare the HTTP request
		Call<ResponseBody> getApkCall = apiService.getAPK(downloadURL);

		// Asynchronously execute HTTP request
		getApkCall.enqueue(apkDownloadHandler);
	}

	public void getWorkspaceDetails(Callback<List<CodenvyResponseBeta>> workspaceResponseHandler) {
		CustomLogger.i(className, "getWorkspaceDetails", "Into Function");
		Call<List<CodenvyResponseBeta>> workspaceDetailsCall = apiService.getWorkspaceDetails();
		workspaceDetailsCall.enqueue(workspaceResponseHandler);
	}

	public void getProjectDetails(String wid, Callback<List<CodenvyResponseBeta>> projectResponseHandler) {
		CustomLogger.d(className, "getProjectDetails", "wid", wid);
		// Call <List<CodenvyResponseBeta>> projectDetailsCall =
		// apiService.getProjectDetails(wid);
		// projectDetailsCall.enqueue(projectResponseHandler);
		// Iterator < CodenvyResponseBeta > iterator = workspaceList.iterator();
		List<String> names = new ArrayList<String>();
		CodenvyResponseBeta workspaceDetails = workspaceDetailsMap.get(wid);
		Iterator<ProjectDetails> iterator = workspaceDetails.getWorkspaceConfig().projects.iterator();
		while (iterator.hasNext()) {
			ProjectDetails projectDetails = iterator.next();
			String name = projectDetails.name;
			names.add(name);
		}
		final String[] namesArr = names.toArray(new String[names.size()]);
		SetupActivity.addProjectMap(wid, namesArr);
	}

	public void getCommandDetails(String wid) {
		CustomLogger.d(className, "getCommandDetails", "wid", wid);
		List<String> names = new ArrayList<String>();
		CodenvyResponseBeta workspaceDetails = workspaceDetailsMap.get(wid);
		Iterator<CommandDetails> iterator = workspaceDetails.getWorkspaceConfig().commands.iterator();
		while (iterator.hasNext()) {
			CommandDetails commandDetails = iterator.next();
			String name = commandDetails.name;
			names.add(name);
			AppData.addCommandMap(wid, name, commandDetails);
		}
		final String[] namesArr = names.toArray(new String[names.size()]);

		SetupActivity.addCommandMap(wid, namesArr);
	}

	public void getWorkspaceDetail(String wid, Callback<CodenvyResponseBeta> workspaceStatusHandler) {
		CustomLogger.d(className, "getWorkspaceDetail", "wid", wid);
		Call<CodenvyResponseBeta> workspaceDetailCall = apiService.getWorkspaceDetail(wid);
		workspaceDetailCall.enqueue(workspaceStatusHandler);
	}

	public void startWorkspace(String wid, Callback<ResponseBody> voidResponseHandler) {
		CustomLogger.d(className, "startWorkspace", "wid", wid);
		Call<ResponseBody> startWorkspaceCall = apiService.startWorkspace(wid);
		startWorkspaceCall.enqueue(voidResponseHandler);
	}

	public String getCurrentURL() {
		CustomLogger.d(className, "getCurrentURL", "BASE_URL", BASE_URL);
		return BASE_URL;
	}

	public boolean isInitialised() {
		CustomLogger.d(className, "isInitialised", "initialised", initialised?"true":"false");
		return initialised;
	}

	public void setWorkspaceList(Map<String, CodenvyResponseBeta> workspaceList) {
		CustomLogger.i(className, "setWorkspaceList", "setWorkspaceList called");
		this.workspaceDetailsMap = workspaceList;
	}

	@Override
	public void buildProj(String workspaceId, String project, Callback<CodenvyResponseBeta> buildResponseHandler) {
		CustomLogger.i(className, "buildProj", "Non Command Function::Not Supported");

	}

	@Override
	public void updateCookie(List<String> cookies) {
		CustomLogger.i(className, "updateCookie", "Update Cookie Called");
		CodenvyBetaWSClient.updateCookie(cookies);
	}
}
