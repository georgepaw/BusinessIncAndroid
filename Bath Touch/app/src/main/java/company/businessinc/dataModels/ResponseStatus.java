package company.businessinc.dataModels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gp on 19/11/14.
 */
public class ResponseStatus {
    private boolean status;

    public ResponseStatus(Boolean status) {
        this.status = status;
    }

    public ResponseStatus() {
        this.status = false;
    }

    public ResponseStatus(JSONObject jsonObject) throws JSONException{
        try {
            this.status = jsonObject.getBoolean("status");
        } catch (JSONException e){
            this.status = false;
        }
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
