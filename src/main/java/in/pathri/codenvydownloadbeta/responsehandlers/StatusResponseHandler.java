package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.Iterator;
import java.util.List;

import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import in.pathri.codenvydownloadbeta.pojo.ResourceLinks;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class StatusResponseHandler extends ApiResponseHandler<CodenvyResponse> {
	private static final String className = StatusResponseHandler.class.getSimpleName();
    String wid;
    public StatusResponseHandler(){
        super(HomePageActivity.buildSpinner,CodenvyResponse.class);
    }
    @Override
    public void nextStep(CodenvyResponse codenvyResponse) {
        final CodenvyResponse currentResponse = codenvyResponse;
      final String respStatus = currentResponse.getStatus();
        if ("IN_QUEUE".equals(respStatus) || "IN_PROGRESS".equals(respStatus)) {
            this.updateStatusText(respStatus);
            HomePageActivity.statusHandler.postDelayed(new Runnable() {
                @Override
                public void run() {                  	
                    CodenvyClient.buildStatus();
                }
            }, 5000);
            } else if ("FAILED".equals(respStatus)) {
            this.updateStatusText(respStatus);
            } else if ("SUCCESSFUL".equals(respStatus)) {
            this.updateStatusText(respStatus);
            List < ResourceLinks > linksList = currentResponse.getLinks();
            Iterator < ResourceLinks > iterator = linksList.iterator();
            while (iterator.hasNext()) {
                ResourceLinks resourceLink = iterator.next();
                if ("download result".equals(resourceLink.rel)) {
                    String downloadURL = resourceLink.href;
                    AppData.setApkUrl(downloadURL);
                    CodenvyClient.getAPK();
                    break;
                }
            }
            } else {
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
