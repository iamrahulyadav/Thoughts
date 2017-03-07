package com.r3Tech.Thoughts.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.mqnvnfx.itwsdvr70223.AdListener;
import com.mqnvnfx.itwsdvr70223.AdView;
import com.mqnvnfx.itwsdvr70223.MA;

import com.r3Tech.Thoughts.Activity.actDetailsView;
import com.r3Tech.Thoughts.Activity.actMainListing;
import com.r3Tech.Thoughts.Adapter.adptMainListing;
import com.r3Tech.Thoughts.R;
import com.r3Tech.Thoughts.Utils.clsGeneral;
import com.r3Tech.Thoughts.Utils.entItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Bhavin on 3/9/14.
 */
public class frgMainListing extends ListFragment implements AbsListView.OnScrollListener, View.OnClickListener {

    adptMainListing ADAPTER;

    private int PAGE_NUMBER = 0;//, CATEGORY = 0;
    private String DISP_MODE = "latest", CATEGORY = "0";//, CATEGORY_DATA = "";

    // The flag is true when async task is loading items (async task is in progress).
    boolean LOADING_DATA = false;

    // The flag is true when all the items from server are fetched. No more items to load.
    boolean LIST_ENDED_ON_SERVER = false;

    // Activity controls
    //TextView TV_HEADER, TV_SUBHEADER;
    ProgressBar PROGRESS;
    Button BTN_RECENT_LIST, BTN_POPULAR_LIST, BTN_FAV_LIST;
    ArrayList<Integer> datakey;

    // -------- AD -------
    private int AP_AD_ITEM_SELECTED_COUNT = 0;
    MA AP_MA_SMARTWALL;
    LinearLayout AP_BANNER_AD_PARENT;
    AdView AP_BANNER_AD;
    // -------- AD -------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View _v = inflater.inflate(R.layout.mainlistingfragmentlayout, null);
        CATEGORY = getArguments().getString(getActivity().getResources().getString(R.string.category));
//        CATEGORY_DATA = getArguments().getString(getActivity().getResources().getString(R.string.category_string));
        datakey = new ArrayList<Integer>();
//        datakey.add(91);
//        datakey.add(92);
//        datakey.add(93);
//        datakey.add(94);
//        datakey.add(95);
//        datakey.add(96);
//        datakey.add(97);
        return _v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Setup activity controls
        PROGRESS = (ProgressBar) getActivity().findViewById(R.id.prgLoading);

        BTN_RECENT_LIST = (Button) getActivity().findViewById(R.id.btnRecentList);
        BTN_RECENT_LIST.setOnClickListener(this);
        BTN_POPULAR_LIST = (Button) getActivity().findViewById(R.id.btnPopularList);
        BTN_POPULAR_LIST.setOnClickListener(this);
        BTN_FAV_LIST = (Button) getActivity().findViewById(R.id.btnFavList);
        BTN_FAV_LIST.setOnClickListener(this);

        ADAPTER = new adptMainListing(getActivity().getApplicationContext());
        setListAdapter(ADAPTER);
        getListView().setOnScrollListener(this);

        //Change Fonts
        RelativeLayout _rLay = (RelativeLayout) getActivity().findViewById(R.id.layParent);
        clsGeneral.changeFonts(getActivity().getApplicationContext(), _rLay);

        // Display Ads
        apShowSmartWallAd();
        apShowBannerAd();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onClick(View v) {
        if (PROGRESS.getVisibility() == View.VISIBLE) return;

        switch (v.getId()) {
            case R.id.btnRecentList:
                BTN_RECENT_LIST.setBackgroundDrawable(getResources().getDrawable(R.drawable.footerselection));
                BTN_POPULAR_LIST.setBackgroundColor(getResources().getColor(R.color.Transparent));
                BTN_FAV_LIST.setBackgroundColor(getResources().getColor(R.color.Transparent));
                datakey.clear();
                reorderList("latest");
                break;

            case R.id.btnPopularList:
                BTN_RECENT_LIST.setBackgroundColor(getResources().getColor(R.color.Transparent));
                BTN_POPULAR_LIST.setBackgroundDrawable(getResources().getDrawable(R.drawable.footerselection));
                BTN_FAV_LIST.setBackgroundColor(getResources().getColor(R.color.Transparent));
                datakey.clear();
                reorderList("popular");
                break;

            case R.id.btnFavList:
                BTN_RECENT_LIST.setBackgroundColor(getResources().getColor(R.color.Transparent));
                BTN_POPULAR_LIST.setBackgroundColor(getResources().getColor(R.color.Transparent));
                BTN_FAV_LIST.setBackgroundDrawable(getResources().getDrawable(R.drawable.footerselection));
                datakey.clear();
                reorderList("fav");
                break;
        }
    }

    private void reorderList(String mode) {
        DISP_MODE = mode;
        PAGE_NUMBER = 0;
        LIST_ENDED_ON_SERVER = false;
        ADAPTER.remoteItems();
        ADAPTER.notifyDataSetChanged();
        new loadItems().execute();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        TextView _tvId = (TextView) v.findViewById(R.id.tvId);
        Integer _id = Integer.valueOf(String.valueOf(_tvId.getText()));

        Intent _intent = new Intent(getActivity().getApplicationContext(), actDetailsView.class);
        _intent.putExtra("mylist", datakey);
        _intent.putExtra("ID", _id);
        startActivityForResult(_intent, 1); // Result code 1 for remove from fav
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && DISP_MODE.equals("fav")) {
            // remove item from favorite list.
            if (data != null) {
                int _id = data.getExtras().getInt("RemoveItemFromFav");
                ADAPTER.removeItem(_id);
                ADAPTER.notifyDataSetChanged();
            }
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        // Check if the list is on last row.
        boolean _loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

        if (_loadMore && (!LOADING_DATA) && (!LIST_ENDED_ON_SERVER)) {
            /* True when
            1. ListView scrolled to last view,
			2. async task is free(not loading items already),
			3. Items are not ended on server. (server has more items which should be loaded on server) */
            new loadItems().execute();
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {/* Not required */}

    private class loadItems extends AsyncTask<Void, Void, Void> {

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
                List<entItem> _items;
                if (CATEGORY.equals(getResources().getString(R.string.all))) {
                    _items = new entItem().getItems(getActivity().getApplicationContext(), PAGE_NUMBER, "0", DISP_MODE);
                } else {
                    _items = new entItem().getItems(getActivity().getApplicationContext(), PAGE_NUMBER, CATEGORY, DISP_MODE);
                }

                PAGE_NUMBER++;
                ADAPTER.addItems(_items);

                for (int i = 0; i < _items.size(); i++) {
                    datakey.add(_items.get(i).ID);
                }

                if (_items.size() < Integer.valueOf(getResources().getString(R.string.ListPageSize))) {
                    // List is ended on server if we receive less than 10(page size) rows.
                    LIST_ENDED_ON_SERVER = true;
                }
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
                // todo error comes here
                if (isAdded()) {    // show error if fragment is attached to activity
                    clsGeneral.showToast(getActivity().getApplicationContext(), EX);
                }
                return;
            }

            ADAPTER.notifyDataSetChanged();

            // CHange page title
            if (DISP_MODE.equals("latest"))
                ((actMainListing) getActivity()).setHeaderText(getResources().getString(R.string.HeaderTextMainListingRecent));
            else if (DISP_MODE.equals("popular"))
                ((actMainListing) getActivity()).setHeaderText(getResources().getString(R.string.HeaderTextMainListingPopular));
            else if (DISP_MODE.equals("fav"))
                ((actMainListing) getActivity()).setHeaderText(getResources().getString(R.string.HeaderTextMainListingFavorites));
        }
    }

    // -------- AD -------
    private void apShowBannerAd() {
        AP_BANNER_AD_PARENT = (LinearLayout) getActivity().findViewById(R.id.layForBannerAd);
        AP_BANNER_AD = new AdView(getActivity(), AdView.BANNER_TYPE_IN_APP_AD, AdView.PLACEMENT_TYPE_INTERSTITIAL, false, false, AdView.ANIMATION_TYPE_FADE);
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
            Log.i("NBT2", "BANNER ERROR");
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
            Log.i("NBT2", "BANNER NOT AVAILABLE");
        }
    };

    private void apShowSmartWallAd() {
        AP_AD_ITEM_SELECTED_COUNT++;
        if (AP_AD_ITEM_SELECTED_COUNT % 5 != 0) return;

        if (AP_MA_SMARTWALL == null)
            AP_MA_SMARTWALL = new MA(getActivity(), apSmartwallCallbackListener, true);

        AP_MA_SMARTWALL.callSmartWallAd();
    }

    AdListener apSmartwallCallbackListener = new AdListener() {
        @Override
        public void onSmartWallAdShowing() {

        }

        @Override
        public void onSmartWallAdClosed() {

        }

        @Override
        public void onAdError(String s) {
            Log.i("NBT2", "WALL ERROR");
            if (s.equals("SmartWall Ad already available in cache. Request Ignored.")) {
                try {
                    AP_MA_SMARTWALL.showCachedAd(getActivity(), AdType.smartwall);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onSDKIntegrationError(String s) {

        }

        @Override
        public void onAdCached(AdListener.AdType adType) {
            try {
                AP_MA_SMARTWALL.showCachedAd(getActivity(), adType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void noAdAvailableListener() {
            Log.i("NBT2", "WALL NOT AVAILABLE");
        }
    };
    // -------- AD -------
}
