package in.pathri.codenvydownloadbeta.Client;

import org.java_websocket.client.WebSocketClient;

import in.pathri.codenvydownloadbeta.Client.Channels;

import in.pathri.codenvydownloadbeta.pojo.CodenvyResponseWS;

import retrofit2.Callback;

public class CodenvyBetaWSClient extends WebSocketClient{
  private static String BASE_URL = "ws://beta.codenvy.com/api/ws";
  private static String CHANNEL_HEADER_NAME = "x-everrest-websocket-message-type";
  private static String CHANNEL_HEADER_VALUE = "subscribe-channel";
  
  private Channels channel;
  private Callback <CodenvyResponseWS> responseHandler;
  
  private static Table<String, Channels,CodenvyBetaWSClient> clientChannelMap = HashBasedTable.create();
  
  public CodenvyBetaWSClient(String wid, Channels channel){
    this.channel = channel;
    super(new URI(BASE_URL + "/" + wid), new Draft_17());
  }
  
  public static CodenvyBetaWSClient getInstance(String wid, Channels channel){
    if(!clientChannelMap.contains(wid,channel)){
      clientChannelMap.put(wid,channel,new CodenvyBetaWSClient(wid,channel));
    }
    
    return clientChannelMap.get(wid, channel);
    
  }
  
  public void initChannel(String param, ApiResponseHandler responseHandler){
    
    this.responseHandler = responseHandler;
    
    JSONObject headersObj = new JSONObject();
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
       
    this.send(msg);    
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

      CodenvyResponse response = gson.fromJson(message, CodenvyResponseWS.class);
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
