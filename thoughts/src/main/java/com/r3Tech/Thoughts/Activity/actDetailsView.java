package com.r3Tech.Thoughts.Activity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

import com.mqnvnfx.itwsdvr70223.AdListener;
import com.mqnvnfx.itwsdvr70223.AdView;
import com.r3Tech.Thoughts.Fragment.fragDetails;
import com.r3Tech.Thoughts.Fragment.frgFBAuttAndShare;
import com.r3Tech.Thoughts.R;
import com.r3Tech.Thoughts.Utils.clsGeneral;
import com.r3Tech.Thoughts.Utils.dlgDefaultMenuAlert;
import com.r3Tech.Thoughts.Utils.entItem;

import java.util.ArrayList;
import java.util.HashMap;


public class actDetailsView extends ActionBarActivity implements OnClickListener {

    Integer ID;
    entItem ITEM;

    // The flag is true when async task is loading items (async task is in progress).
    boolean LOADING_DATA = false;
    ProgressBar PROGRESS;

    ImageButton LIKE_BUTTON, FAV_BUTTON, SHARE_FB_BUTTON, SHARE_REGULAR_BUTTON;
    private frgFBAuttAndShare FB_AUTH_FRAGMENT;
    private LinearLayout LAY_FB_CONTAINER;

    // -------- AD -------
    //    private int AP_AD_ITEM_SELECTED_COUNT = -1;
    //    MA AP_MA_SMARTWALL;
    LinearLayout AP_BANNER_AD_PARENT;
    AdView AP_BANNER_AD;
    // -------- AD -------

    private HashMap<Integer, fragDetails> mPageReferenceMap = null;
    ViewPager PAGER;
    FragmentStatePagerAdapter ADAPTER;
//    String[] arr;

    //    List<Integer> datakey;
    ArrayList<Integer> DATAFROMSERVER;
    TextView _tvHeader, _tvSubHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsviewlayout);
        mPageReferenceMap = new HashMap<Integer, fragDetails>();
        // Set up actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.header);

        // Get the id from intent
        ID = getIntent().getIntExtra("ID", 0);
        DATAFROMSERVER = (ArrayList<Integer>) getIntent().getSerializableExtra("mylist");
        // Set activity result to cancel
        setResult(RESULT_CANCELED);

        // Activity controls
        PROGRESS = (ProgressBar) findViewById(R.id.prgLoading);
        LIKE_BUTTON = (ImageButton) findViewById(R.id.btnLike);
        FAV_BUTTON = (ImageButton) findViewById(R.id.btnFav);
        SHARE_FB_BUTTON = (ImageButton) findViewById(R.id.btnShareFacebook);
        SHARE_REGULAR_BUTTON = (ImageButton) findViewById(R.id.btnShareRegular);
        LAY_FB_CONTAINER = (LinearLayout) findViewById(R.id.layFBContainer);
        _tvHeader = (TextView) findViewById(R.id.tvHeader);
        _tvSubHeader = (TextView) findViewById(R.id.tvSubHeader);
        // Setup Listners
        LIKE_BUTTON.setOnClickListener(this);
        FAV_BUTTON.setOnClickListener(this);
        SHARE_FB_BUTTON.setOnClickListener(this);
        SHARE_REGULAR_BUTTON.setOnClickListener(this);

        //Change Fonts
        RelativeLayout _rLay = (RelativeLayout) findViewById(R.id.layParent);
        clsGeneral.changeFonts(getApplicationContext(), _rLay);

        // Load data from database
//        new loadItem().execute();

        //Facebook call
        FBAuthenticationAndShare(savedInstanceState);

        // Display Ads
        //        showSmartWallAd();
        AP_BANNER_AD = findViewById(R.id.myAdView);
        AP_BANNER_AD.loadAd();

//        datakey = new ArrayList<Integer>();
//        datakey.add(91);
//        datakey.add(92);
//        datakey.add(93);
//        datakey.add(94);
//        datakey.add(95);
//        datakey.add(96);
//        datakey.add(97);

//        arr = new String[]{"91", "92", "93", "94", "95", "96", "97"};
        PAGER = (ViewPager) findViewById(R.id.test_pager);

        ADAPTER = new MyAdapter(getSupportFragmentManager());
        PAGER.setAdapter(ADAPTER);

        int index = DATAFROMSERVER.indexOf(ID);
        PAGER.setCurrentItem(index);

        PAGER.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                int index = PAGER.getCurrentItem();
                MyAdapter adapter = ((MyAdapter) PAGER.getAdapter());
                fragDetails fragment = adapter.getFragment(index);
                // Update My favorite text on header
                String _title = ((TextView) (fragment.getView().findViewById(R.id.tvTitle))).getText().toString();
                String _category = ((TextView) (fragment.getView().findViewById(R.id.tvCategoty))).getText().toString();
                String isFav = ((TextView) (fragment.getView().findViewById(R.id.tvIsFav))).getText().toString();
                String _description = ((TextView) (fragment.getView().findViewById(R.id.tvDescription))).getText().toString();
                if (!_description.equals("")) FB_AUTH_FRAGMENT.setFBPostTitleAndDescription(_title, _description);
                _tvHeader.setText(_title);
                if (isFav.equals("fav")) {
                    _tvSubHeader.setText(_category + " (My favorite)");
                } else if (isFav.equals("nofav")) {
                    _tvSubHeader.setText(_category.replace(" (My favorite)", ""));
                }
                clsGeneral.changeFonts(getApplicationContext(), _tvHeader);
                clsGeneral.changeFonts(getApplicationContext(), _tvSubHeader);
            }
        });
    }

    public class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return DATAFROMSERVER.size();
        }

        @Override
        public Fragment getItem(int position) {
            fragDetails fragDetails = com.r3Tech.Thoughts.Fragment.fragDetails.newInstance(DATAFROMSERVER.get(position));
            mPageReferenceMap.put(position, fragDetails);
            return fragDetails;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }

        public fragDetails getFragment(int key) {
            return mPageReferenceMap.get(key);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Do not go back if share dialog is open. Just close it.
            if (LAY_FB_CONTAINER.getVisibility() == View.VISIBLE) {
                LAY_FB_CONTAINER.setVisibility(View.GONE);
                return true;
            }
            if (ITEM != null) {
                // While going back,
                // If item is un faved in this activity, remove from the fav list (Parent activity).
                if (ITEM.IS_FAV == 0) {
                    Intent _intResult = new Intent();
                    _intResult.putExtra("RemoveItemFromFav", ITEM.ID);
                    setResult(RESULT_OK, _intResult);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onClick(View v) {
        if (LOADING_DATA) {
            clsGeneral.showToast(this, getResources().getString(R.string.PleaseWait));
            return;
        }

        if (LAY_FB_CONTAINER.getVisibility() == View.VISIBLE) {
            return;
        }

        if (v.getId() == LIKE_BUTTON.getId()) new increaseLikeCount().execute();
        else if (v.getId() == FAV_BUTTON.getId()) new addToFavList().execute();
        else if (v.getId() == SHARE_FB_BUTTON.getId()) {
            if (LAY_FB_CONTAINER.getVisibility() == View.GONE) {
                LAY_FB_CONTAINER.setVisibility(View.VISIBLE);
            }
        } else if (v.getId() == SHARE_REGULAR_BUTTON.getId()) {

            int index = PAGER.getCurrentItem();
            MyAdapter adapter = ((MyAdapter) PAGER.getAdapter());
            fragDetails fragment = adapter.getFragment(index);

            String desc = ((TextView) (fragment.getView().findViewById(R.id.tvDescription))).getText().toString();
            String title = ((TextView) (fragment.getView().findViewById(R.id.tvTitle))).getText().toString();

//            String str = ((TextView) getSupportFragmentManager().
//                    getFragments().get(PAGER.getCurrentItem()).getView().
//                    findViewById(R.id.tvDescription)).getText().toString();

            Intent _shareIntent = new Intent(Intent.ACTION_SEND);
            _shareIntent.setType("text/plain");
            _shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            _shareIntent.putExtra(Intent.EXTRA_TEXT, desc);
            startActivity(Intent.createChooser(_shareIntent, "Share with friends"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.defaultmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.mnuIfYouLike:
                new dlgDefaultMenuAlert().show(getSupportFragmentManager(), "1");
                break;
            case R.id.mnuAboutApp:
                clsGeneral.shareAndSupport(this, "about");
                break;
        }
        return true;
    }

    public void bindView() {
        if (ITEM.ID == 0) {
            clsGeneral.showToast(this, getResources().getString(R.string.NoDataFoundError));
            return;
        }

        TextView _tvHeader = (TextView) findViewById(R.id.tvHeader);
        TextView _tvSubHeader = (TextView) findViewById(R.id.tvSubHeader);
        TextView _tvStatus = (TextView) findViewById(R.id.tvStatus);
        TextView _tvDesctiption = (TextView) findViewById(R.id.tvDescription);

        _tvHeader.setText(ITEM.TITLE);
        _tvSubHeader.setText(ITEM.CATEGORY);
        if (ITEM.IS_FAV == 1) _tvSubHeader.setText(_tvSubHeader.getText() + " (My favorite)");

        clsGeneral.changeFonts(getApplicationContext(), _tvHeader);
        clsGeneral.changeFonts(getApplicationContext(), _tvSubHeader);

        String _statusString = ITEM.VIEWS + " Views";
        _statusString += "  |  " + ITEM.LIKES + " Likes";
        //_statusString += "  |  Published on " + ITEM.PUBLISH_DATE;
        _tvStatus.setText(_statusString);
        _tvDesctiption.setText(ITEM.FULL_DESC);
    }

    public void hideFbContainerLayout() {
        LAY_FB_CONTAINER.setVisibility(View.GONE);
    }

    private void FBAuthenticationAndShare(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            FB_AUTH_FRAGMENT = new frgFBAuttAndShare();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.layFBContainer, FB_AUTH_FRAGMENT)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            FB_AUTH_FRAGMENT = (frgFBAuttAndShare) getSupportFragmentManager().findFragmentById(android.R.id.content);
        }
    }

    private class loadItem extends AsyncTask<Void, Void, Void> {

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
                ITEM = new entItem().getItem(actDetailsView.this, ID);
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
                clsGeneral.showToast(actDetailsView.this, EX);
                return;
            }

            actDetailsView.this.bindView();
            try {
                if (ITEM != null) FB_AUTH_FRAGMENT.setFBPostTitleAndDescription(ITEM.TITLE, ITEM.FULL_DESC);
            } catch (NullPointerException ex) {
                //suppress this error
            }
        }
    }


    private class increaseLikeCount extends AsyncTask<Void, Void, Void> {

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
                new entItem().increaseLikeCount(actDetailsView.this, ID);
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
                clsGeneral.showToast(actDetailsView.this, EX);
                return;
            }

            // Show user that post is liked
            clsGeneral.showToast(actDetailsView.this, getResources().getString(R.string.PostLiked));
        }
    }


    private class addToFavList extends AsyncTask<Void, Void, Integer> {

        Exception EX = null;
        private int IS_FAV;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LOADING_DATA = true;
            PROGRESS.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                int index = PAGER.getCurrentItem();
                MyAdapter adapter = ((MyAdapter) PAGER.getAdapter());
                fragDetails fragment = adapter.getFragment(index);
                // Update My favorite text on header
                String isFav = ((TextView) (fragment.getView().findViewById(R.id.tvIsFav))).getText().toString();

                if (isFav.equals("fav")) {
                    new entItem().addToFavList(actDetailsView.this, DATAFROMSERVER.get(PAGER.getCurrentItem()), "nofav");

                    IS_FAV = 0;
                    //--
                } else if (isFav.equals("nofav")) {
                    new entItem().addToFavList(actDetailsView.this, DATAFROMSERVER.get(PAGER.getCurrentItem()), "fav");

                    IS_FAV = 1;
                }
            } catch (Exception e) {
                EX = e;
            }
            return IS_FAV;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            LOADING_DATA = false;
            PROGRESS.setVisibility(View.INVISIBLE);

            if (EX != null) {
                clsGeneral.showToast(actDetailsView.this, EX);
                return;
            }
            int index = PAGER.getCurrentItem();
            MyAdapter adapter = ((MyAdapter) PAGER.getAdapter());
            fragDetails fragment = adapter.getFragment(index);
            // Update My favorite text on header
            String subTitle = ((TextView) (fragment.getView().findViewById(R.id.tvCategoty))).getText().toString();
//

//
            // Show user that items is added or removed in fav list.
            if (result == 1) {
                clsGeneral.showToast(actDetailsView.this, getResources().getString(R.string.PostFaved));
                _tvSubHeader.setText(subTitle + " (My favorite)");
                ((TextView) (fragment.getView().findViewById(R.id.tvIsFav))).setText("fav");
            } else if (result == 0) {
                clsGeneral.showToast(actDetailsView.this, getResources().getString(R.string.PostNoFaved));
                _tvSubHeader.setText(subTitle.replace(" (My favorite)", ""));
                ((TextView) (fragment.getView().findViewById(R.id.tvIsFav))).setText("nofav");
            }
        }
    }
}
