package company.businessinc.dataModels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Grzegorz on 03/03/2015.
 */
public class CachedRequest {
    public enum RequestType{SUBMITSCORE}
    private RequestType requestType;
    private JSONObject parameters;

    public static final String KEY_REQUESTTYPE = "requestType";
    public static final String KEY_PARAMETERS = "parameters";
    public static final String[] COLUMNS = {KEY_REQUESTTYPE,KEY_PARAMETERS};
    public static final String CREATE_TABLE =   KEY_REQUESTTYPE + "\tINTEGER,\n" +
                                                KEY_PARAMETERS + "\tTEXT";


    public CachedRequest(RequestType requestType, JSONObject parameters) {
        this.requestType = requestType;
        this.parameters = parameters;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public JSONObject getParameters() {
        return parameters;
    }

    public CachedRequest(Cursor cursor) {
        try {
            this.requestType = recoverRequestType(cursor.getInt(cursor.getColumnIndex(KEY_REQUESTTYPE)));
        } catch(Exception e) {
            this.requestType = null;
        }
        try {
            this.parameters = new JSONObject(cursor.getString(cursor.getColumnIndex(KEY_PARAMETERS)));
        } catch(Exception e) {
            this.parameters = null;
        }
    }

    public static List<CachedRequest> cursorToList(Cursor cursor){
        List<CachedRequest> output = new ArrayList<>();
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                output.add(new CachedRequest(cursor));
                cursor.moveToNext();
            }
        }
        return output;
    }

    public ContentValues toContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_REQUESTTYPE, requestType.ordinal());
        contentValues.put(KEY_PARAMETERS, parameters.toString());
        return contentValues;
    }

    public static RequestType recoverRequestType(int value) {
        for (RequestType my: RequestType.values()) {
            if (my.ordinal() == value) {
                return my;
            }
        }
        return null;
    }
}
