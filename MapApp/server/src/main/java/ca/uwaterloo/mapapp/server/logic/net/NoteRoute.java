package ca.uwaterloo.mapapp.server.logic.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ca.uwaterloo.mapapp.server.Main;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.event.EventNote;

import java.util.Arrays;
import java.util.List;
import spark.Request;
import spark.Response;

/**
 * Created by brwarner2 on 20/07/2015.
 */
public class NoteRoute implements IGetSetDeleteRoute {
    private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    @Override
    public Object get(Request request, Response response) throws Exception {
        DataManager<EventNote, String> notesDataManager = Main.getDataManager(EventNote.class);
        List<EventNote> notes = notesDataManager.find(EventNote.COLUMN_EVENT_ID, Integer.parseInt(request.params(":event")));

        response.status(200);
        System.out.printf("NoteRoute: Successfully got notes %n" );
        return gson.toJson(notes);
    }

    @Override
    public Object set(Request request, Response response) throws Exception {
        EventNote note = gson.fromJson(request.body(), EventNote.class);
        DataManager<EventNote, String> notesDataManager = Main.getDataManager(EventNote.class);
        notesDataManager.insertOrUpdate(note);
        
        response.status(200);
        System.out.printf("NoteRoute: Successfully inserted or updated note with id %d%n", note.getId());
        return "";
    }

    @Override
    public Object delete(Request request, Response response) throws Exception {
        EventNote note = new EventNote();
        note.setId(Long.parseLong(request.params(":id")));
        
        DataManager<EventNote, String> notesDataManager = Main.getDataManager(EventNote.class);
        if( !notesDataManager.delete(note) ) {
            response.status(500);
            System.err.println("NoteRoute: Failed to delete note with id " + note.getId() + ". Database error.");
            return "Failed to delete object";
        }
        System.out.printf("NoteRoute: Successfully deleted note with id %d%n", note.getId() );
        response.status(200);
        return "";
    }
}
