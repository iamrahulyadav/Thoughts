package com.r3Tech.Thoughts.Utils;


import android.content.Context;
import android.content.SharedPreferences;
import com.r3Tech.Thoughts.R;


public class clsPrefes {

    // ------------------------------------------------------------------------------------------------------------ Global declarations
    SharedPreferences PREFS;
    SharedPreferences.Editor PREFS_EDITOR;
    Context CONTEXT;

    // ------------------------------------------------------------------------------------------------------------ Constructor
    public clsPrefes(Context context) {
        CONTEXT = context;
        String _prefName = context.getResources().getString(R.string.SharePreferenceName);
        PREFS = context.getSharedPreferences(_prefName, 0);
    }

    private void putString(String key, String value) {
        PREFS_EDITOR = PREFS.edit();
        PREFS_EDITOR.putString(key, value);
        PREFS_EDITOR.commit();
    }

    private String getString(String key) {
        return PREFS.getString(key, "");
    }

    private void putInteger(String key, Integer value) {
        PREFS_EDITOR = PREFS.edit();
        PREFS_EDITOR.putInt(key, value);
        PREFS_EDITOR.commit();
    }

    private Integer getInteger(String key) {
        return PREFS.getInt(key, 0);
    }

    private void putBoolean(String key, boolean value) {
        PREFS_EDITOR = PREFS.edit();
        PREFS_EDITOR.putBoolean(key, value);
        PREFS_EDITOR.commit();
    }

    private boolean getBoolean(String key) {
        return PREFS.getBoolean(key, false);
    }

    public void putUserId(Integer userId) {
        putInteger("UID", userId);
    }

    public Integer getUserId() {
        return getInteger("UID");
    }

    public void putFBId(String fbId) {
        putString("FB_ID", fbId);
    }

    public String getFBId() {
        return getString("FB_ID");
    }

    public void putFirstName(String firstName) {
        putString("FIRST_NAME", firstName);
    }

    public String getFirstName() {
        return getString("FIRST_NAME");
    }

    public void putLastName(String lastName) {
        putString("LAST_NAME", lastName);
    }

    public String getLastName() {
        return getString("LAST_NAME");
    }

    public void putEmail(String email) {
        putString("EMAIL", email);
    }

    public String getEmail() {
        return getString("EMAIL");
    }

    public void putSex(String sex) {
        putString("SEX", sex);
    }

    public String getSex() {
        return getString("SEX");
    }

    public void putDOB(String dob) {
        putString("DOB", dob);
    }

    public String getDOB() {
        return getString("DOB");
    }

    public void putUserFBDetailsServiceCalled(Boolean isServiceCalled) {
        putBoolean("IS_USER_UPDATED_ON_SERVER", isServiceCalled);
    }

    public Boolean getUserFBDetailsServiceCalled() {
        return getBoolean("IS_USER_UPDATED_ON_SERVER");
    }
}
