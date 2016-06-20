package in.pathri.codenvydownloadbeta.preferancehandlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import in.pathri.codenvydownloadbeta.CustomProgressDialog;
import in.pathri.codenvydownloadbeta.HomePageActivity;
import in.pathri.codenvydownloadbeta.R;
import in.pathri.codenvydownloadbeta.Client.CodenvyClient;
import in.pathri.codenvydownloadbeta.pojo.AppData;
import in.pathri.codenvydownloadbeta.pojo.Servers;

public class SetupActivity extends PreferenceActivity  {
    public static final String SERVER_DOMAIN = "serverName";  
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String IS_LOGGED_IN = "is_logged_in";
    public static final String WORKSPACE = "workspace";
    public static final String PROJECT = "project";
    public static final String WORKSPACE_NAME = "workspace_name";
  public static final String COMMAND = "command";
    private static Context mContext = HomePageActivity.context;
    private static ListPreference serverPrefs,worspacePrefs,projectPrefs,commandPrefs;
    private static Preference userCred;
    private static Map<String,String> workspaceDetails = new HashMap<String,String>();  
    private static String[] workspaceEntries;
    private static String[] workspaceValues;
    private static Map<String,String[]> projectDetails= new HashMap<String,String[]>();
  private static Map<String,String[]> commandDetails= new HashMap<String,String[]>();  
    private static List<String> wids,commadWids,projectWids;
    private static SharedPreferences sharedPreferences;
    public static CustomProgressDialog workspaceProgressDialog;
    public static CustomProgressDialog projectProgressDialog;
    private static SetupFragment setupFragment;
    private static Gson gson;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFragment = new SetupFragment();
        gson = new Gson();
        getFragmentManager().beginTransaction().replace(android.R.id.content, setupFragment).commit();
    }
    
    public static class SetupFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);            
            addPreferencesFromResource(R.xml.preferences);
          serverPrefs = (ListPreference) findPreference("serverName");
            worspacePrefs = (ListPreference) findPreference("workspace");
            projectPrefs = (ListPreference) findPreference("project");
          commandPrefs = (ListPreference) findPreference("command");
            userCred = findPreference(IS_LOGGED_IN);
          
          serverPrefs.setEntries(Servers.getServerNames());
          serverPrefs.setEntryValues(Servers.getServerValues());
//           String serverName = serverPrefs.getValue();
//           if(Servers.valueOf(serverName) == Servers.PRODUCTION){
//             commandPrefs.setEnabled(false);
//             commandPrefs.setSummary("Not supported in Production Server");
//           } else if(Servers.valueOf(serverName) == Servers.BETA){
//             commandPrefs.setEnabled(true);            
//           }
        }
        
      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          LinearLayout v = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);

          Button btn = new Button(getActivity().getApplicationContext());
          btn.setText("Refresh");

          v.addView(btn);
          btn.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
                if("-NONE-".equals(sharedPreferences.getString(USER_NAME, "-NONE-"))){
                	toastThis("Please Login before refresh");
                }else{
                	CodenvyClient.getWorkspaceDetails();  
                }
              }
          });

          return v;
      }
      
        @Override
        public void onAttach(Activity activity){
            workspaceProgressDialog = new CustomProgressDialog(getActivity(),"Workspace List Refresh","Please Wait...");
            projectProgressDialog = new CustomProgressDialog(getActivity(),"Project List Refresh","Please Wait...");            
            super.onAttach(activity);
        }
        
        @Override
        public void onResume() {
            super.onResume();
            sharedPreferences = getPreferenceScreen().getSharedPreferences();
            getMapPrefs(sharedPreferences);
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);            
            String temp = sharedPreferences.getString(USER_NAME, "");
            updateServerPreference();
            if(!temp.equals("")){
                userCred.setSummary("Logged In User: " + temp);
                if(workspaceDetails.isEmpty()){
                    CodenvyClient.getWorkspaceDetails();
                    }else{
                    updateWorspacePreference();
                    updateProjectPreference();
                  updateCommandPreference();
                }                
            }
        }
        
        @Override
        public void onPause() {
            super.onPause();
            // Set up a listener whenever a key changes
            sharedPreferences = getPreferenceScreen().getSharedPreferences();
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
            updateMapPrefs(sharedPreferences);
        }
        
        private void updateMapPrefs(SharedPreferences sharedPreferences){            
            SharedPreferences.Editor editor = sharedPreferences.edit();            
            String workspaceDetailsString = gson.toJson(workspaceDetails);
            editor.putString("workspace_details", workspaceDetailsString);            
            String workspaceEntriesString = gson.toJson(workspaceEntries);
            editor.putString("workspace_entries", workspaceEntriesString);            
            String workspaceValuesString = gson.toJson(workspaceValues);
            editor.putString("workspace_values", workspaceValuesString);            
            String projectDetailsString = gson.toJson(projectDetails);            
            editor.putString("project_details", projectDetailsString);
            editor.commit();
         }
        
        private void getMapPrefs(SharedPreferences sharedPreferences){
            java.lang.reflect.Type mapType = new TypeToken<HashMap<String, String>>(){}.getType();
            java.lang.reflect.Type charseqType = new TypeToken<String[]>(){}.getType();
            java.lang.reflect.Type mapcharseqType = new TypeToken<Map<String,String[]>>(){}.getType();
            String workspaceDetailsString = sharedPreferences.getString("workspace_details", "");
            if(workspaceDetailsString != ""){
                workspaceDetails = gson.fromJson(workspaceDetailsString, mapType);
            }
            String projectDetailsString = sharedPreferences.getString("project_details", "");
            if(projectDetailsString != ""){
                projectDetails = gson.fromJson(projectDetailsString, mapcharseqType);
            }
            String workspaceEntriesString = sharedPreferences.getString("workspace_entries", "");
            if(workspaceEntriesString != ""){
                workspaceEntries = gson.fromJson(workspaceEntriesString, charseqType);
            }
            String workspaceValuesString = sharedPreferences.getString("workspace_values", "");
            if(workspaceValuesString != ""){
                workspaceValues = gson.fromJson(workspaceValuesString, charseqType);
            }           
        }
        
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
          	if(key.equals(SERVER_DOMAIN)){
              String serverName = serverPrefs.getValue();
              Log.d("Server Name pref change",serverName);
              CodenvyClient.switchServer(Servers.valueOf(serverName));
              serverPrefs.setSummary(serverName);
              if(!CodenvyClient.isInitialised()){
                CodenvyClient.apiInit();
              }
              String temp = sharedPreferences.getString(USER_NAME, "");
                          if(!temp.equals("")){
//                 userCred.setSummary("Logged In User: " + temp);
//                 if(workspaceDetails.isEmpty()){
//                     CodenvyClient.getWorkspaceDetails(workspaceProgressDialog);
//                     }else{
//                     updateWorspacePreference();
//                     updateProjectPreference();                    
//                 }               
                CodenvyClient.getWorkspaceDetails();
                                          	 
            }

            }else if (key.equals(USER_NAME)) {
                userCred.setSummary("Logged In User: " + sharedPreferences.getString(USER_NAME, "-NONE-"));
                CodenvyClient.getWorkspaceDetails();
            }else if(key.equals(WORKSPACE)){
                worspacePrefs.setSummary(worspacePrefs.getEntry());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String workspaceName = worspacePrefs.getEntry().toString();
                editor.putString("workspace_name", workspaceName);
              	 editor.remove(PROJECT);
              editor.remove(COMMAND);
                editor.commit();
              AppData.setWorkspaceName(workspaceName);
              AppData.setWorkspaceId(worspacePrefs.getValue());
                updateProjectPreference();
              updateCommandPreference();
            }else if(key.equals(PROJECT)){
                projectPrefs.setSummary(projectPrefs.getEntry());
              AppData.setProject(projectPrefs.getEntry().toString());
            }else if(key.equals(COMMAND)){
                commandPrefs.setSummary(commandPrefs.getEntry());
              AppData.setCommand(commandPrefs.getEntry().toString());
            }
          
        }
      
        private void toastThis(String msg){
      		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    	  }
    }
    
    public static void updateWorspacePreference(){
        worspacePrefs.setEntries(workspaceEntries);
        worspacePrefs.setEntryValues(workspaceValues);
        worspacePrefs.setDefaultValue(0);
        CharSequence tempEntry = worspacePrefs.getEntry();
        if(tempEntry == null){
            tempEntry = "Please Select a Value";
        }
        updateWorkspaceSummary(tempEntry.toString());
        worspacePrefs.setEnabled(true);
//         if(projectDetails.isEmpty()){
//             getProjectLists(workspaceValues);
//             }else{
//             updateProjectPreference();
//         }
    }
    
    public static void updateWorspacePreference(String[] namesArr,String[] idsArr){
        if(namesArr.length > 0){
            workspaceEntries = namesArr;
            workspaceValues = idsArr;
            }else{
            updateWorkspaceSummary("No Workspace Available for this User");
        }
        	updateWorspacePreference();
      	getProjectLists(workspaceValues);
      	updateProjectPreference();
    }
    
    public static void getProjectLists(String[] ids){
        wids = new LinkedList<String>(Arrays.asList(ids));
        projectWids = new LinkedList<String>(Arrays.asList(ids));
        commadWids = new LinkedList<String>(Arrays.asList(ids));
        Iterator < String > widIterator = wids.iterator();
        projectProgressDialog.show();
        while (widIterator.hasNext()) {
            String wid = widIterator.next();
            String tempWid = wid.toString();
            CodenvyClient.getProjectDetails(tempWid);
          	CodenvyClient.getCommandDetails(tempWid);
        }
    }
    
    public static void addProjectMap(String wid,String[] projectsArr){
        projectDetails.put(wid,projectsArr);
        projectWids.remove(wid);
        if(projectWids.isEmpty()){
            projectProgressDialog.dismiss();
            updateProjectPreference();
        }
    }
  
  public static void addCommandMap(String wid, String[] commandArr){
    commandDetails.put(wid,commandArr);    
        commadWids.remove(wid);
        if(commadWids.isEmpty()){
            updateCommandPreference();
        }    
  }
  
  public static void updateCommandPreference(){
        String temp = sharedPreferences.getString(WORKSPACE, "");
        if(!temp.equals("")){
          Log.d("command","Inside Wid check");
            String[] commandsList = commandDetails.get(temp);
            if(commandsList != null && commandsList.length != 0){
              Log.d("command","Command list non empty");
                commandPrefs.setEntries(commandsList);
                commandPrefs.setEntryValues(commandsList);
                commandPrefs.setEnabled(true);
                temp = sharedPreferences.getString(COMMAND, "");
                if(temp.equals("")){
                    updateCommandSummary("Please Select a Value");
                    commandPrefs.setDefaultValue(0);
                    }else{
                    updateCommandSummary(temp);
                }
                }else{
              Log.d("command","Command list empty");
                updateCommandSummary("No Command Available for the selected Workspace");
                commandPrefs.setEnabled(false);
            }
        }else{
              Log.d("command","Command wid empty");
          updateCommandSummary("No Command");
          commandPrefs.setEnabled(false);
        }
  }
  
  public static void updateServerPreference(){
    String temp = sharedPreferences.getString(SERVER_DOMAIN, "");
        if(temp.equals("")){
          serverPrefs.setValue(Servers.getDefaultValue());
          SharedPreferences.Editor editor = sharedPreferences.edit();
          editor.putString(SERVER_DOMAIN, Servers.getDefaultValue());
          editor.commit();
          updateServerSummary(Servers.getDefaultValue());
        }else{
          updateServerSummary(temp);
        }
  }
    
    public static void addWorspaceMap(String wid,String name){
        workspaceDetails.put(wid,name);
    }
    
//     public static void popWids(String wid){
//         wids.remove(wid);
//     }
    
    private static void updateProjectPreference(){
        String temp = sharedPreferences.getString(WORKSPACE, "");
        if(!temp.equals("")){
            String[] projectsList = projectDetails.get(temp);
            if(projectsList != null && projectsList.length != 0){
                projectPrefs.setEntries(projectsList);
                projectPrefs.setEntryValues(projectsList);
                projectPrefs.setEnabled(true);
                temp = sharedPreferences.getString(PROJECT, "");
                if(temp.equals("")){
                    updateProjectSummary("Please Select a Value");
                    projectPrefs.setDefaultValue(0);
                    }else{
                    updateProjectSummary(temp);
                }
                }else{
                updateProjectSummary("No Project Available for the selected Workspace");
                projectPrefs.setEnabled(false);
            }
        }else{
          updateProjectSummary("No Project");
          projectPrefs.setEnabled(false);
        }
    }
    
    public static void updateWorkspaceSummary(String statusText){
        worspacePrefs.setSummary(statusText);
    }
    
    public static void updateProjectSummary(String statusText){
        projectPrefs.setSummary(statusText);
    }
      public static void updateCommandSummary(String statusText){
        commandPrefs.setSummary(statusText);
    }
  
      public static void updateServerSummary(String statusText){
        serverPrefs.setSummary(statusText);
    }
  
  
  

}