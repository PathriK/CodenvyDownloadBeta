package in.pathri.codenvydownloadbeta;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.pojo.Servers;
import in.pathri.codenvydownloadbeta.preferancehandlers.SetupActivity;

public class HomePageActivity extends Activity {
	private static final String className = HomePageActivity.class.getSimpleName();
    public static final Integer SUCCESS_CODE = 200;
    static TextView loginStatus, buildStatus, statusMsg, downloadStatus, triggerStatus,currentProject;
    public static ProgressBar loginSpinner,buildSpinner,triggerSpinner,downloadSpinner;
    public static Handler statusHandler;
    public static Context context;
    
//     private static String username,password,wid,project,wname,command;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        loginStatus = (TextView) findViewById(R.id.login_status);
        buildStatus = (TextView) findViewById(R.id.build_status);
        triggerStatus = (TextView) findViewById(R.id.trigger_status);
        downloadStatus = (TextView) findViewById(R.id.download_status);
        statusMsg = (TextView) findViewById(R.id.status_msg);
        currentProject = (TextView) findViewById(R.id.current_project);
        
        loginSpinner = (ProgressBar) findViewById(R.id.login_progress);
        buildSpinner = (ProgressBar) findViewById(R.id.build_progress);
        triggerSpinner = (ProgressBar) findViewById(R.id.trigger_progress);
        downloadSpinner = (ProgressBar) findViewById(R.id.download_progress);
        
        statusHandler = new Handler();
        context = getApplicationContext();
        
        statusMsg.setMovementMethod(new ScrollingMovementMethod());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setup:
            Intent i = new Intent(this, SetupActivity.class);
            startActivity(i);
            return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public static void clearStatusTexts(){
        updateLoginStatusText("");
        updateTriggerStatusText("");
        updateBuildStatusText("");
        updateDownloadStatusText("");
    }
    
    public static void updateLoginStatusText(String text) {       
    	loginStatus.setText(text);
    }
    
    public static void updateTriggerStatusText(String text) {
    	CustomLogger.i(className, "updateTriggerStatusText", text);    	
        triggerStatus.setText(text);
    }
    
    public static void updateBuildStatusText(String text) {
//        buildStatus.setText(text);
    	CustomLogger.d(className, "updateBuildStatusText", "text", text);    	
    }
    
    public static void updateDownloadStatusText(String text) {
        downloadStatus.setText(text);
    }
    
    public static void updateStatusText(String text) {
        String temp = statusMsg.getText().toString();
        statusMsg.setText(temp + text);
    }
    
    
    public void onBuild(View view) {
        clearStatusTexts();        
        AppData.clearBuildData();
        loginStatus.setText("Logging In");
        doLogin();
    }
    
    private static void doLogin() {
//         LoginData loginData = new LoginData(username, password);
//         CodenvyClient.postLogin(loginData, wid, project,command);
        CodenvyClient.postBuildLogin();
    }
    
    public static void installAPK(String fileRelPath) {
        Intent promptInstall = new Intent(Intent.ACTION_VIEW);
        promptInstall.setDataAndType(Uri.fromFile(new File(fileRelPath)), "application/vnd.android.package-archive");
        promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(promptInstall);
    }
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    
    public static File getCodenvyDownloadDir() {
        // Get the directory for the app's private pictures directory.
        //     File file = new File(context.getExternalFilesDir(null), "CodenvyAPKs");
        File file = new File(context.getFilesDir(), "CodenvyAPKs");
        if (!file.mkdirs()) {
            HomePageActivity.updateStatusText("Unable to Create Directory");
        }
        return file;
    }
    
    public static String getStackTraceString(Throwable t){
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        t.printStackTrace(printWriter);
        String s = writer.toString();
        return s;
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	CustomLogger.i(className, "onPause", "into onPause");
    	AppData.onPause(context);    	
    }
    
    @Override
    protected void onResume(){
        super.onResume();
        CustomLogger.i(className, "onResume", "into onResume");
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        String username = myPrefs.getString(SetupActivity.USER_NAME, "");
        String password = myPrefs.getString(SetupActivity.PASSWORD, "");
        String wid = myPrefs.getString(SetupActivity.WORKSPACE, "");
        String wname = myPrefs.getString(SetupActivity.WORKSPACE_NAME, "");
        String project = myPrefs.getString(SetupActivity.PROJECT, "");
      String command = myPrefs.getString(SetupActivity.COMMAND, ""); 
      String server = myPrefs.getString(SetupActivity.SERVER_DOMAIN, ""); 

//      AppData.clearAll();
//      AppData.setLoginData(new LoginData(username,password));
//      AppData.setWorkspaceName(wname);
//      AppData.setWorkspaceId(wid);
//      AppData.setProject(project);
//      AppData.setCommand(command);
      
      AppData.onResume(context);
         
      if(server != "" && Servers.valueOf(server) == Servers.BETA){
    	  CodenvyClient.switchServer(Servers.valueOf(server));
        server = "Beta";
      }else{
        server = "";
      }
      if(command != ""){
        command = " ; Command: " + command;
      }
      
      CodenvyClient.apiInit();
      
        
        
        String statusMsg = "Error!";
        if(username == ""){
            statusMsg = "Please Login and Select a workspace,project in settings page";
            }else if(wid == "" || project == ""){
            statusMsg = "Please Select a workspace,project in settings page";
            }else if(server == "Beta" && command == ""){
          statusMsg = "Please Select a command in settings page";
        }else{
            statusMsg = "Current " + server + " Project: " + wname + "/" + project + command;
        }
        currentProject.setText(statusMsg);
    }
}