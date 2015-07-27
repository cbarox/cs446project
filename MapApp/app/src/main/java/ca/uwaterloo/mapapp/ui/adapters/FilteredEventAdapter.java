package ca.uwaterloo.mapapp.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.objects.Note;
import ca.uwaterloo.mapapp.shared.objects.event.Event;

/**
 * Created by Kev on 2015-07-19.
 */
public class FilteredEventAdapter extends EventAdapter {
    private String filterName;

    public FilteredEventAdapter(Context context, List<Event> events, String filterName) {
        super(context, events);
        this.filterName = filterName;
    }

    @Override
    public int getCount() {
        return events.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return null;
        }
        return super.getItem(position-1);
    }

    @Override
    public long getItemId(int position) {
        if (position == 0) {
            return -1;
        }
        return super.getItemId(position - 1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (position == 0) {
            view = View.inflate(mContext, R.layout.list_item_header, null);
            TextView title = (TextView)view.findViewById(R.id.header_title);
            TextView count = (TextView)view.findViewById(R.id.header_note_count);

            title.setText(filterName);
            count.setText("" + super.getCount() + " Events");

        } else {
            view = super.getView(position-1, null, parent);
        }

        return view;
    }
}
