package ar.edu.unc.famaf.redditreader.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {
    static final int REQUEST =0;
    private static final String TAG = "MyActivity";
    public static final String AUTH_URL =
            "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" +
                    "&response_type=code&state=%s&redirect_uri=%s&" +
                    "duration=permanent&scope=identity,vote";

    public static final String CLIENT_ID = "Cqk_PzT6pdxmHg";

    public static final String REDIRECT_URI =
            "http://www.example.com/my_redirect";

    public static final String STATE = "MY_RANDOM_STRING_1";

    public static final String ACCESS_TOKEN_URL =
            "https://www.reddit.com/api/v1/access_token";

    public static  String accessToken;
    public static  String refreshToken;

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume.......");
        if(getIntent()!=null && getIntent().getAction().equals(Intent.ACTION_VIEW)) {
            Uri uri = getIntent().getData();
            if(uri.getQueryParameter("error") != null) {
                String error = uri.getQueryParameter("error");
                Log.e(TAG, "An error has occurred : " + error);
            } else {
                String state = uri.getQueryParameter("state");
                if(state.equals(STATE)) {
                    String code = uri.getQueryParameter("code");
                    getAccessToken(code);
                }
            }
        }
    }

    private void getAccessToken(String code) {
        System.out.println("getAccess");
        OkHttpClient client = new OkHttpClient();
        String authString = CLIENT_ID + ":";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(),
                Base64.NO_WRAP);
        Request request = new Request.Builder()
                .addHeader("User-Agent", "Sample App")
                .addHeader("Authorization", "Basic " + encodedAuthString)
                .url(ACCESS_TOKEN_URL)
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                        "grant_type=authorization_code&code=" + code +
                                "&redirect_uri=" + REDIRECT_URI))
                .build();
        System.out.println(request.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "ERROR: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("onResponse");
                String json = response.body().string();

                JSONObject data = null;
                try {
                    data = new JSONObject(json);
                    accessToken = data.optString("access_token");
                    refreshToken = data.optString("refresh_token");
                    NewsActivity.LOGIN = true;
                    Log.d(TAG, "Access Token = " + accessToken);
                    Log.d(TAG, "Refresh Token = " + refreshToken);
                    me_detail(accessToken, refreshToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private boolean me_detail (String token, String refreshToken){

        String authorizacion = "bearer "+ token;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "Reddit Reader")
                .addHeader("Authorization", authorizacion)
                .url("https://oauth.reddit.com/api/v1/me")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "ERROR: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("onResponse GET");
                String json = response.body().string();
                System.out.println(json);
                JSONObject data = null;
                try {
                    data = new JSONObject(json);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return  true;
    }

}