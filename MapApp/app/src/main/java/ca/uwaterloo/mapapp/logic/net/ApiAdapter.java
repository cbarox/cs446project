package ca.uwaterloo.mapapp.logic.net;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by cjbarrac
 * 6/10/15
 */
public class ApiAdapter<T> implements JsonDeserializer<T> {
    //TODO Add null checking
    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement dataElement = jsonObject.get("data");
        return gson.fromJson(dataElement, typeOfT);
    }
}
