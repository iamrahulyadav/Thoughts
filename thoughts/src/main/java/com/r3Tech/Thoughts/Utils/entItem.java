package com.r3Tech.Thoughts.Utils;


import android.content.Context;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class entItem {

    // ------------------------------------------------------------------------------------------------------------ Properties
    public Integer ID;
    public String CATEGORY;
    public String TAGS;
    public String CONTENT_TYPE;
    public String TITLE;
    public String SMALL_DESC;
    public String FULL_DESC;
    public String THUMB_URL;
    public String EXTERNAL_URL;
    public String ATTRIBUTION;
    public String PUBLISH_DATE;
    public Integer VIEWS;
    public Integer LIKES;
    public Integer IS_FAV;

    // ------------------------------------------------------------------------------------------------------------ getItems
    public List<entItem> getItems(Context context, int pageNumber, String category, String displayMode) throws Exception {
        try {

            List<NameValuePair> _parameters = new ArrayList<NameValuePair>();
            if (displayMode.equals("latest") || displayMode.equals("popular")) {
                _parameters.add(new BasicNameValuePair("function", "GetContentList"));
                _parameters.add(new BasicNameValuePair("pagenumber", String.valueOf(pageNumber)));
                _parameters.add(new BasicNameValuePair("category", String.valueOf(category)));
                _parameters.add(new BasicNameValuePair("dispmode", displayMode));
                _parameters.add(new BasicNameValuePair("foros", "android"));
            } else if (displayMode.equals("fav")) {
                _parameters.add(new BasicNameValuePair("function", "GetUserFavList"));
                Integer _uid = new clsPrefes(context).getUserId();
                _parameters.add(new BasicNameValuePair("uniqueuserid", String.valueOf(_uid)));
                _parameters.add(new BasicNameValuePair("pagenumber", String.valueOf(pageNumber)));
                _parameters.add(new BasicNameValuePair("foros", "android"));
            }
            String _jsonContent = new clsGeneral().GetJSONContentFromInternetService(context, _parameters, true);
            JSONArray _jArr = new JSONArray(_jsonContent);
            List<entItem> _items = new ArrayList<entItem>();

            for (int _i = 0; _i < _jArr.length(); _i++) {
                JSONObject _jObj = _jArr.getJSONObject(_i);
                entItem _item = new entItem();

                _item.ID = _jObj.getInt("Id");
                _item.CATEGORY = _jObj.getString("Category");
                _item.CONTENT_TYPE = _jObj.getString("ContentType");
                _item.TITLE = _jObj.getString("Title");
                _item.SMALL_DESC = _jObj.getString("SmallDesc");
                _item.THUMB_URL = _jObj.getString("ThumbURL");
                _item.PUBLISH_DATE = _jObj.getString("PublishDate");
                _item.VIEWS = _jObj.getInt("Views");
                _item.LIKES = _jObj.getInt("Likes");

                _items.add(_item);
            }
            return _items;
        } catch (Exception ex) {
            throw ex;
        }
    }

    // ------------------------------------------------------------------------------------------------------------ getItem
    public entItem getItem(Context context, int id) throws Exception {
        try {
            List<NameValuePair> _parameters = new ArrayList<NameValuePair>();
            _parameters.add(new BasicNameValuePair("function", "GetContentDetail"));
            _parameters.add(new BasicNameValuePair("contentid", String.valueOf(id)));
            _parameters.add(new BasicNameValuePair("foros", "android"));
            _parameters.add(new BasicNameValuePair("uniqueuserid", String.valueOf(new clsPrefes(context).getUserId())));
            String _jsonContent = new clsGeneral().GetJSONContentFromInternetService(context, _parameters, true);

            entItem _item = new entItem();
            JSONArray _jArr = new JSONArray(_jsonContent);
            if (_jArr.length() == 0) return _item;

            JSONObject _jObj = _jArr.getJSONObject(0);
            _item.ID = _jObj.getInt("Id");
            _item.CATEGORY = _jObj.getString("Category");
            _item.TAGS = _jObj.getString("Tags");
            _item.CONTENT_TYPE = _jObj.getString("ContentType");
            _item.TITLE = _jObj.getString("Title");
            _item.SMALL_DESC = _jObj.getString("SmallDesc");
            _item.FULL_DESC = _jObj.getString("FullDesc").replace("\\n", "\n").replace("\\'", "\'");
            _item.THUMB_URL = _jObj.getString("ThumbURL");
            _item.EXTERNAL_URL = _jObj.getString("ExternalURL");
            _item.ATTRIBUTION = _jObj.getString("Attribution");
            _item.PUBLISH_DATE = _jObj.getString("PublishDate");
            _item.VIEWS = _jObj.getInt("Views");
            _item.LIKES = _jObj.getInt("Likes");
            _item.IS_FAV = _jObj.getInt("IsUserFav");

            return _item;
        } catch (Exception ex) {
            throw ex;
        }
    }

    // ------------------------------------------------------------------------------------------------------------ increaseLikeCount
    public void increaseLikeCount(Context context, Integer id) throws Exception {
        try {
            List<NameValuePair> _parameters = new ArrayList<NameValuePair>();
            _parameters.add(new BasicNameValuePair("function", "UpdateLikeCount"));
            _parameters.add(new BasicNameValuePair("contentid", String.valueOf(id)));
            _parameters.add(new BasicNameValuePair("foros", "android"));
            String _jsonContent = new clsGeneral().GetJSONContentFromInternetService(context, _parameters, true);
            JSONObject _jObj = new JSONObject(_jsonContent);
            if (_jObj.getString("message") == "success") {
                // OK RESULT
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    // ------------------------------------------------------------------------------------------------------------ addToFavList
    public void addToFavList(Context context, Integer id, String action) throws Exception {
        try {
            List<NameValuePair> _parameters = new ArrayList<NameValuePair>();
            _parameters.add(new BasicNameValuePair("function", "UpdateUserFavs"));
            Integer _uid = new clsPrefes(context).getUserId();
            _parameters.add(new BasicNameValuePair("uniqueuserid", String.valueOf(_uid)));
            _parameters.add(new BasicNameValuePair("action", action));
            _parameters.add(new BasicNameValuePair("itemid", String.valueOf(id)));
            String _jsonContent = new clsGeneral().GetJSONContentFromInternetService(context, _parameters, true);
            JSONObject _jObj = new JSONObject(_jsonContent);
            if (_jObj.getString("message") == "success") {
                // OK RESULT
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
}
