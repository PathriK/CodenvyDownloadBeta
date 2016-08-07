package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import in.pathri.codenvydownloadbeta.CustomProgressDialog;
import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.Client.CodenvyBetaClientAdapter;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import in.pathri.codenvydownloadbeta.pojo.WorkspaceDetails;
import in.pathri.codenvydownloadbeta.preferancehandlers.SetupActivity;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class ArtifactIdResponseHandler<Z extends List<CodenvyResponse>> extends ApiResponseHandler <List<CodenvyResponse>>{
	private static final String className = ArtifactIdResponseHandler.class.getSimpleName();
	CodenvyBetaClientAdapter clientImpl;
    public ArtifactIdResponseHandler(CodenvyBetaClientAdapter clientImpl){
        super(HomePageActivity.buildSpinner,CodenvyResponse.class);
        super.responseType = ResponseType.ARRAY;
        this.clientImpl = clientImpl;
    }
    
    @Override
    void nextStep(List<CodenvyResponse> codenvyResponse) {
    	final List<CodenvyResponse> currentResponse = codenvyResponse;
    	CodenvyResponse currentMachine = currentResponse.get(0);
    	AppData.setArtifactId(currentMachine.getAttribute("maven.artifactId"));
    	AppData.setArtifactExt(currentMachine.getAttribute("maven.packaging"));
    	clientImpl.checkCompletion();
    }
    
    @Override
    void nextStep(ResponseBody arg0) {
    	HomePageActivity.updateTriggerStatusText(className + "::" + "nextStep" + "::" + "ResponseBody" + "::" + "Application Error!!");
    }

    @Override
    void handleAuthIssue(ResponseBody responseBody) {
      try{
        String responseString = responseBody.string();
        if(responseString.contains("User not authorized to call this method")){
          CodenvyClient.postRefreshLogin();
        }
              }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nextStep(CodenvyResponse arg0) {
    	HomePageActivity.updateTriggerStatusText(className + "::" + "nextStep" + "::" + "CodenvyResponse" + "::" + "Application Error!!");
    }
    
    @Override
    void updateStatusText(String statusText) {
    	HomePageActivity.updateBuildStatusText(statusText);
    }

	@Override
	void handleCookie(Response<List<CodenvyResponse>> response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub
		
	}
}