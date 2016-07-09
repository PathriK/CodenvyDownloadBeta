	package in.pathri.codenvydownloadbeta.Client;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.WebSocket.READYSTATE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.pathri.codenvydownloadbeta.pojo.Body;
import in.pathri.codenvydownloadbeta.pojo.Channels;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponseWS;
import in.pathri.codenvydownloadbeta.pojo.wsBodyDeserializer;
import in.pathri.codenvydownloadbeta.responsehandlers.ApiResponseHandler;

public class CodenvyBetaWSClient2 extends WebSocketClient{
  private static String BASE_URL = "ws://beta.codenvy.com/api/ws";
  private static String CHANNEL_HEADER_NAME = "x-everrest-websocket-message-type";
  private static String CHANNEL_HEADER_VALUE = "subscribe-channel";
  
  private Channels channel;
  private ApiResponseHandler <CodenvyResponseWS> responseHandler;
  
  private static Table<String, Channels,CodenvyBetaWSClient2> clientChannelMap = HashBasedTable.create();
  
  public CodenvyBetaWSClient2(String wid, Channels channel) throws URISyntaxException{
	 super(new URI(BASE_URL + "/" + wid), new Draft_17());
    this.channel = channel;    
  }
  
  public static CodenvyBetaWSClient2 getInstance(String wid, Channels channel){
    if(!clientChannelMap.contains(wid,channel)){
      try {
		clientChannelMap.put(wid,channel,new CodenvyBetaWSClient2(wid,channel));
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    CodenvyBetaWSClient2 tempClient = clientChannelMap.get(wid, channel);
    tempClient.connect();
    return tempClient;
    
  }
  
  public void initChannel(String param, ApiResponseHandler <CodenvyResponseWS> responseHandler){
    System.out.println(this.getReadyState());
    if(this.getReadyState() != READYSTATE.CONNECTING && this.getReadyState() != READYSTATE.OPEN){
      this.connect();    
      System.out.println("Called COnnect");
    }
    this.responseHandler = responseHandler;
    
    JSONObject headersObj = new JSONObject();
    try {
		headersObj.putOpt("name",CHANNEL_HEADER_NAME);
	    headersObj.putOpt("value",CHANNEL_HEADER_VALUE);
	
	    JSONArray headersArr = new JSONArray();
	    headersArr.put(headersObj);
	    
	    JSONObject bodyObj = new JSONObject();
	    bodyObj.putOpt("channel",channel.getChannel(param));
	    
	    JSONObject msgObj = new JSONObject();
	    msgObj.putOpt("method","POST");
	    msgObj.putOpt("headers",headersArr);
	    msgObj.putOpt("body",bodyObj.toString());
	    
	    String msg = msgObj.toString();
	    
	    System.out.println("Message: " + msg);
	       System.out.println(this.getReadyState());
	    this.send(msg); 	    
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

  }
  
  public void closeChannel(){
	  this.close();
  }
  
  public void closeAllChannel(){
	  
  }
  
  	@Override
	public void onOpen( ServerHandshake handshakedata ) {
		System.out.println( "opened connection" );
// 		this.initChannel();
	}

	@Override
	public void onMessage( String message ) {
		System.out.println( "received: " + message );
     
		GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Body.class, new wsBodyDeserializer());
      Gson gson = builder.create();

      CodenvyResponse response = (CodenvyResponse)gson.fromJson(message, channel.getResponseClass());
     if(response.getStatusCode().equals("0")){
       this.responseHandler.nextStep(response);
     }
	}

	@Override
	public void onClose( int code, String reason, boolean remote ) {
		// The codecodes are documented in class org.java_websocket.framing.CloseFrame
		System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) + "reason:" + reason );
	}

	@Override
	public void onError( Exception ex ) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
	}
}
