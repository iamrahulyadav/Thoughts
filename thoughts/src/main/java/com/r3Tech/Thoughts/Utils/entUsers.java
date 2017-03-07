package com.r3Tech.Thoughts.Utils;


import android.content.Context;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class entUsers {

    Integer UID;

    public void UserLogin(Context context) throws Exception {
        try {
            clsPrefes _prefs = new clsPrefes(context);
            Integer _uid = _prefs.getUserId();

            List<NameValuePair> _parameters = new ArrayList<NameValuePair>();
            _parameters.add(new BasicNameValuePair("function", "UserLogin"));
            _parameters.add(new BasicNameValuePair("uniqueuserid", String.valueOf(_uid)));
            _parameters.add(new BasicNameValuePair("foros", "android"));
            String _jsonContent = new clsGeneral().GetJSONContentFromInternetService(context, _parameters, true);
            JSONObject _jObj = new JSONObject(_jsonContent);

            Integer _uidFromService = _jObj.getInt("uniqueuserid");
            if (_uid == 0) {
                _prefs.putUserId(_uidFromService);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void updateUserDetails(Context context, String fbID, String firstName, String lastName, String email, String DOB, String sex) throws Exception {
        try {
            clsPrefes _prefs = new clsPrefes(context);
            Integer _uid = _prefs.getUserId();

            List<NameValuePair> _parameters = new ArrayList<NameValuePair>();
            _parameters.add(new BasicNameValuePair("function", "UpdateUserDetails"));
            _parameters.add(new BasicNameValuePair("uniqueuserid", String.valueOf(_uid)));
            _parameters.add(new BasicNameValuePair("fbid", fbID));
            _parameters.add(new BasicNameValuePair("firstname", firstName));
            _parameters.add(new BasicNameValuePair("lastname", lastName));
            _parameters.add(new BasicNameValuePair("email", email));
            _parameters.add(new BasicNameValuePair("dob", DOB));
            _parameters.add(new BasicNameValuePair("sex", sex));
            _parameters.add(new BasicNameValuePair("foros", "android"));

            String _jsonContent = new clsGeneral().GetJSONContentFromInternetService(context, _parameters, true);
            JSONObject _jObj = new JSONObject(_jsonContent);

            Integer _uidFromService = _jObj.getInt("uniqueuserid");
            if (_uid == 0) {
                _prefs.putUserId(_uidFromService);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
}