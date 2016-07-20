package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.List;

import in.pathri.codenvydownloadbeta.CustomLogger;
import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.Client.CodenvyBetaClientAdapter;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.pojo.BuildResult;
import in.pathri.codenvydownloadbeta.pojo.BuildStatus;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class BuildOutputHandler<Z extends CodenvyResponse> extends ApiResponseHandler<CodenvyResponse>{
	private static final String className = BuildOutputHandler.class.getSimpleName();
	CodenvyBetaClientAdapter clientImpl;
    public BuildOutputHandler( CodenvyBetaClientAdapter clientImpl){      
        super(HomePageActivity.triggerSpinner,CodenvyResponse.class);
        this.clientImpl = clientImpl;
    }
    
    @Override
    public void nextStep(CodenvyResponse codenvyResponse) {
    	final CodenvyResponse currentResponse = codenvyResponse;
    	String outputMsg = currentResponse.getMessage();
    	CustomLogger.d(className, "nextStep", "outputMsg", outputMsg);
    	AppData.addBuildOutput(outputMsg);
    	checkMsgStatus(outputMsg);
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
    
    private void checkMsgStatus(String msg){
    	boolean buildSuccess = false;
    	boolean doCheck = false;
    	if(msg.contains("[ERROR]")){
    		buildSuccess = false;
    		doCheck = true;
    	}else if(msg.contains("[INFO] BUILD FAILURE")){
    		buildSuccess = false;
    		doCheck = true;    		
    	}else if(msg.contains("[INFO] BUILD SUCCESS")){
    		buildSuccess = true;
    		doCheck = true;    		
    	}
    	
    	if(doCheck){
	    	if(buildSuccess){
	    		AppData.setBuildResult(BuildResult.SUCCESS);
	    		clientImpl.checkCompletion();
	    	}else{
	    		AppData.setBuildResult(BuildResult.FAILED);
	    		clientImpl.checkCompletion();    		
	    	}
    	}
    }

	@Override
	void handleCookie(Response<CodenvyResponse> response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnect() {
		CustomLogger.i(className, "onConnect", "inside Function");
		clientImpl.isBuildOutputHandlerReady = true;
		clientImpl.triggerBuild();
	}
}
