package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;

import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.pojo.ResourceLinks;

public class VoidResponseHandler extends ApiResponseHandler<ResponseBody> {

    public VoidResponseHandler(){
        super(HomePageActivity.buildSpinner,ResponseBody.class);
    }
    @Override
    void nextStep(ResponseBody responseBody) {
       
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