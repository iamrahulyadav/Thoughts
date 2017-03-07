package com.r3Tech.Thoughts.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.facebook.*;
import com.facebook.widget.LoginButton;
import com.r3Tech.Thoughts.Activity.actDetailsView;
import com.r3Tech.Thoughts.R;
import com.r3Tech.Thoughts.Utils.clsGeneral;
import com.r3Tech.Thoughts.Utils.clsPrefes;
import com.r3Tech.Thoughts.Utils.svcUpdateUserFBDetailsOnServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class frgFBAuttAndShare extends Fragment implements View.OnClickListener {

    private static final String FB_TAG = " ----------- FB_AUTH ---------- ";
    private UiLifecycleHelper UI_HELPER;
    private Button BTN_SHARE;
    private ProgressBar PROGRESS;
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean PENDING_PUBLISH_REAUTHORIZATION = false;
    private String TITLE = "", DESCRIPTION = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UI_HELPER = new UiLifecycleHelper(getActivity(), callback);
        UI_HELPER.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fbauthandshare, container, false);
        LoginButton authButton = (LoginButton) _view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        if (savedInstanceState != null) {
            PENDING_PUBLISH_REAUTHORIZATION = savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
        }
        return _view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BTN_SHARE = (Button) getActivity().findViewById(R.id.btnFBShare);
        BTN_SHARE.setOnClickListener(this);
        clsGeneral.changeFonts(getActivity().getApplicationContext(), BTN_SHARE);
        PROGRESS = (ProgressBar) getActivity().findViewById(R.id.prgLoading);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception, Boolean calledFromOnResume) {
        if (state.isOpened()) {
            BTN_SHARE.setVisibility(View.VISIBLE);
            if (PENDING_PUBLISH_REAUTHORIZATION == true && state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
                PENDING_PUBLISH_REAUTHORIZATION = false;
                publishStory(TITLE, DESCRIPTION);
            }
            if (calledFromOnResume == false && new clsPrefes(getActivity().getApplicationContext()).getUserFBDetailsServiceCalled() == false) {
                getActivity().startService(new Intent(getActivity(), svcUpdateUserFBDetailsOnServer.class));
            }
        } else if (state.isClosed()) {
            BTN_SHARE.setVisibility(View.GONE);
            new clsPrefes(getActivity().getApplicationContext()).putUserFBDetailsServiceCalled(false);
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception, false);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null, true);
        }
        // ============================================================
        UI_HELPER.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UI_HELPER.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        UI_HELPER.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UI_HELPER.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PENDING_PUBLISH_KEY, PENDING_PUBLISH_REAUTHORIZATION);
        UI_HELPER.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == BTN_SHARE.getId()) {
            publishStory(TITLE, DESCRIPTION);
        }
    }

    public void setFBPostTitleAndDescription(String title, String description) {
        TITLE = title;
        DESCRIPTION = description;
    }

    private void publishStory(String title, String description) {
        if (PROGRESS.getVisibility() == View.VISIBLE) {
            return;
        }

        if (title.equals("") || description.equals("")) {
            return;
        }
        Session session = Session.getActiveSession();
        if (session != null) {

            // Check for publish permissions
            List<String> permissions = session.getPermissions();
            if (!isSubsetOf(PERMISSIONS, permissions)) {
                PENDING_PUBLISH_REAUTHORIZATION = true;
                Session.NewPermissionsRequest newPermissionsRequest = new Session
                        .NewPermissionsRequest(this, PERMISSIONS);
                session.requestNewPublishPermissions(newPermissionsRequest);
                return;
            }

            Bundle postParams = new Bundle();
            postParams.putString("name", title);
            postParams.putString("caption", getResources().getString(R.string.app_name));
//            postParams.putString("description", description);
            postParams.putString("message", description);
            postParams.putString("link", getResources().getString(R.string.MarketLocationLong));
            postParams.putString("picture", getResources().getString(R.string.LogoPath));

            Request.Callback callback = new Request.Callback() {
                public void onCompleted(Response response) {
                    JSONObject graphResponse = response
                            .getGraphObject()
                            .getInnerJSONObject();
                    String postId = null;
                    try {
                        postId = graphResponse.getString("id");
                    } catch (JSONException e) {
                        Log.i(FB_TAG, "JSON error " + e.getMessage());
                    }
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Toast.makeText(getActivity().getApplicationContext(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.fb_share_success), Toast.LENGTH_LONG).show();
                        ((actDetailsView) getActivity()).hideFbContainerLayout();
                    }
                    PROGRESS.setVisibility(View.GONE);
                }
            };

            Request request = new Request(session, "me/feed", postParams, HttpMethod.POST, callback);
            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
            PROGRESS.setVisibility(View.VISIBLE);
        }
    }

    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }
}