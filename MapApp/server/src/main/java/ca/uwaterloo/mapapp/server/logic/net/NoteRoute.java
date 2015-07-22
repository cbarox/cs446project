package ca.uwaterloo.mapapp.server.logic.net;

import com.google.gson.Gson;

import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.shared.net.ServerResponse;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.EventNote;
import java.util.List;
import spark.Request;
import spark.Response;

/**
 * Created by brwarner2 on 20/07/2015.
 */
public class NoteRoute implements IGetSetDeleteRoute {
    private static Gson gson = new Gson();

    @Override
    public Object get(Request request, Response response) throws Exception {
        final DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        
        DataManager<EventNote, String> notesDataManager = databaseHelper.getDataManager(EventNote.class);
        List<EventNote> notes = notesDataManager.find(EventNote.COLUMN_EVENT_ID, Integer.parseInt(request.params(":event")));
        
        return gson.toJson(notes)); 
    }

    @Override
    public Object set(Request request, Response response) throws Exception {
        final DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        
        List<EventNote> notes = gson.fromJson(request.body(), List<EventNote>.class);
        DataManager<EventNote, String> notesDataManager = databaseHelper.getDataManager(EventNote.class);
        notesDataManager.insertOrUpdateAll(notes);
        
        return "success";
    }

    @Override
    public Object delete(Request request, Response response) throws Exception {
        final DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        
        EventNote note = new EventNote();
        note.setId(Integer.parseInt(request.params(":id")));
        
        DataManager<EventNote, String> notesDataManager = databaseHelper.getDataManager(EventNote.class);
        if( !notesDataManager.delete(note) )
            return "fail";
            
        return "success";
    }
}
