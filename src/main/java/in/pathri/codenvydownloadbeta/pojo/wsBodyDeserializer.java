package in.pathri.codenvydownloadbeta.pojo;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class wsBodyDeserializer implements JsonDeserializer<Body>{
  public Body deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	 JsonObject o = new JsonParser().parse(json.getAsString()).getAsJsonObject();
    return context.deserialize(o, Body.class);
  }
}
