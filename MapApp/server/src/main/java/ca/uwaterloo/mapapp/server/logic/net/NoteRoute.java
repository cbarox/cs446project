package ca.uwaterloo.mapapp.server.logic.net;

import java.util.List;

import ca.uwaterloo.mapapp.server.MagicLogger;
import ca.uwaterloo.mapapp.server.Main;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.event.EventNote;
import spark.Request;
import spark.Response;

/**
 * Created by brwarner2 on 20/07/2015.
 */
public class NoteRoute implements IGetSetDeleteRoute {

    @Override
    public Object get(Request request, Response response) throws Exception {
        DataManager notesDataManager = Main.getDataManager(EventNote.class);
        List<EventNote> notes = notesDataManager.find(EventNote.COLUMN_EVENT_ID, Integer.parseInt(request.params(":event")));

        response.status(200);
        MagicLogger.log("Successfully got %d notes", notes.size());
        return Main.GSON.toJson(notes);
    }

    @Override
    public Object set(Request request, Response response) throws Exception {
        EventNote note = Main.GSON.fromJson(request.body(), EventNote.class);
        DataManager notesDataManager = Main.getDataManager(EventNote.class);
        notesDataManager.insertOrUpdate(note);

        response.status(200);
        MagicLogger.log("Successfully updated %s", note.toString());
        return "";
    }

    @Override
    public Object delete(Request request, Response response) throws Exception {
        EventNote note = new EventNote();
        note.setId(Long.parseLong(request.params(":id")));

        DataManager notesDataManager = Main.getDataManager(EventNote.class);
        if (!notesDataManager.delete(note)) {
            response.status(500);
            MagicLogger.log("Failed to delete %s", note.toString());
            return "Failed to delete object";
        }
        MagicLogger.log("Successfully deleted %s", note.toString());
        response.status(200);
        return "";
    }
}
