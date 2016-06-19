package in.pathri.codenvydownloadbeta.responsehandlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import okhttp3.ResponseBody;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import in.pathri.codenvydownloadbeta.CustomProgressDialog;
import in.pathri.codenvydownloadbeta.pojo.WorkspaceDetails;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.preferancehandlers.SetupActivity;


public class WorkspaceResponseHandler extends ApiResponseHandler <List<CodenvyResponse>>{
  	 
    public WorkspaceResponseHandler(CustomProgressDialog workspaceProgressDialog){
        super(workspaceProgressDialog);
    }
    
    @Override
    void nextStep(List<CodenvyResponse> apiResponse) {
        List<String> names = new ArrayList<String>();
        List<String> ids = new ArrayList<String>();
        List < CodenvyResponse > workspaceList = apiResponse;
// 			CodenvyClient.setWorkspaceList(workspaceList);
        Iterator < CodenvyResponse > iterator = workspaceList.iterator();
      Map<String,CodenvyResponse> workspaceDetailsMap = new HashMap<String,CodenvyResponse>();
        while (iterator.hasNext()) {
            CodenvyResponse workspaceDetails = iterator.next();
          
          WorkspaceDetails workspaceRef = workspaceDetails.getWorkspaceReference();
            String name = workspaceRef.name;
            String id = workspaceRef.id;
          workspaceDetailsMap.put(id,workspaceDetails);
            names.add(name);
            ids.add(id);
            SetupActivity.addWorspaceMap(id,name);
        }
      CodenvyClient.setWorkspaceList(workspaceDetailsMap);
        String[] namesArr = names.toArray(new String[names.size()]);
        String[] idsArr= ids.toArray(new String[ids.size()]);
        SetupActivity.updateWorspacePreference(namesArr,idsArr);
    }
    
    @Override
    void nextStep(ResponseBody arg0) {
        updateStatusText("Application Error!!");
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
    void nextStep(CodenvyResponse arg0) {
        updateStatusText("Application Error!!");
    }
    
    @Override
    void updateStatusText(String statusText) {
        SetupActivity.updateWorkspaceSummary(statusText);
    }
}