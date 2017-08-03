package com.a5harfliler.webviewwithnotification;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.a5harfliler.webviewwithnotification.Parser.PostParser;
import com.a5harfliler.webviewwithnotification.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.a5harfliler.webviewwithnotification.R.id.webView;


public class MainActivity extends Activity {

    TextView textView;
    ProgressBar progressBar;
    List<MyTask> tasks;
    public static final String JSONsrc = "http://www.5harfliler.com/wp-json/wp/v2/posts";

    List<Post> postList;

    NotificationCompat.Builder notifyOBJ;
    static Post latestPost = new Post();

    Timer timer;
    TimerTask timerTask;
    final android.os.Handler handler = new android.os.Handler();
    private WebView webview;
    private ImageView imageView;

    private static final int uniuqID = 23423;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // Log.d("onCreate","Top of onCreate" + " Hummm");

        tasks = new ArrayList<>();
        imageView = (ImageView) findViewById(R.id.img);
        notifyOBJ = new NotificationCompat.Builder(this);
        notifyOBJ.setAutoCancel(true);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        notifyOBJ.setContentIntent(pendingIntent);


        latestPost.setId(0); webview =(WebView)findViewById(webView);


        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                imageView.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
            }
        });
        webview.loadUrl("http://www.5harfliler.com/");
//        webview.setWebViewClient(new WebViewClient(){
//// shouldOverrideUrlLoadin might not be the method to use for link contro
//
//            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (Uri.parse(url).getHost().equals("twitter.com")) {
//
//
//                    Log.d("shouldOverrideUrlLoading()","Is 5harfliler");
//                 //   view.loadUrl("http://www.apple.com/");
//
//                    try
//                    {
//                        // Check if the Twitter app is installed on the phone.
//                        getPackageManager().getPackageInfo("com.twitter.android", 0);
//
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity");
//                        // Don't forget to put the "L" at the end of the id.
//                        intent.putExtra("566967598", 01234567L);
//                        startActivity(intent);
//                    }
//                    catch (PackageManager.NameNotFoundException e)
//                    {
//                        return false;
//                       // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/5harfliler")));
//                    }
//
//
//
//
//                    return true;
//                }
//                Log.d("shouldOverrideUrlLoading()","Is not 5harfliler");
//                return false;
//
//
//
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                findViewById(R.id.two).setVisibility(View.GONE);
//
//                findViewById(R.id.webView).setVisibility(View.VISIBLE);
//            }
//        });




    }
    @Override
    protected void onResume() {
        super.onResume();

        //onResume we start our timer so it can start when the app comes from the background
        startTimer();
    }

    public void startTimer() {
        //set a new Time
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 10000, 5000); //
    }


    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {


                handler.post(new Runnable() {
                    public void run() {

                        requestData(JSONsrc);

                    }
                });
            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            //if Back key pressed and webview can navigate to previous page
            webview.goBack();

            return true;
        }
        else
        {
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }


    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }


    private void checkIfUpDated() {

        if (latestPost.getId() < postList.get(0).getId()) {
            Log.d("MainActivity", "After if befor try" + latestPost.getId());
            if (latestPost.getId() > 0) {
                Log.d("MainActivity", "New Post???!!!!!!!!");
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notifyOBJ.setSound(alarmSound);
                notifyOBJ.setSmallIcon(R.mipmap.ic_launcher);

                String toBeTrimed = postList.get(0).getTitle();
                String trimed = toBeTrimed.substring(13,toBeTrimed.length()-2);
                notifyOBJ.setContentTitle(trimed);

                notifyOBJ.setContentText("There are new article from feminists!!!!");
                Intent resultIntent = new Intent(this, MainActivity.class);

                PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                        resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(NOTIFICATION_SERVICE);

                notificationManager.notify(uniuqID, notifyOBJ.build());

            }
            try {
                latestPost = postList.get(0);
                Log.d("MainActivity", "After try" + latestPost.getId());


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.d("MainActivity", "Old Post!!");
        }


    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpManager httpmanager = new HttpManager();
            String contect = httpmanager.getData(params[0]);

            return contect;

        }

        @Override
        protected void onPostExecute(String result) {

            PostParser postParser = new PostParser();
           try {
               postList = postParser.parseFeed(result);
           }catch (Exception e){
               e.printStackTrace();
           }

            checkIfUpDated();

            tasks.remove(this);

        }

        @Override
        protected void onProgressUpdate(String... values) {

        }
    }

}