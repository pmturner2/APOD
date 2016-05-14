package peterturner.info.apod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.impl.cookie.DateUtils;

/**
 * Created by pturner on 4/30/16.
 */
public class PhotoAdapter extends ArrayAdapter<Photo> {
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd-yyyy");
    public static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public TextView tvExplanation;
        public ImageView ivPhoto;
    }

    public PhotoAdapter(Context context, ArrayList<Photo> photos) {
        super(context, 0, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get data item for position
        Photo photo = getItem(position);

        ViewHolder viewHolder;
        // Check if recycled view
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            // no recycled view, create a new view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvExplanation = (TextView) convertView.findViewById(R.id.tvExplanation);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            convertView.setTag(viewHolder);
        }

        viewHolder.tvTitle.setText(photo.title);
        viewHolder.tvDate.setText(mDateFormat.format(photo.date));
        viewHolder.tvExplanation.setText(photo.explanation);
        viewHolder.ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.url).into(viewHolder.ivPhoto);

        // Return converted View.
        return convertView;
    }
}
