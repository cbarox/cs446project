package ca.uwaterloo.mapapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.objects.Note;

/**
 * Created by Kev Kat on 2015-06-15.
 */
public class NoteAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context mContext;
    List<Note> notes;

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

        TextView titleView = (TextView)view.findViewById(R.id.item_title);
        TextView descView = (TextView)view.findViewById(R.id.item_desc);
        titleView.setText(notes.get(position).getTitle());
        descView.setText(notes.get(position).getDescription());

        ImageView circle = (ImageView)view.findViewById(R.id.item_circle);
        circle.setColorFilter(mContext.getResources().getColor(R.color.primary));


        return view;
    }
}
