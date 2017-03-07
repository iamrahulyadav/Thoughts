package com.r3Tech.Thoughts.Utils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.r3Tech.Thoughts.Activity.actAbout;
import com.r3Tech.Thoughts.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class clsGeneral {

    // ------------------------------------------------------------------------------------------------------------ Check internet connection
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager _connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean _isConnected = false;
        NetworkInfo _activeNetwork = _connManager.getActiveNetworkInfo();
        if (_activeNetwork != null) {
            _isConnected = _activeNetwork.isConnectedOrConnecting();
        }

        return _isConnected;
    }

    // ------------------------------------------------------------------------------------------------------------ Toast messages
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showToast(Context context, Exception ex) {

        if (ex.getMessage().contains("likebit.com")) {
            // Show no internet connection error if Likebit.com fonds in error string.
            Toast.makeText(context, context.getResources().getString(R.string.NoConnectionError), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // ------------------------------------------------------------------------------------------------------------ Get JSON content from internet
    public String GetJSONContentFromInternetService(Context context, List<NameValuePair> parameters, Boolean checkInternetConnectivity) throws Exception {
        try {
            if (checkInternetConnectivity == true && !clsGeneral.checkInternetConnection(context)) {
                throw new Exception(context.getResources().getString(R.string.NoConnectionError));
            }

            HttpClient _httpClient = new DefaultHttpClient();
            HttpPost _httpPost = new HttpPost(context.getResources().getString(R.string.ServicePath));
            HttpResponse _httpResponse;
            HttpEntity _httpEntity;
            if (parameters != null) {
                _httpPost.setEntity(new UrlEncodedFormEntity(parameters));
            }
            _httpResponse = _httpClient.execute(_httpPost);
            _httpEntity = _httpResponse.getEntity();
            String _retuenStr = EntityUtils.toString(_httpEntity); // JSON COntent
            return _retuenStr;
        } catch (Exception e) {
            throw e;
        }
    }

    // ------------------------------------------------------------------------------------------------------------ Share and support
    public static void shareAndSupport(Context context, String mode) {
        if (mode.equals("feedback")) {

//            final Context _context = context;
//            AlertDialog _dialog = new AlertDialog.Builder(_context).create();
//            _dialog.setTitle(_context.getResources().getString(R.string.AppSupportPopupTitle));
//            _dialog.setMessage(_context.getResources().getString(R.string.AppSupportPopupDescription));
//            _dialog.setCancelable(true);
//            _dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    String _uri = "mailto:" + _context.getResources().getString(R.string.AppSupportEmailAddress)
//                            + "?subject=" + _context.getResources().getString(R.string.AppSupportEmailSubject);
//                    Uri _data = Uri.parse(_uri);
//                    Intent _intentSupport = new Intent(Intent.ACTION_VIEW);
//                    _intentSupport.setData(_data);
//                    _intentSupport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    _context.startActivity(_intentSupport);
//                }
//            });
//            _dialog.show();
            String _uri = "mailto:" + context.getResources().getString(R.string.AppSupportEmailAddress)
                    + "?subject=" + context.getResources().getString(R.string.AppSupportEmailSubject);
            Uri _data = Uri.parse(_uri);
            Intent _intentSupport = new Intent(Intent.ACTION_VIEW);
            _intentSupport.setData(_data);
            _intentSupport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(_intentSupport);
        } else if (mode.equals("rate")) {
            Intent _intentRate = new Intent(Intent.ACTION_VIEW);
            _intentRate.setData(Uri.parse(context.getResources().getString(R.string.MarketLocationForReview)));
            _intentRate.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(_intentRate);
        } else if (mode.equals("friends")) {
            Intent _shareIntent = new Intent(Intent.ACTION_SEND);
            _shareIntent.setType("text/plain");
            _shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.ShareAppWithFriendsSubject));
            String _extraText = context.getResources().getString(R.string.ShareAppWithFriendsText);

            // Append market location with text.
            String _marketLocationShort = context.getResources().getString(R.string.MarketLocationShort);
            String _marketLocationLong = context.getResources().getString(R.string.MarketLocationLong);

            if (_marketLocationLong.equals(_marketLocationShort)) {
                _extraText += "\n\n";
                _extraText += _marketLocationShort;
            } else {
                _extraText += "\n\n";
                _extraText += _marketLocationShort;
                _extraText += "\n\nOr\n\n";
                _extraText += _marketLocationLong;
            }

            _shareIntent.putExtra(Intent.EXTRA_TEXT, _extraText);

            Intent _intentFriends = Intent.createChooser(_shareIntent, "Share this app");
            _intentFriends.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(_intentFriends);
        } else if (mode.equals("about")) {
            Intent _intentAbout = new Intent(context, actAbout.class);
            _intentAbout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(_intentAbout);
        } else if (mode.equals("social")) {
//            Intent _intetnSocial = new Intent(context, actSocial.class);
//            _intetnSocial.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(_intetnSocial);
        }
    }

    // ------------------------------------------------------------------------------------------------------------ Change Fonts
    public static void changeFonts(final Context context, final View v) {
        try {

            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    changeFonts(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "MuseoSansRounded-500.otf"));
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "MuseoSansRounded-500.otf"));
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "MuseoSansRounded-500.otf"));
            }
        } catch (Exception e) {
            /* suppress errors */
        }
    }

    public void generateHashKeyForFacebook(Context context) throws Exception {
        try {
            PackageInfo _info = null;
            _info = context.getPackageManager().getPackageInfo("com.r3Tech.Thoughts", PackageManager.GET_SIGNATURES);
            if (_info == null) {
                Toast.makeText(context.getApplicationContext(), "Invalid Package Name / Package not found", Toast.LENGTH_LONG).show();
                return;
            }
            for (Signature signature : _info.signatures) {
                MessageDigest _md = MessageDigest.getInstance("SHA");
                _md.update(signature.toByteArray());
                Log.d("FB KeyHash: =>", Base64.encodeToString(_md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
