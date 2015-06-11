package ca.uwaterloo.mapapp.logic.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import ca.uwaterloo.mapapp.data.objects.Building;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by cjbarrac
 * 6/11/15
 */
public class NetworkHelper {
    public static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Building.class, new ApiAdapter<Building>())
            .registerTypeAdapter(List.class, new ApiAdapter<List>())
            .create();
    public static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(WaterlooApiService.API_ENDPOINT)
            .setConverter(new GsonConverter(gson))
            .build();
    public static WaterlooApiService service = restAdapter.create(WaterlooApiService.class);
}
