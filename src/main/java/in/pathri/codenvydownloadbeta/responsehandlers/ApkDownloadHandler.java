package in.pathri.codenvydownloadbeta.responsehandlers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import android.content.Context;
import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import okhttp3.ResponseBody;

public class ApkDownloadHandler extends ApiResponseHandler<ResponseBody>{
        
    public ApkDownloadHandler() {
        super(HomePageActivity.downloadSpinner,ResponseBody.class);
    }
    
    @Override
    void nextStep(ResponseBody rawResponse) {
        String fileName = AppData.getApkFileName();
        try {
            FileOutputStream fileOutputStream =
            HomePageActivity.context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
            IOUtils.write(rawResponse.bytes(), fileOutputStream);
            this.updateStatusText("Downloaded, Installing..");
            String path = HomePageActivity.context.getFilesDir().getAbsolutePath() + "/" + fileName;
            this.updateStatusText(path);
          	AppData.setApkPath(path);
            CodenvyClient.installAPK();
        } catch (IOException e) {
            this.updateStatusText("Error while writing file! " + e.toString());
        }
    }
    
    @Override
    public void nextStep(CodenvyResponse codenvyResponses) {
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
        HomePageActivity.updateDownloadStatusText(statusText);
    }
}