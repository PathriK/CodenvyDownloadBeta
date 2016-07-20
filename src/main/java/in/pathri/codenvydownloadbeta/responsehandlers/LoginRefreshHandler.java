package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.List;

import in.pathri.codenvydownloadbeta.CustomProgressDialog;
import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoginRefreshHandler extends ApiResponseHandler<CodenvyResponse> {
	private static final String className = LoginRefreshHandler.class.getSimpleName();
	  
    public LoginRefreshHandler(CustomProgressDialog workspaceProgressDialog){
        super(workspaceProgressDialog,CodenvyResponse.class);
    }
    
    @Override
    public void nextStep(CodenvyResponse codenvyResponses) {
        CodenvyClient.getWorkspaceDetails();
    }
    
    @Override
    void updateStatusText(String statusText) {
//         HomePageActivity.updateLoginStatusText(statusText);
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
    	HomePageActivity.updateTriggerStatusText(className + "::" + "nextStep" + "::" + "ResponseBody" + "::" + "Application Error!!");
    }
    
    @Override
    void nextStep(List<CodenvyResponse> codenvyResponses) {
    	HomePageActivity.updateTriggerStatusText(className + "::" + "nextStep" + "::" + "CodenvyResponse List" + "::" + "Application Error!!");
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