package company.businessinc.dataModels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gp on 19/11/14.
 */
public class Status {
    private boolean status;

    public Status(boolean status) {
        this.status = status;
    }

    public Status(JSONObject jsonObject) throws JSONException{
        this.status = jsonObject.getBoolean("status");
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
