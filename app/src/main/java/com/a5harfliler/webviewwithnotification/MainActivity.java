package com.a5harfliler.webviewwithnotification;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.a5harfliler.webviewwithnotification.Parser.PostParser;
import com.a5harfliler.webviewwithnotification.model.HttpManager;
import com.a5harfliler.webviewwithnotification.model.Post;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;




import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static android.R.attr.tag;
import static com.a5harfliler.webviewwithnotification.R.id.webView;


public class MainActivity extends Activity {

    TextView textView;
    ProgressBar progressBar;
    List<MyTask> tasks;
    public static final String JSONsrc = "http://nycosborne.com/WordPressTest/wp-json/wp/v2/posts";

    List<Post> postList;

    NotificationCompat.Builder notifyOBJ;
    static Post latestPost = new Post();

    Timer timer;
    TimerTask timerTask;
    final android.os.Handler handler = new android.os.Handler();
    private WebView webview;
   // Uri alarmSound = RingtoneManager.getActualDefaultRingtoneUri()
    private static final int uniuqID = 23423;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tasks = new ArrayList<>();

        notifyOBJ = new NotificationCompat.Builder(this);
        notifyOBJ.setAutoCancel(true);
        latestPost.setId(0); webview =(WebView)findViewById(webView);

        webview.setWebViewClient(new WebViewClient());
        webview .getSettings().setJavaScriptEnabled(true);
        webview .getSettings().setDomStorageEnabled(true);
        webview.loadUrl("http://www.apple.com/");



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
        timer.schedule(timerTask, 10000, 10000); //
    }


    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                        requestData(JSONsrc);
                        //show the toast
//                        int duration = Toast.LENGTH_SHORT;
//                        Toast toast = Toast.makeText(getApplicationContext(), strDate, duration);
//                        toast.show();
                    }
                });
            }
        };
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            //if Back key pressed and webview can navigate to previous page
            webview.goBack();
            // go back to previous page
            return true;
        }
        else
        {
            finish();
            // finish the activity
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item1) {
            if (isOnline()) {
                //requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                //requestData(JSONsrc);
            } else {
                Toast.makeText(this, "Not Online", Toast.LENGTH_LONG).show();
            }
        }
        if (item.getItemId() == R.id.menu_item2) {
            //setNotifyOBJ();
            Toast.makeText(this, "Not Online", Toast.LENGTH_LONG).show();
        }


        return false;
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }


//    private void updateDisply() {
//        if (postList != null) {
//            textView.setText(" ");
//            textView.setTextSize(18);
//            for (Post post : postList) {
//
//                textView.append(post.getId() + "\n" + "\n" + "\n");
//
//            }
//
//
//        } else {
//
//            textView.setText("Shit still null");
//        }
//
//
//    }

    private void checkIfUpDated() {

        if (latestPost.getId() < postList.get(0).getId()) {
            Log.d("MainActivity", "After if befor try" + latestPost.getId());
            if (latestPost.getId() > 0) {
                Log.d("MainActivity", "New Post???!!!!!!!!");
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
                //    private void setNotifyOBJ(){
//        notifyOBJ.setSmallIcon(R.mipmap.ic_launcher);
//        notifyOBJ.setContentTitle("Aler!!");
//        notifyOBJ.setContentText("This is the body of the Alert");
//        Intent resultIntent = new Intent(this, MainActivity.class);
//
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
//                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationManager notificationManager =(NotificationManager)
//                getSystemService(NOTIFICATION_SERVICE);
//
//        notificationManager.notify(uniuqID, notifyOBJ.build());
//    }
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
            // updateDisply("Starting task from background");

//            if (tasks.size() == 0) {
//                progressBar.setVisibility(View.VISIBLE);
//            }
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

//            FlowerJSONParser flowerJSONParser = new FlowerJSONParser();
//            postList = flowerJSONParser.parseFeed(result);

            PostParser postParser = new PostParser();
           try {
               postList = postParser.parseFeed(result);
           }catch (Exception e){
               e.printStackTrace();
           }


            //  updateDisply2(result);
            //updateDisply();
            checkIfUpDated();

            tasks.remove(this);
//            if (tasks.size() == 0) {
//                progressBar.setVisibility(View.INVISIBLE);
//            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            //this values Array is contraled my the publishProgress();
            // to have more then one eliment in the array publishProgress();
            // needs to be loaded with multiple agrs
            //updateDisply(values[0]);
        }
    }

}