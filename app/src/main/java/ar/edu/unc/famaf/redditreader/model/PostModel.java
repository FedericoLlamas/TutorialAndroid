package ar.edu.unc.famaf.redditreader.model;


import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import ar.edu.unc.famaf.redditreader.backend.ReeditDBHelper;
import ar.edu.unc.famaf.redditreader.backend.RedditEntryApi;

public class PostModel implements Serializable {
    private Integer id;
    private String mTitle;/*titulo*/
    private String mSubreddit;/*csubreddit*/
    private int mCreated;/*creado fecha*/
    private String mAuthor;
    private byte[] icon= new byte[0];

    private String thumbnail;

    private String url;
    private int comments;
    private boolean download=false;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getIcon(){return icon;}

    public void setIcon(byte[] icon2){
        this.icon= new byte[icon2.length];
        System.arraycopy(icon2, 0, this.icon,0,icon2.length);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getSubreddit() {
        return mSubreddit;
    }

    public void setSubreddit(String subreddit) {
        this.mSubreddit = subreddit;
    }

    public int getCreated() {
        return mCreated;
    }

    public void setCreated(int created) {
        this.mCreated = created;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public static byte[] getBytes(Bitmap bitmap)
    {
        byte[] image = new byte[0];
        try {
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,0, stream);
            image= stream.toByteArray();
        }catch (OutOfMemoryError e){
            e.printStackTrace();
        }catch ( NullPointerException e){
            e.printStackTrace();
        }

        return image;
    }
    public static Bitmap getImage(byte[] image){
        Bitmap b=null;
        try{
            b=BitmapFactory.decodeByteArray(image, 0, image.length);
        }catch (OutOfMemoryError e){
            e.printStackTrace();
        }
        return b;
    }

    public ContentValues toContentValues(){
        ContentValues values= new ContentValues();
        //este valor se setea una vez que se inserta en la base de datos
        //values.put(RedditDb.RedditEntry.ID, id);
        values.put(RedditEntryApi.Entry.AUTHOR, mAuthor);
        values.put(RedditEntryApi.Entry.SUBREDDIT, mSubreddit);
        values.put(RedditEntryApi.Entry.CREATED, mCreated);
        values.put(RedditEntryApi.Entry.TITLE, mTitle);
        values.put(RedditEntryApi.Entry.COMMENTS,comments);
        values.put(RedditEntryApi.Entry.THUMBNAIL, thumbnail);
        values.put(RedditEntryApi.Entry.URL, url);
        if (this.icon.length >0 ){
            values.put(RedditEntryApi.Entry.ICON, icon);
        }
        return  values;
    }
}
