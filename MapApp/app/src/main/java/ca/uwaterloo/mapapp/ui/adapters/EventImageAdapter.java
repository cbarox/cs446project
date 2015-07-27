package ca.uwaterloo.mapapp.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.shared.objects.event.EventImage;

/**
 * Created by brwarner2 on 26/07/2015.
 */
public class EventImageAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context mContext;
    List<EventImage> images;

    public EventImageAdapter(Context context, List<EventImage> images) {
        this.mContext = context;
        this.images = images;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return images.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.grid_item_image, null);

        EventImage image = images.get(position);

        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        byte[] decodedString = Base64.decode(image.getBase64(), Base64.URL_SAFE);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);

        return view;
    }
}
