package peterturner.info.apod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pturner on 4/30/16.
 */
public class PhotoAdapter extends ArrayAdapter<Photo> {
    public PhotoAdapter(Context context, ArrayList<Photo> photos) {
        super(context, 0, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get data item for position
        Photo photo = getItem(position);

        // Check if recycled view
        if (convertView != null) {

        } else {
            // no recycled view, create a new view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(photo.title);

        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        tvDate.setText(photo.date.toString());

        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        // Clear image until async image loaded.
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.url).into(ivPhoto);

        // Return converted View.
        return convertView;
    }
}
