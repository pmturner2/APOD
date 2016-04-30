package peterturner.info.apod;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.ParseException;

public class PhotoListActivity extends AppCompatActivity {

    // Nasa API Key
    private static final String API_KEY = "8xLqaN9LQZVF6a7kAi1kupue3xIXVz1M45ga5bxe";

    // Base Url
    String mBaseUrl = "https://api.nasa.gov/planetary/apod?api_key=" + API_KEY;

    TextView mTitle;
    PhotoAdapter mAdapterPhotos;
    ArrayList<Photo> mPhotos;
    int mHighestFetched = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        mTitle = (TextView) findViewById(R.id.tvTitle);
        mPhotos = new ArrayList<Photo>();
        mAdapterPhotos = new PhotoAdapter(this, mPhotos);

        // Populate initial 5 cells.
        Log.d("PETER", "initial population");
        for (int i = 0; i < 5; ++i) {
            fetchPhoto(i);
            mHighestFetched = 4;
        }

        ListView lv = (ListView) findViewById(R.id.lvPhotos);
        lv.setAdapter(mAdapterPhotos);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("PETER", "onScroll first " + firstVisibleItem + " mHighest: " + mHighestFetched);
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (lastVisibleItem > mHighestFetched) {
                    for (int i = mHighestFetched + 1; i <= lastVisibleItem; ++i) {
                        fetchPhoto(i);
                    }
                    mHighestFetched = lastVisibleItem;
                }
                Log.d("PETER", "onScroll last: " + lastVisibleItem);
            }
        });
    }

    private void fetchPhoto(final int index) {
        int offsetInDays = -index;
        Log.d("PETER", "Fetching index " + index);

        Calendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
        calendar.add(Calendar.DAY_OF_YEAR, offsetInDays);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = dateFormat.format(calendar.getTime());

        String url = mBaseUrl + "&date=" + formatted;
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("PETER", "Attempting to fetch url " + url);
        // url, params, response handler
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("PETER", "Success " + index);

                try {
                    Photo photo = new Photo(
                            response.getString("url"),
                            response.getString("hdurl"),
                            response.getString("title"),
                            response.getString("media_type"),
                            response.getString("service_version"),
                            response.getString("explanation")
                    );

                    String dateString = response.getString("date");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                         photo.date = format.parse(dateString);

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (index < mPhotos.size()) {
                        mPhotos.add(index, photo);
                    } else {
                        mPhotos.add(photo);
                    }
                } catch (Exception e) {

                }
                mAdapterPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("PETER", "Fail " + index);
            }
        });
    }
}

