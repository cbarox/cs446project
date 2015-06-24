package ca.uwaterloo.mapapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.objects.Note;

/**
 * Created by Kev Kat on 2015-06-15.
 */
public class NoteAdapter extends BaseAdapter {
    Context mContext;
    List<Note> notes;
    private static LayoutInflater inflater = null;

    public NoteAdapter(Context context, List<Note> notes) {
        this.mContext = context;
        this.notes = notes;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.list_item_note, null);

        TextView textView = (TextView)view.findViewById(R.id.title);
        textView.setText(notes.get(position).getTitle());

        return view;
    }
}
