package com.r3Tech.Thoughts.Activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.r3Tech.Thoughts.Adapter.MenuListAdapter;
import com.r3Tech.Thoughts.Fragment.frgMainListing;
import com.r3Tech.Thoughts.R;
import com.r3Tech.Thoughts.Utils.clsGeneral;
import com.r3Tech.Thoughts.Utils.dlgDefaultMenuAlert;


public class actMainListing extends ActionBarActivity {

    String DISP_MODE = "latest";

    private int SELECTED_FRAGEMENT;
    private DrawerLayout DRAWER_LAYOUT;
    private ListView DRAWER_LIST_VIEW;
    private ActionBarDrawerToggle DRAWER_TOGGLE;
    private MenuListAdapter ADPT_MENULST;
    private String[] ARR_TITLE;
    private String[] ARR_SUBTITLE;
    private int[] ARR_ICON;
    private frgMainListing FRA_MAINLISTING;
    private String CATEGORY = "0";
//    private String CATEGORY_STRING;

    // Activity controls
    TextView TV_HEADER, TV_SUBHEADER;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlistinglayout);

        FRA_MAINLISTING = new frgMainListing();
        CATEGORY = this.getIntent().getExtras().getString(getResources().getString(R.string.category));
//        CATEGORY_STRING = this.getIntent().getExtras().getString(getResources().getString(R.string.category_string));
        initialPageControl(savedInstanceState);
        // Set up actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.header);
        TV_HEADER = (TextView) findViewById(R.id.tvHeader);
        TV_HEADER.setText(getResources().getString(R.string.HeaderTextMainListingRecent));
        TV_SUBHEADER = (TextView) findViewById(R.id.tvSubHeader);
        TV_SUBHEADER.setText(getResources().getString(R.string.HeaderTextMainSubHeader));
        clsGeneral.changeFonts(getApplicationContext(), TV_HEADER);
        clsGeneral.changeFonts(getApplicationContext(), TV_SUBHEADER);
    }

    private void initialPageControl(Bundle savedInstanceState) {
        ARR_TITLE = new String[]{"Category"};

        ARR_SUBTITLE = new String[]{"Select Category"};

        ARR_ICON = new int[]{R.drawable.menucategory};

        DRAWER_LAYOUT = (DrawerLayout) findViewById(R.id.drawer_layout);
        DRAWER_LIST_VIEW = (ListView) findViewById(R.id.left_drawer);
        DRAWER_LAYOUT.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        ADPT_MENULST = new MenuListAdapter(this, ARR_TITLE, ARR_SUBTITLE, ARR_ICON);
        DRAWER_LIST_VIEW.setAdapter(ADPT_MENULST);
        DRAWER_LIST_VIEW.setOnItemClickListener(new DrawerItemClickListener());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DRAWER_TOGGLE = new ActionBarDrawerToggle(this, DRAWER_LAYOUT,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                // TODO Auto-generated method stub
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.app_name);
            }

            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
            }
        };

        DRAWER_LAYOUT.setDrawerListener(DRAWER_TOGGLE);

        if (savedInstanceState == null) {
            selectItem(10);
        }
    }

    public void setHeaderText(String headerText) {
        TV_HEADER.setText(headerText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.defaultmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (DRAWER_LAYOUT.isDrawerOpen(DRAWER_LIST_VIEW)) {
                DRAWER_LAYOUT.closeDrawer(DRAWER_LIST_VIEW);
            } else {
                DRAWER_LAYOUT.openDrawer(DRAWER_LIST_VIEW);
            }
        }
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

    private void selectItem(int position) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                startActivity(new Intent(actMainListing.this, actCategoryListing.class));
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case 10:
                Bundle bundle = new Bundle();
                bundle.putString(getResources().getString(R.string.category), CATEGORY);
//                bundle.putString(getResources().getString(R.string.category_string), CATEGORY_STRING);
                FRA_MAINLISTING.setArguments(bundle);
                ft.replace(R.id.content_frame, FRA_MAINLISTING);
                break;
        }
        ft.commit();
        DRAWER_LIST_VIEW.setItemChecked(position, true);
        DRAWER_LAYOUT.closeDrawer(DRAWER_LIST_VIEW);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DRAWER_TOGGLE.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DRAWER_TOGGLE.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon)
                .setTitle(getResources().getString(R.string.are_you_sure_want_to_exit))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        actMainListing.super.onBackPressed();
                    }
                }).create().show();
    }
}

