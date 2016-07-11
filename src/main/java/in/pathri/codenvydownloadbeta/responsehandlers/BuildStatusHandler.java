package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.Iterator;
import java.util.List;

import in.pathri.codenvydownloadbeta.CustomLogger;
import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.Client.CodenvyBetaClientAdapter;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.pojo.BuildStatus;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import in.pathri.codenvydownloadbeta.pojo.ResourceLinks;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class BuildStatusHandler<Z extends CodenvyResponse> extends ApiResponseHandler<CodenvyResponse> {
	private static final String className = BuildStatusHandler.class.getSimpleName();
  	CodenvyBetaClientAdapter clientImpl;
    public BuildStatusHandler(CodenvyBetaClientAdapter clientImpl){
      super(HomePageActivity.buildSpinner,CodenvyResponse.class);
      this.clientImpl = clientImpl;
    }
    @Override
    public void nextStep(CodenvyResponse codenvyResponse) {
        final CodenvyResponse currentResponse = codenvyResponse;
      final String respStatus = currentResponse.getStatus();
        if ("STOPPED".equals(respStatus)) {
        	CustomLogger.d(className, "nextStep", "respStatus", respStatus);
        	AppData.setBuildStatus(BuildStatus.COMPLETED);
          clientImpl.checkCompletion();
          this.updateStatusText(respStatus);          
        } else if ("STARTED".equals(respStatus)) {
        	CustomLogger.d(className, "nextStep", "respStatus", respStatus);
        	AppData.setBuildStatus(BuildStatus.STARTED);
        	this.updateStatusText(respStatus);
        } else {
        	CustomLogger.d(className, "nextStep", "respStatus::Unknown", respStatus);
        	this.updateStatusText("Build Status Unknown" + respStatus);
   	 }
    }
    
    @Override
    void nextStep(ResponseBody arg0) {
        HomePageActivity.updateStatusText("Application Error!!");
    }
  
    @Override
    void handleAuthIssue(ResponseBody responseBody) {
        try{
           this.updateStatusText("Error: " + responseBody.string());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }      
    
    @Override
    void updateStatusText(String statusText) {
        HomePageActivity.updateBuildStatusText(statusText);
    }
    
    @Override
    void nextStep(List<CodenvyResponse> codenvyResponses) {
        HomePageActivity.updateTriggerStatusText("Application Error!!");
    }
	@Override
	void handleCookie(Response<CodenvyResponse> response) {
		// TODO Auto-generated method stub
		
	}
}