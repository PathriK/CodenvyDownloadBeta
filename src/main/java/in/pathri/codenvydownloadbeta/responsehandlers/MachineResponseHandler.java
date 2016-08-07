package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.List;

import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.Client.CodenvyBetaClientAdapter;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class MachineResponseHandler<Z extends CodenvyResponse> extends ApiResponseHandler<CodenvyResponse> {
	private static final String className = MachineResponseHandler.class.getSimpleName();
  	CodenvyBetaClientAdapter clientImpl;
    public MachineResponseHandler( CodenvyBetaClientAdapter clientImpl){
      super(HomePageActivity.buildSpinner,CodenvyResponse.class);
      this.clientImpl = clientImpl;
    }
    @Override
    public void nextStep(CodenvyResponse codenvyResponse) {
        final CodenvyResponse currentResponse = codenvyResponse;
      String machineToken = currentResponse.getMachineToken();
      String projectURL = currentResponse.getProjectURL();
      AppData.setMachineToken(machineToken); 
      AppData.setProjectURL(projectURL);
      clientImpl.getArtifactId();      
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
    void updateStatusText(String statusText) {
        HomePageActivity.updateBuildStatusText(statusText);
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