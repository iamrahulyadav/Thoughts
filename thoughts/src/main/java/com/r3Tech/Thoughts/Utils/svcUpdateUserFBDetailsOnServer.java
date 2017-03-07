package com.r3Tech.Thoughts.Utils;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Bhavin on 3/26/14.
 */
public class svcUpdateUserFBDetailsOnServer extends Service {

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getUserDetailsFromFaceBook();
        return super.onStartCommand(intent, flags, startId);
    }

    private void getUserDetailsFromFaceBook() {
        String fqlQuery = "SELECT uid,name,first_name,last_name, email,birthday_date,sex  FROM user WHERE uid = me()";

        Bundle params = new Bundle();
        params.putString("q", fqlQuery);
        Session session = Session.getActiveSession();
        Request request = new Request(session, "/fql", params, HttpMethod.GET, new Request.Callback() {
            public void onCompleted(Response response) {
                GraphObject graphObject = response.getGraphObject();
                if (graphObject != null) {
                    if (graphObject.getProperty("data") != null) {
                        try {
                            String arry = graphObject.getProperty("data").toString();
                            JSONArray jsonNArray = new JSONArray(arry);
                            if (jsonNArray.length() != 1) {
                                return;
                            }
                            JSONObject jsonObject = jsonNArray.getJSONObject(0);

                            String _fbId = jsonObject.getString("uid");
                            String _firstName = jsonObject.getString("first_name");
                            String _lastName = jsonObject.getString("last_name");
                            String _email = jsonObject.getString("email");
                            String _birthDate = jsonObject.getString("birthday_date");
                            String _sex = jsonObject.getString("sex");

                            clsPrefes _prefs = new clsPrefes(getApplicationContext());
                            _prefs.putFBId(_fbId);
                            _prefs.putFirstName(_firstName);
                            _prefs.putLastName(_lastName);
                            _prefs.putEmail(_email);
                            _prefs.putDOB(_birthDate);
                            _prefs.putSex(_sex);

                            new asyncUpdateUserDetails().execute();
                        } catch (JSONException ex) {
                            if (ex != null) {
                                Log.e("NBT2", ex.getMessage());
                                stopSelf();
                            }
                        }
                    }
                }
            }
        });
        Request.executeBatchAsync(request);
    }

    private class asyncUpdateUserDetails extends AsyncTask<Void, Void, Void> {

        private Exception EX;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            clsPrefes _prefs = new clsPrefes(getApplicationContext());
            String _fbId = _prefs.getFBId();
            String _firstName = _prefs.getFirstName();
            String _lastName = _prefs.getLastName();
            String _email = _prefs.getEmail();
            String _birthDate = _prefs.getDOB();
            String _sex = _prefs.getSex();

            if (_fbId == null || _fbId.equals("")) return null;

            try {

                new entUsers().updateUserDetails(getApplicationContext(), _fbId, _firstName, _lastName, _email, _birthDate, _sex);
            } catch (Exception ex) {
                EX = ex;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (EX != null) {
                Log.e("NBT2", EX.getMessage());
            }

            new clsPrefes(getApplicationContext()).putUserFBDetailsServiceCalled(true);
            stopSelf();
        }
    }
}
