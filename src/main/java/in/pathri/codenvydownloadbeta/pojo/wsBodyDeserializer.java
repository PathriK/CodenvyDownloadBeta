package in.pathri.codenvydownloadbeta.pojo;

public class wsBodyDeserializer implements JsonDeserializer<Body>{
  public Body deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	 JsonObject o = new JsonParser().parse(json.getAsString()).getAsJsonObject();
    return context.deserialize(o, Body.class);
  }
}
