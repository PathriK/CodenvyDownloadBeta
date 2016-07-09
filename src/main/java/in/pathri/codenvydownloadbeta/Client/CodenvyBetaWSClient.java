	package in.pathri.codenvydownloadbeta.Client;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import in.pathri.codenvydownloadbeta.pojo.Body;
import in.pathri.codenvydownloadbeta.pojo.Channels;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponse;
import in.pathri.codenvydownloadbeta.pojo.CodenvyResponseWS;
import in.pathri.codenvydownloadbeta.pojo.wsBodyDeserializer;
import in.pathri.codenvydownloadbeta.responsehandlers.ApiResponseHandler;

public class CodenvyBetaWSClient extends WebSocketAdapter{
	private WebSocket ws;
	
  private static String BASE_URL = "ws://beta.codenvy.com/api/ws";
  private static String CHANNEL_HEADER_NAME = "x-everrest-websocket-message-type";
  private static String CHANNEL_HEADER_VALUE = "subscribe-channel";
  private static final int TIMEOUT = 5000;
  
  private Channels channel;
  private ApiResponseHandler <CodenvyResponseWS> responseHandler;
  
  private static Table<String, Channels,CodenvyBetaWSClient> clientChannelMap = HashBasedTable.create();
  
  public CodenvyBetaWSClient(String wid, Channels channel) throws IOException{
	  this.ws = new WebSocketFactory()
      .setConnectionTimeout(TIMEOUT)
      .createSocket(BASE_URL + "/" + wid)
      .addListener(this)
      .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE);	 
    this.channel = channel;    
  }
  
  public static CodenvyBetaWSClient getInstance(String wid, Channels channel){
    if(!clientChannelMap.contains(wid,channel)){
      try {
		clientChannelMap.put(wid,channel,new CodenvyBetaWSClient(wid,channel));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    CodenvyBetaWSClient tempClient = clientChannelMap.get(wid, channel);
    tempClient.connect();
    return tempClient;
    
  }
  
  private void connect(){
	  try {
		if(!ws.isOpen()){
			ws.connect();
		}
	} catch (WebSocketException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public void initChannel(String param, ApiResponseHandler <CodenvyResponseWS> responseHandler){
    System.out.println("INIT Channel" + ws.isOpen());

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
	    ws.sendText(msg); 	    
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

  }
  
  public void closeChannel(){
	  ws.disconnect();
  }
  
  public void closeAllChannel(){
	  
  }
  
  
    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception
    {
    	System.out.println( "opened connection" );
    }

    @Override
    public void onDisconnected(WebSocket websocket,WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,boolean closedByServer) throws Exception
    {
    	System.out.println( "Connection closed by " + ( closedByServer ? "remote peer" : "us" ) + "serverCloseFrame:" + serverCloseFrame + "::clientCloseFrame" + clientCloseFrame );
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception
    {
		System.out.println( "received: " + text );
	     
		GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Body.class, new wsBodyDeserializer());
      Gson gson = builder.create();

      CodenvyResponse response = (CodenvyResponse)gson.fromJson(text, channel.getResponseClass());
     if(response.getStatusCode().equals("0")){
       this.responseHandler.nextStep(response);
     }
    	
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception
    {
    	System.out.println("onBinaryMessage");
    }
    
    ///Error Methods
    private void errorLogger(String errorName, Throwable cause){
  	  System.out.println("WSCLIENT::Error Occured-" + errorName + "::" + cause.getMessage());
  	  cause.printStackTrace();
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception
    {
    	errorLogger("onError", exception);
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception
    {
    	errorLogger("onError", cause);
    }


    @Override
    public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception
    {
    	errorLogger("onFrameError", cause);
    }


    @Override
    public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception
    {
    	errorLogger("onMessageError", cause);
    }


    @Override
    public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception
    {
    	errorLogger("onMessageDecompressionError", cause);
    }


    @Override
    public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception
    {
    	errorLogger("onTextMessageError", cause);
    }


    @Override
    public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception
    {
    	errorLogger("onSendError", cause);
    }


    @Override
    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception
    {
    	errorLogger("onUnexpectedError", cause);
    }


    @Override
    public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception
    {
    	errorLogger("handleCallbackError", cause);
    }

}
