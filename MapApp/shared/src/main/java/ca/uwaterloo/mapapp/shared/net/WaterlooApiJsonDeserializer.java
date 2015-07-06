package ca.uwaterloo.mapapp.shared.net;

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


/**
 * Created by cjbarrac
 * 6/10/15
 */
public class WaterlooApiJsonDeserializer<T> implements JsonDeserializer<T> {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static Date deserializeDate(String date) {
        String[] split = date.split("\\+");
        String timezone = split[1].replace(":", "");
        String dateString = split[0] + "+" + timezone;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
        }
        return null;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        //TODO Add null checking
        JsonElement dataElement = jsonObject.get("data");
        return GSON.fromJson(dataElement, typeOfT);
    }
}
