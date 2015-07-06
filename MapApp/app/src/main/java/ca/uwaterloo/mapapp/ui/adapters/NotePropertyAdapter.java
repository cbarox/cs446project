package ca.uwaterloo.mapapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.objects.Note;

/**
 * Created by Kev on 2015-06-29.
 */
public class NotePropertyAdapter extends BaseAdapter {
    protected static LayoutInflater inflater = null;
    protected Context mContext;
    protected Note mNote;

    public NotePropertyAdapter(Context context, Note note) {
        this.mContext = context;
        this.mNote = note;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mNote == null ? 0 : 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.list_item_note_property, null);

        ImageView icon = (ImageView)view.findViewById(R.id.note_property_icon);
        TextView info = (TextView)view.findViewById(R.id.note_property_text);

        switch (position) {
            case 0: // building
                icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_store_black_24dp));
                icon.setVisibility(View.VISIBLE);
                info.setText(mNote.getBuildingCode());
                break;
            case 1:
                icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_subject_black_24dp));
                info.setText(mNote.getDescription());
                break;
        }

        return view;
    }
}
