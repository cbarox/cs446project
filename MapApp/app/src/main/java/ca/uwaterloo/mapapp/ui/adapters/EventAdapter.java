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
        import ca.uwaterloo.mapapp.logic.net.objects.event.Event;

/**
 * Created by Kev Kat on 2015-06-15.
 */
public class EventAdapter extends BaseAdapter {
    Context mContext;
    List<Event> events;
    private static LayoutInflater inflater = null;

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

        TextView titleView = (TextView)view.findViewById(R.id.item_title);
        TextView descView = (TextView)view.findViewById(R.id.item_desc);
        titleView.setText(events.get(position).getTitle());
        descView.setText("");

        ImageView circle = (ImageView)view.findViewById(R.id.item_circle);
        circle.setColorFilter(mContext.getResources().getColor(R.color.primary));

        return view;
    }
}
