package peterturner.info.apod;

import java.util.Date;

/**
 * Created by pturner on 4/30/16.
 */
public class Photo {
    public String url;
    public String hdUrl;
    public String title;
    public String mediaType;
    public String serviceVersion;
    public String explanation;
    public Date date;

    public Photo(String url, String hdUrl, String title, String mediaType, String serviceVersion, String explanation) {
        this.url = url;
        this.hdUrl = hdUrl;
        this.title = title;
        this.mediaType = mediaType;
        this.serviceVersion = serviceVersion;
        this.explanation = explanation;
    }
}
