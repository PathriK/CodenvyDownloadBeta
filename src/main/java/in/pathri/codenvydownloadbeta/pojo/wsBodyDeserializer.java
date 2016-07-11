package in.pathri.codenvydownloadbeta.pojo;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import in.pathri.codenvydownloadbeta.CustomLogger;

public class wsBodyDeserializer implements JsonDeserializer<Body>{
	private static final String className = wsBodyDeserializer.class.getSimpleName();
  public synchronized Body deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	  CustomLogger.d(className,"","JSON Contents",json.getAsString());
	 JsonObject o = new JsonParser().parse(json.getAsString()).getAsJsonObject();
    return new Gson().fromJson(o, Body.class);
  }
}
