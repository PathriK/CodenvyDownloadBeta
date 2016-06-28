package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.Iterator;
import java.util.List;

import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.Client.CodenvyBetaClientAdapter;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import in.pathri.codenvydownloadbeta.pojo.ResourceLinks;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public class WorkspaceStatusHandler<Z extends CodenvyResponse> extends ApiResponseHandler<CodenvyResponse> {
    String wid;
  	CodenvyBetaClientAdapter clientImpl;
    public WorkspaceStatusHandler(String wid, CodenvyBetaClientAdapter clientImpl){
      super(HomePageActivity.buildSpinner,CodenvyResponse.class);
      this.wid = wid;
      this.clientImpl = clientImpl;
    }
    @Override
    public void nextStep(CodenvyResponse codenvyResponse) {
        final CodenvyResponse currentResponse = codenvyResponse;
      final String respStatus = currentResponse.getStatus();
        if ("STOPPED".equals(respStatus)) {
          clientImpl.startWorkspace(wid);          
          this.updateStatusText(respStatus);          
        } else if ("CREATING".equals(respStatus)) {        	
        this.updateStatusText(respStatus);
        } else if ("RUNNING".equals(respStatus)) {
       //
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
}