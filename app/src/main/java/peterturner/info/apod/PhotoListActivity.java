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

    // How many photos to fetch beyond what is needed for current views.
    private static final int FETCH_THRESHOLD = 4;

    // Base Url
    String mBaseUrl = "https://api.nasa.gov/planetary/apod?api_key=" + API_KEY;

    PhotoAdapter mAdapterPhotos;
    ArrayList<Photo> mPhotos;
    int mHighestFetched = -1;
    SimpleDateFormat mDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        mPhotos = new ArrayList<Photo>();
        mAdapterPhotos = new PhotoAdapter(this, mPhotos);
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Populate initial 5 cells.
        Log.d("PETER", "initial population");
        fetchPhotoRange(0, FETCH_THRESHOLD);

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
                if (lastVisibleItem >= mHighestFetched - FETCH_THRESHOLD) {
                    fetchPhotoRange(mHighestFetched + 1, mHighestFetched + FETCH_THRESHOLD);
                }
                Log.d("PETER", "onScroll last: " + lastVisibleItem);
            }
        });
    }

    private void fetchPhotoRange(final int startIndex, final int endIndex) {
        if (mHighestFetched >= startIndex) {
            Log.e("Error", "Re-fetching data: Highest fetched " + mHighestFetched + " startIndex " + startIndex + " endIndex " + endIndex);
        }
        mPhotos.ensureCapacity(endIndex);
        for (int i = startIndex; i <= endIndex; ++i) {
            fetchPhoto(i);
        }
        mHighestFetched = endIndex;
    }

    private void fetchPhoto(final int index) {
        int offsetInDays = -index;
        Log.d("PETER", "Fetching index " + index);

        Calendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
        calendar.add(Calendar.DAY_OF_YEAR, offsetInDays);

        String formatted = mDateFormat.format(calendar.getTime());

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
                    Photo photo = new Photo();
                    photo.url = response.getString("url");
                    photo.title = response.getString("title");
                    photo.explanation = response.getString("explanation");

                    String dateString = response.getString("date");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                         photo.date = format.parse(dateString);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    photo.copyright = response.optString("copyright");

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

