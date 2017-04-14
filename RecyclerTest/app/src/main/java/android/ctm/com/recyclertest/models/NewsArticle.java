package android.ctm.com.recyclertest.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Paul on 4/13/17.
 */

public class NewsArticle implements Parcelable {

    private int id;

    private String url;
    private String imageUrl;

    private String humanDate;

    private String title;

    private String description;

    public NewsArticle () {

    }

    public NewsArticle (String url, String imageUrl, String humanDate, String title, String description) {
        this.url = url;
        this.imageUrl = imageUrl;
        this.humanDate = humanDate;
        this.title = title;
        this.description = description;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHumanDate() {
        return humanDate;
    }

    public void setHumanDate(String humanDate) {
        this.humanDate = humanDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(imageUrl);
        dest.writeString(humanDate);
        dest.writeString(title);
        dest.writeString(description);
    }

    public void readFromParcel(Parcel source) {
        url = source.readString();
        imageUrl = source.readString();
        humanDate = source.readString();
        title = source.readString();
        description = source.readString();
    }

    public static final Parcelable.Creator<NewsArticle> CREATOR = new Parcelable.Creator<NewsArticle>() {

        public NewsArticle[] newArray(int size) {
            return new NewsArticle[size];
        }

        public NewsArticle createFromParcel(Parcel source) {
            NewsArticle object = new NewsArticle();
            object.readFromParcel(source);

            return object;
        }
    };
}
