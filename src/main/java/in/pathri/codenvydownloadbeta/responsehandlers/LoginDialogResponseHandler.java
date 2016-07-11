package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.List;

import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import in.pathri.codenvydownloadbeta.preferancehandlers.LoginPreference;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoginDialogResponseHandler extends ApiResponseHandler<CodenvyResponse> {
	private static final String className = LoginDialogResponseHandler.class.getSimpleName();
    public LoginDialogResponseHandler(){
        super(LoginPreference.loginSpinner,CodenvyResponse.class);
    }
    
    @Override
    public void nextStep(CodenvyResponse codenvyResponses) {
        LoginPreference.updateLoginStatus("NextStep");
        LoginPreference.acceptLogin();
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
        LoginPreference.updateLoginStatus(statusText);
    }
    
    @Override
    void nextStep(ResponseBody arg0) {
        LoginPreference.updateLoginStatus("Application Error!!");
    }
    
    @Override
    void nextStep(List<CodenvyResponse> codenvyResponses) {
        LoginPreference.updateLoginStatus("Application Error!!");
    }

	@Override
	void handleCookie(Response<CodenvyResponse> response) {
		// TODO Auto-generated method stub
		
	}
}