package com.r3Tech.Thoughts.Activity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.r3Tech.Thoughts.R;
import com.r3Tech.Thoughts.Utils.clsGeneral;
import com.r3Tech.Thoughts.Utils.clsPrefes;
import com.r3Tech.Thoughts.Utils.entUsers;


public class actMain extends ActionBarActivity {

    // ------------------------------------------------------------------------------------------------------------ Global Declaration
    private Boolean APP_STARTED = false;
    // The flag is true when async task is loading items (async task is in progress).
    boolean LOADING_DATA = false;
    ProgressBar PROGRESS;

    // ------------------------------------------------------------------------------------------------------------ onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        new clsPrefes(getApplicationContext()).putUserFBDetailsServiceCalled(false);

        try {
            // FB KEY GEN
            new clsGeneral().generateHashKeyForFacebook(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        //		.detectAll()
        //		.penaltyLog()
        //		.penaltyDialog()
        //		.build());
        //		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
        //				.penaltyLog()
        //				.build());

        // Activity controls
        PROGRESS = (ProgressBar) findViewById(R.id.prgLoading);

        // Check user's unique id and call next activity.
        new checkUniqueUserId().execute();

        //Change Fonts
        RelativeLayout _rLay = (RelativeLayout) findViewById(R.id.layParent);
        clsGeneral.changeFonts(getApplicationContext(), _rLay);
    }

    // ------------------------------------------------------------------------------------------------------------ onRestart
    @Override
    protected void onRestart() {
        super.onRestart();

        if (APP_STARTED == true) {
            finish();
        }
    }

    // ------------------------------------------------------------------------------------------------------------ onDestroy
    @Override
    protected void onDestroy() {
        if (APP_STARTED == true) finish();
        super.onDestroy();
    }

    // ------------------------------------------------------------------------------------------------------------ Async to checkUniqueUserId
    private class checkUniqueUserId extends AsyncTask<Void, Void, Void> {

        Exception EX = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LOADING_DATA = true;
            PROGRESS.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                new entUsers().UserLogin(actMain.this);
            } catch (Exception e) {
                EX = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            LOADING_DATA = false;
            PROGRESS.setVisibility(View.INVISIBLE);

            if (EX != null) {
                clsGeneral.showToast(actMain.this, EX);
                return;
            }

            // Start new Activity
            APP_STARTED = true;

            Intent intent = new Intent(actMain.this, actMainListing.class);
            intent.putExtra(getResources().getString(R.string.category), "0");
//            intent.putExtra(getResources().getString(R.string.category_string), "");
            startActivity(intent);
            finish();
        }
    }
}