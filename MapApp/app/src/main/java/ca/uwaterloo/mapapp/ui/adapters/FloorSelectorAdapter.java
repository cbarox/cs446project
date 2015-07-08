package ca.uwaterloo.mapapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import ca.uwaterloo.mapapp.R;

/**
 * Created by Kev on 2015-07-03.
 */
public class FloorSelectorAdapter extends BaseAdapter {

    private Context mContext;
    private int numOfFloors;

    private static LayoutInflater inflater;

    public FloorSelectorAdapter(Context context, int numOfFloors) {
        this.mContext = context;
        this.numOfFloors = numOfFloors;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.numOfFloors;
    }

    @Override
    public Object getItem(int position) {
        return numOfFloors - position;
    }

    @Override
    public long getItemId(int position) {
        return numOfFloors - position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.list_item_button, null);

        Button button = (Button)view.findViewById(R.id.list_button);
        button.setText(""+getItemId(position));

        return view;
    }
}
