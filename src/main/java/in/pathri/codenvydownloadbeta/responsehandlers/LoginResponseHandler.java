package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.List;

import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import okhttp3.ResponseBody;

public class LoginResponseHandler extends ApiResponseHandler<CodenvyResponse> {
    String wid,project;
    
    public LoginResponseHandler(){
        super(HomePageActivity.loginSpinner,CodenvyResponse.class);
    }
    
    @Override
    public void nextStep(CodenvyResponse codenvyResponses) {
        CodenvyClient.buildProj();
    }
    
    @Override
    void updateStatusText(String statusText) {
        HomePageActivity.updateLoginStatusText(statusText);
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
    void nextStep(ResponseBody arg0) {
        HomePageActivity.updateLoginStatusText("Application Error!!");
    }
    
    @Override
    void nextStep(List<CodenvyResponse> codenvyResponses) {
        HomePageActivity.updateTriggerStatusText("Application Error!!");
    }
}