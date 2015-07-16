package ca.uwaterloo.mapapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.shared.objects.event.Event;

/**
 * Created by Kev Kat on 2015-06-15.
 */
public class EventAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context mContext;
    List<Event> events;

    public EventAdapter(Context context, List<Event> events) {
        this.mContext = context;
        this.events = events;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return events.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.list_item_event, null);

        Event event = events.get(position);

        TextView titleView = (TextView)view.findViewById(R.id.item_title);
        TextView locView = (TextView)view.findViewById(R.id.item_location);
        titleView.setText(event.getTitle());
        if (event.getLocation() == null || !event.getLocation().isEmpty())
            locView.setText("UW");
        else
            locView.setText(event.getLocation());

        return view;
    }
}
