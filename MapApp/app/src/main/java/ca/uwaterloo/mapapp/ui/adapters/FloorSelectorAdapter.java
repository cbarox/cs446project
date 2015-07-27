package ca.uwaterloo.mapapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.objects.floorplan.Floor;

/**
 * Created by Kev on 2015-07-03.
 */
public class FloorSelectorAdapter extends BaseAdapter {

    private Context mContext;
    private List<Floor> mFloors;

    private static LayoutInflater inflater;

    public FloorSelectorAdapter(Context context, List<Floor> floors) {
        this.mContext = context;
        this.mFloors = floors;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.mFloors.size();
    }

    @Override
    public Object getItem(int position) {
        return mFloors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.list_item_button, null);

        TextView label = (TextView)view.findViewById(R.id.list_button);
        label.setText(mFloors.get(position).getName());

        return view;
    }
}
