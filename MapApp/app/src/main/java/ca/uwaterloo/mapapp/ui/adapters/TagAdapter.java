package ca.uwaterloo.mapapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.objects.Tag;

/**
 * Created by Kev on 2015-07-06.
 */
public class TagAdapter extends BaseAdapter {
    Context mContext;
    List<Tag> mTags;
    private static LayoutInflater inflater = null;

    public TagAdapter(Context context, List<Tag> tags) {
        this.mContext = context;
        this.mTags = tags;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return mTags.size(); }

    @Override
    public Tag getItem(int position) {
        return mTags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTags.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.list_item_tag, null);

        TextView titleView = (TextView)view.findViewById(R.id.tag_title);
        titleView.setText(mTags.get(position).getTitle());

        return view;
    }
}
