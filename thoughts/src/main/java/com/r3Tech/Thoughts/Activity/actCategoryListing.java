package com.r3Tech.Thoughts.Activity;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.mqnvnfx.itwsdvr70223.AdListener;
import com.mqnvnfx.itwsdvr70223.AdView;
import com.r3Tech.Thoughts.R;


/**
 * Created by Rudrik on 4/18/2014.
 */
public class actCategoryListing extends ListActivity {

    private String[] ARR_CATEGORY;

    // -------- AD -------
    //    private int AP_AD_ITEM_SELECTED_COUNT = -1;
    //    MA AP_MA_SMARTWALL;
    LinearLayout AP_BANNER_AD_PARENT;
    AdView AP_BANNER_AD;
    // -------- AD -------

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorylistingfragmentlayout);
        ARR_CATEGORY = getResources().getStringArray(R.array.arr_category);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(actCategoryListing.this,
                R.layout.laylistitemtext,
                R.id.page_textview, ARR_CATEGORY);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String value = adapter.getItem(i);
                Intent intent = new Intent(actCategoryListing.this, actMainListing.class);
                intent.putExtra(getResources().getString(R.string.category), value.trim());
//                intent.putExtra(getResources().getString(R.string.category_string), value);
                startActivity(intent);
                finish();
            }
        });

        // Display Ads
        //        showSmartWallAd();
        apShowBannerAd();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        Intent intent = new Intent(actCategoryListing.this, actMainListing.class);
        intent.putExtra(getResources().getString(R.string.category), "0");
//                intent.putExtra(getResources().getString(R.string.category_string), value);
        startActivity(intent);
        finish();
    }

    // -------- AD -------
    private void apShowBannerAd() {
        AP_BANNER_AD_PARENT = (LinearLayout) findViewById(R.id.layForBannerAd);
        AP_BANNER_AD = new AdView(this, AdView.BANNER_TYPE_IN_APP_AD, AdView.PLACEMENT_TYPE_INTERSTITIAL, false, false, AdView.ANIMATION_TYPE_FADE);
        AP_BANNER_AD.setAdListener(apBannerAdListener);
        AP_BANNER_AD_PARENT.addView(AP_BANNER_AD);
    }

    AdListener.MraidAdListener apBannerAdListener = new AdListener.MraidAdListener() {
        @Override
        public void onAdLoadingListener() {

        }

        @Override
        public void onAdLoadedListener() {

        }

        @Override
        public void onErrorListener(String s) {
            Log.i("NBT2", "INNER BANNER ERROR");
        }

        @Override
        public void onCloseListener() {

        }

        @Override
        public void onAdExpandedListner() {

        }

        @Override
        public void onAdClickListener() {

        }

        @Override
        public void noAdAvailableListener() {
            Log.i("NBT2", "INNER BANNER NOT AVAILABLE");
        }
    };
}
