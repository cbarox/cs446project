package ca.uwaterloo.mapapp.ui.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.objects.Note;
import ca.uwaterloo.mapapp.objects.NoteTag;

/**
 * Created by Kev on 2015-06-29.
 */
public class NotePropertyAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Context mContext;
    protected List<Pair<Integer, String>> properties;

    protected NotePropertyAdapter(Context context) {
        this.mContext = context;
        properties = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public NotePropertyAdapter(Context context, Note note, List<NoteTag> tags) {
        this(context);

        if (!note.getBuildingCode().isEmpty()) {
            Pair<Integer, String> row = new Pair<>(R.drawable.ic_store_black_24dp, note.getBuildingCode());
            properties.add(row);
        }
        if (!note.getDescription().isEmpty()) {
            Pair<Integer, String> row = new Pair<>(R.drawable.ic_subject_black_24dp, note.getDescription());
            properties.add(row);
        }
        if (!tags.isEmpty()) {
            String values = "";
            int i=0;
            for (; i < tags.size()-1; i++) {
                values += "#" + tags.get(i).getTag().getTitle() + "\n";
            }
            values += "#" + tags.get(i).getTag().getTitle();

            Pair<Integer, String> row = new Pair<>(R.drawable.ic_announcement_black_24dp, values);
            properties.add(row);
        }
    }

    @Override
    public int getCount() {
        return properties.size();
    }

    @Override
    public Object getItem(int position) {
        return properties.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.list_item_note_property, null);

        ImageView icon = (ImageView)view.findViewById(R.id.note_property_icon);
        TextView info = (TextView)view.findViewById(R.id.note_property_text);

        Pair<Integer, String> property = properties.get(position);
        icon.setImageDrawable(mContext.getResources().getDrawable(property.first));
        info.setText(property.second);

        return view;
    }
}
