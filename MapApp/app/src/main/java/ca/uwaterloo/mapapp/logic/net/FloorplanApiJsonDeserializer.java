package ca.uwaterloo.mapapp.logic.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.uwaterloo.mapapp.logic.Logger;

/**
 * Created by brwarner
 * 6/10/15
 */
public class FloorplanApiJsonDeserializer<T> implements JsonDeserializer<T> {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        //TODO Add null checking
        JsonElement dataElement = jsonObject.get("data");
        return GSON.fromJson(dataElement, typeOfT);
    }
}
