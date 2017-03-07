package com.r3Tech.Thoughts.Activity;


import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.r3Tech.Thoughts.R;
import com.r3Tech.Thoughts.Utils.clsGeneral;
import com.r3Tech.Thoughts.Utils.dlgDefaultMenuAlert;


public class actAbout extends ActionBarActivity implements View.OnClickListener {

    // ------------------------------------------------------------------------------------------------------------ onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutlayout);
        // Set up actionbar
        TextView _tvHeader = (TextView) findViewById(R.id.tvHeader);
        _tvHeader.setText(getResources().getString(R.string.HeaderTextAboutHeader));
        TextView _tvSubHeader = (TextView) findViewById(R.id.tvSubHeader);
        _tvSubHeader.setText(getResources().getString(R.string.HeaderTextAboutSubheader));
        clsGeneral.changeFonts(getApplicationContext(), _tvHeader);
        clsGeneral.changeFonts(getApplicationContext(), _tvSubHeader);

        String _aboutText = "";
        _aboutText = getString(R.string.AboutThisAppText);

        try {
            // Add version name.
            _aboutText += "\n\nVersion : " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "\n\n";
        } catch (NameNotFoundException e) {
        }

        TextView _tvAbout = (TextView) findViewById(R.id.tvAbout);
        _tvAbout.setText(_aboutText);

//        BTN_MENU = (ImageButton) findViewById(R.id.btnMenu);
//        BTN_MENU.setOnClickListener(this);

        //Change Fonts
        RelativeLayout _rLay = (RelativeLayout) findViewById(R.id.layParent);
        clsGeneral.changeFonts(getApplicationContext(), _rLay);
    }

    // ------------------------------------------------------------------------------------------------------------ onClick

    @Override
    public void onClick(View view) {
    }

    // ------------------------------------------------------------------------------------------------------------ Menu inflater and listeners
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.aboutmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.mnuIfYouLike:
                new dlgDefaultMenuAlert().show(getSupportFragmentManager(), "1");
                break;
//            case R.id.mitemFeedback:
//                clsGeneral.shareAndSupport(this, "feedback");
//                break;
//            case R.id.mitemReview:
//                clsGeneral.shareAndSupport(this, "review");
//                break;
//            case R.id.mitemShareApp:
//                clsGeneral.shareAndSupport(this, "shareapp");
//                break;
        }
        return true;
    }
}
