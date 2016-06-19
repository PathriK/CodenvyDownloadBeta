package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;

import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.pojo.ResourceLinks;

public class WorkspaceStatusHandler extends ApiResponseHandler<CodenvyResponse> {
    String wid;
  	CodenvyBetaClientAdapter clientImpl;
    public WorkspaceStatusHandler(String wid, CodenvyBetaClientAdapter clientImpl){
      super(HomePageActivity.buildSpinner,CodenvyResponse.class);
      this.wid = wid;
      this.clientImpl = clientImpl;
    }
    @Override
    void nextStep(CodenvyResponse codenvyResponse) {
        final CodenvyResponse currentResponse = codenvyResponse;
      final String respStatus = currentResponse.getStatus();
        if ("STOPPED".equals(respStatus)) {
          clientImpl.startWorkspace(wid);
          
            this.updateStatusText(respStatus);
        } else if ("FAILED".equals(respStatus)) {
        this.updateStatusText(respStatus);
        } else if ("SUCCESSFUL".equals(respStatus)) {
        this.updateStatusText(respStatus);
        List < ResourceLinks > linksList = currentResponse.getLinks();
        Iterator < ResourceLinks > iterator = linksList.iterator();
        while (iterator.hasNext()) {
            ResourceLinks resourceLink = iterator.next();
            if ("download result".equals(resourceLink.rel)) {
                String downloadURL = resourceLink.href;
                CodenvyClient.getAPK();
                break;
            }
        }
        } else {
        	this.updateStatusText("Build Status Unknown" + respStatus);
   	 }
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