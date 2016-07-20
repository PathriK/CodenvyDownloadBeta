package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.List;

import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class BuildResponseHandler extends ApiResponseHandler<CodenvyResponse>{
	private static final String className = BuildResponseHandler.class.getSimpleName();
    public BuildResponseHandler(){      
        super(HomePageActivity.triggerSpinner,CodenvyResponse.class);
    }
    
    @Override
    public void nextStep(CodenvyResponse codenvyResponse) {
      	String taskId = codenvyResponse.getTaskId();
        this.updateStatusText("TaskID:" + taskId);
        AppData.setBuildTaskId(taskId);
        CodenvyClient.buildStatus();
    }
    
    @Override
    void nextStep(ResponseBody arg0) {
    	HomePageActivity.updateTriggerStatusText(className + "::" + "nextStep" + "::" + "ResponseBody" + "::" + "Application Error!!");
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
    	HomePageActivity.updateTriggerStatusText(className + "::" + "nextStep" + "::" + "CodenvyResponse List" + "::" + "Application Error!!");
    }
    
    
    @Override
    void updateStatusText(String statusText) {
        HomePageActivity.updateTriggerStatusText(statusText);
    }

	@Override
	void handleCookie(Response<CodenvyResponse> response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub
		
	}
}
