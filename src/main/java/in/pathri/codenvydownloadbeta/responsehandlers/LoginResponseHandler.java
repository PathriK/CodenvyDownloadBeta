package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.List;

import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginResponseHandler extends ApiResponseHandler<CodenvyResponse> {
	private static final String className = LoginResponseHandler.class.getSimpleName();
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
    	HomePageActivity.updateTriggerStatusText(className + "::" + "nextStep" + "::" + "ResponseBody" + "::" + "Application Error!!");
    }
    
    @Override
    void nextStep(List<CodenvyResponse> codenvyResponses) {
    	HomePageActivity.updateTriggerStatusText(className + "::" + "nextStep" + "::" + "CodenvyResponse List" + "::" + "Application Error!!");
    }

	@Override
	void handleCookie(Response<CodenvyResponse> response) {
		List<String> cookies = response.headers().values("Set-Cookie");
		CodenvyClient.updateCookie(cookies);		
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub
		
	}
}