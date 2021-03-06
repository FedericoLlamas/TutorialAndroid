package ar.edu.unc.famaf.redditreader.ui;

import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class VoteTask extends AsyncTask<String, Integer,Boolean> {

    String name;

    public VoteTask(String name){
        this.name = name;

    }

    @Override
    protected void onPreExecute() { //REcibe dir
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String dir = params[0];
        String authorizacion = "bearer "+ NewsActivity.accessToken;

        String urlParameters  = "dir="+dir+"&id="+name+"&rank=3";
        byte[] postData       = new byte[0];
        try {
            postData = urlParameters.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int    postDataLength = postData.length;
        String request        = "https://oauth.reddit.com/api/vote";
        URL    url            = null;
        try {
            url = new URL( request );
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        HttpURLConnection conn= null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput( true );
            conn.setRequestProperty ("Authorization", authorizacion);
            conn.setRequestProperty ("User-Agent", "Reddit Reader");
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            conn.setUseCaches( false );
            DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
            wr.write( postData );
            wr.flush();

            StringBuilder sb = new StringBuilder();
            int HttpResult = conn.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                String json = sb.toString();
                System.out.println("....." + json);
                return true;
            } else if (HttpResult == HttpURLConnection.HTTP_UNAUTHORIZED) {
                System.out.println(conn.getResponseMessage());
                NewsActivity.ACTIVE_USER = false;
            }else{
                NewsActivity.ACTIVE_USER = false;
                System.out.println(conn.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
