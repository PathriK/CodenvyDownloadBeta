package in.pathri.codenvydownloadbeta.responsehandlers;

import okhttp3.ResponseBody;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.HomePageActivity;
import java.util.List;

public class BuildResponseHandler extends ApiResponseHandler<CodenvyResponse>{
    public BuildResponseHandler(){      
        super(HomePageActivity.triggerSpinner,CodenvyResponse.class);
    }
    
    @Override
    void nextStep(CodenvyResponse codenvyResponse) {
      	String taskId = codenvyResponse.getTaskId();
        this.updateStatusText("TaskID:" + taskId);
        AppData.setBuildTaskId(taskId);
        CodenvyClient.buildStatus();
    }
    
    @Override
    void nextStep(ResponseBody arg0) {
        HomePageActivity.updateTriggerStatusText("Application Error!!");
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
    void nextStep(List<CodenvyResponse> codenvyResponses) {
        HomePageActivity.updateTriggerStatusText("Application Error!!");
    }
    
    
    @Override
    void updateStatusText(String statusText) {
        HomePageActivity.updateTriggerStatusText(statusText);
    }
}
