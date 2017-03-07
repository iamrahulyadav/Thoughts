package com.r3Tech.Thoughts.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.r3Tech.Thoughts.R;
import com.r3Tech.Thoughts.Utils.clsGeneral;
import com.r3Tech.Thoughts.Utils.entItem;

/**
 * Created by Rudrik on 4/19/2014.
 */
public class fragDetails extends Fragment {
    Integer ID;
    entItem ITEM;
    // The flag is true when async task is loading items (async task is in progress).
    boolean LOADING_DATA = false;
    ProgressBar PROGRESS;
    TextView _tvStatus, _tvDesctiption, _tvTitle, _tvIsFav, _tvCategory;
    int mNum;
    static View view;
    static String str = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.detailsviewlayoutfragment, container, false);
        PROGRESS = (ProgressBar) view.findViewById(R.id.prgLoading);
        _tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        _tvDesctiption = (TextView) view.findViewById(R.id.tvDescription);
        _tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        _tvIsFav = (TextView) view.findViewById(R.id.tvIsFav);
        _tvCategory = (TextView) view.findViewById(R.id.tvCategoty);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        view.setTag(mNum);
        new loadItem().execute();

        // Just load whatever URL this fragment is
        // created with.
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Load data from database

    }

    // This is the method the pager adapter will use
    // to create a new fragment
    public static fragDetails newInstance(int id) {
//        fragDetails f = new fragDetails();
//        f.ID = id;
//        return f;

        fragDetails f = new fragDetails();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", id);
        f.setArguments(args);
        f.ID = id;
        return f;


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
                ITEM = new entItem().getItem(getActivity().getApplicationContext(), ID);
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
                clsGeneral.showToast(getActivity().getApplicationContext(), EX);
                return;
            }

            fragDetails.this.bindView();
            try {
//                if (ITEM != null) FB_AUTH_FRAGMENT.setFBPostTitleAndDescription(ITEM.TITLE, ITEM.FULL_DESC);
            } catch (NullPointerException ex) {
                //suppress this error
            }
        }
    }

    public void bindView() {
        if (ITEM.ID == 0) {
            clsGeneral.showToast(getActivity().getApplicationContext(), getResources().getString(R.string.NoDataFoundError));
            return;
        }

//        TextView _tvHeader = (TextView) view.findViewById(R.id.tvHeader);
//        TextView _tvSubHeader = (TextView) view.findViewById(R.id.tvSubHeader);


//        _tvHeader.setText(ITEM.TITLE);
//        _tvSubHeader.setText(ITEM.CATEGORY);
//        if (ITEM.IS_FAV == 1) _tvSubHeader.setText(_tvSubHeader.getText() + " (My favorite)");
//
//        clsGeneral.changeFonts(getActivity().getApplicationContext(), _tvHeader);
//        clsGeneral.changeFonts(getActivity().getApplicationContext(), _tvSubHeader);

        String _statusString = ITEM.VIEWS + " Views";
        _statusString += "  |  " + ITEM.LIKES + " Likes";
        //_statusString += "  |  Published on " + ITEM.PUBLISH_DATE;
        _tvStatus.setText(_statusString);
        _tvDesctiption.setText(ITEM.FULL_DESC);
        _tvTitle.setText(ITEM.TITLE);
        _tvCategory.setText(ITEM.CATEGORY);
        if (ITEM.IS_FAV.equals(0))
            _tvIsFav.setText("nofav");
        else
            _tvIsFav.setText("fav");
    }
}
