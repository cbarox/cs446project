package ca.uwaterloo.mapapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.shared.objects.event.EventNote;

/**
 * Created by Kev on 2015-07-24.
 */
public class EventNoteAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context mContext;
    List<EventNote> eventNotes;

    public EventNoteAdapter(Context context, List<EventNote> eventNotes) {
        this.mContext = context;
        this.eventNotes = eventNotes;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return eventNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return eventNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 12l;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.list_item_event, null);

        return view;
    }
}